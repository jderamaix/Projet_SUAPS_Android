# Projet_SUAPS_Android
Cette application va de paire avec le projet suivant : [Projet SUAPS Symfony](https://github.com/OsheaNorman/projet_SUAPS_Symfony/tree/develop)
L'objectif est de rendre plus facile la gestion d'entrées/sorties dans une salle de sport. Les étudiants allant à la salle de sport utiliseront leur carte étudiante pour signaler leur présence. Les cartes universitaire disposent d'une puce NFC, elles peuvent donc être lu par un téléphone disposant des technologies nécessaire à la lecture de carte. 

## Lancement de l'application
* Posséder un SDK compris entre la version 21 et 26
* Vérifier le port de connection au site Web, il se configure dans le fichier ServiceGenerator.java

## Libraire extérieur à Android.
Il y a deux librairies externes qui sont utilisées :
* Retrofit, qui sert pour communiquer avec des API REST
* GSON, qui sert pour la transmission d'information. GSON a été développé par Google, la version utilisé dans Retrofit est une version adapté.
* Les versions des dépendances peuvent varier en focntion du SDK utilisé pour compiler. Ces dépendance sont renseignées dans la fichier build.graddle du projet.

## Technologie utilisées
* [Android](https://developer.android.com/)
* [NFC](https://developer.android.com/guide/topics/connectivity/nfc/)
* [Retrofit](https://github.com/square/retrofit)

## Gestion de version
Nous avons utilisé la méthode de travail [GitFlow](https://danielkummer.github.io/git-flow-cheatsheet/index.fr_FR.html). 

## Documentation supplémentaire
Une javadoc complète est disponible dans le git

## Licence utilisé
Ce projet est sous licence GPL

## Autheur 
Deramaix Jonathan
