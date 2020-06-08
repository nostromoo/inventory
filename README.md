Inventory - Romain PEDEPOY
=========================


Choix de l'architecture et des composants
------------

J'ai décidé d'utiliser les Android Architecture Components et de mettre en place les bonnes pratiques que Google conseille : une architecture sous forme de layer ( activity -> viewmodel -> repository -> WS et BDD)
L'avantage de cette architecture est que chaque layer ne connait que le layer en contact avec lui et pas les autres, il y a une séparation entre l'UI, la logique métier et la couche data (repository).
J'utilise donc un pattern MVVM avec DataBinding, LiveData, Room pour la base de données, Retrofit pour les appels réseaux, et Navigation pour la navigation entre les écrans.


Scanner de code-barres
------------

J'utilise ML Kit, puisque c'est le composant de Google. Après une rapide recherche, cela semblait être l'option la plus évidente.
j'ai fait mes tests avec des code-barres GTIN 13 sur le site OpenFoodFacts.

Gestion de la persistence
------------

J'utilise Room pour la base de données car il fait partie intégrante des AAC.
Le LiveData se charge de déclencher la mise à jour de l'UI.

Appels réseaux
------------

J'utilise l'API de OpenFoodFact pour récupérer le nom et l'image des produits, avec Retrofit et les coroutines.

DI
------------

Les différents composents (VM, repository, retrofit, DAO) sont injectés avec Dagger(ce qui est recommandé par Google).

Axes d'amélioration
------------

Concernant la partie scan de code-barre, ne maitrisant pas ce sujet, j'ai récupéré des classes dans la sample app de ML Kit que j'ai copiées. Il y a certainement moyen d'optimiser le code.
Le design peut également être amélioré.
On pourrait aussi mettre en place des tests unitaires.
