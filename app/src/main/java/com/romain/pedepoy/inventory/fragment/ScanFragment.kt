package com.romain.pedepoy.inventory.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.romain.pedepoy.inventory.barcode.BarcodeScanningProcessor
import com.romain.pedepoy.inventory.barcode.CameraSource
import com.romain.pedepoy.inventory.data.ProductDatabase
import com.romain.pedepoy.inventory.data.ProductRepository
import com.romain.pedepoy.inventory.databinding.FragmentScanBinding
import com.romain.pedepoy.inventory.viewmodels.ScanViewModel
import com.romain.pedepoy.inventory.viewmodels.ViewModelsFactory
import java.io.IOException
import java.util.*

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private lateinit var scanViewModel: ScanViewModel

    private var cameraSource: CameraSource? = null

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
        val dao = ProductDatabase.getInstance(requireContext()).productDao()
        val repository = ProductRepository(dao)
        val factory = ViewModelsFactory(repository)
        scanViewModel = ViewModelProvider(this,factory).get(ScanViewModel::class.java)
        binding.myViewModel = scanViewModel
        binding.datePicker.visibility = View.GONE
        binding.validateButton.visibility = View.GONE
        binding.lifecycleOwner = this


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker.setOnDateChangedListener(object :DatePicker.OnDateChangedListener{
                override fun onDateChanged(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    scanViewModel.inputDate.value = Date()
                }

            })
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
        //Toast.makeText(this,"selected name is ${subscriber.name}",Toast.LENGTH_LONG).show()

        binding.datePicker.visibility = View.VISIBLE
        binding.validateButton.visibility = View.VISIBLE
        binding.firePreview.stop()
        binding.firePreview.release()
        binding.firePreview.visibility = View.GONE
        scanViewModel.insertFromBarcode(firebaseVisionBarcode)
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
            ActivityCompat.requestPermissions(
                requireActivity(), allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
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