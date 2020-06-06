package com.romain.pedepoy.inventory.scan

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.romain.pedepoy.inventory.R
import com.romain.pedepoy.inventory.barcode.BarcodeScanningProcessor
import com.romain.pedepoy.inventory.barcode.CameraSource
import com.romain.pedepoy.inventory.dagger.Injectable
import com.romain.pedepoy.inventory.dagger.injectViewModel
import com.romain.pedepoy.inventory.databinding.FragmentScanBinding
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ScanFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback, Injectable {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var scanViewModel: ScanViewModel
    private var cameraSource: CameraSource? = null
    private lateinit var binding: FragmentScanBinding

    private val requiredPermissions: Array<String?>
        get() {
            return try {
                val info = requireActivity().packageManager
                    .getPackageInfo(requireActivity().packageName, PackageManager.GET_PERMISSIONS)
                val ps = info.requestedPermissions
                if (ps != null && ps.isNotEmpty()) {
                    ps
                } else {
                    arrayOfNulls(0)
                }
            } catch (e: Exception) {
                arrayOfNulls(0)
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        scanViewModel = injectViewModel(viewModelFactory)
        binding.myViewModel = scanViewModel
        binding.lifecycleOwner = this

        scanViewModel.statusMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        scanViewModel.products.observe(viewLifecycleOwner){
        }

        scanViewModel.editTaskEvent.observe(viewLifecycleOwner){
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.productListFragment, true).build()
            val navController = findNavController()
            navController.navigate(R.id.action_scanFragment_to_productListFragment,null, navOptions)
        }

        scanViewModel.getBarCode.observe(viewLifecycleOwner){
            //Date Picker
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.show(parentFragmentManager, picker.toString())
            picker.addOnCancelListener {
                picker.dismiss()
                scanViewModel.editTask()
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
                scanViewModel.editTask()
            }

            picker.addOnPositiveButtonClickListener {
                scanViewModel.validateExpirationDate(Date(it))
            }
        }

        if (allPermissionsGranted()) {
            createCameraSource()
        } else {
            getRuntimePermissions()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (allPermissionsGranted()) {
            createCameraSource()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(requireActivity(), binding.fireFaceOverlay)
        }

        try {
            cameraSource?.setMachineLearningFrameProcessor(BarcodeScanningProcessor { firebaseVisionBarcode: FirebaseVisionBarcode -> barcodeFoundListener(firebaseVisionBarcode)})

        } catch (e: Exception) {
        }
    }

    private fun barcodeFoundListener(firebaseVisionBarcode: FirebaseVisionBarcode){

        binding.firePreview.stop()
        binding.firePreview.release()
        binding.firePreview.visibility = View.GONE
        scanViewModel.setBarcodeValue(firebaseVisionBarcode)
    }

    private fun startCameraSource() {
        cameraSource?.let {
            try {
                binding.firePreview.start(cameraSource, binding.fireFaceOverlay)
            } catch (e: IOException) {
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(requireContext(), permission!!)) {
                return false
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = arrayListOf<String>()
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(requireContext(), permission!!)) {
                allNeededPermissions.add(permission)
            }
        }

        if (allNeededPermissions.isNotEmpty()) {
            requestPermissions(
                allNeededPermissions.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    companion object {
        private const val PERMISSION_REQUESTS = 1
    }
}