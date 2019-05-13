# FeedBuzz : Projet de génie logiciel et gestion de projet (INFO-F-307)

Logiciel de traitement et de sauvegarde d'articles à partir de flux RSS  
La fenêtre principale du logiciel affiche une liste d'article. Il est possible de sélectionner un article et de le lire, ce qui ouvre une fenêtre secondaire qui affiche l'article.  
Ce logiciel permet de télécharger des articles à partir de différentes sources, il est possible de choisir à partir de quelle sources est-ce que l'on veut télécharger un article.  
Il est également possible de définir différents paramètres, tels que la durée de vie d'un article (la durée du stockage de celui-ci) ou le nombre d'articles que l'on veut télécharger.  


# Utilisation

Version de Java utilisée: Java version 11.0.2  
Version de JavaFX: JavaFX 11  
Librairies utilisées:  
 -Apache.commons.io 2.4  
 -JSonDB 1.0.85  
 -JUnit 5.  
 -JSoup 1.11.3  
 -SLF4J 1.6.1  

<!--TO DO: Informations sur le système de build et la version du Java/Librairies utilisés dans l'implementation.-->

## Compilation

Pour compiler, il faut utiliser les options de compilation suivantes: 

Si vous êtes sur Linux:
 
 --add-modules=javafx.controls,javafx.graphics,javafx.base,javafx.fxml,javafx.web,javafx.swing  
 --module-path "./lib/javafx-sdk-11.0.2-linux/lib" 

Si vous êtes sur Windows:
 
  --add-modules=javafx.controls,javafx.graphics,javafx.base,javafx.fxml,javafx.web,javafx.swing  
 --module-path "./lib/javafx-sdk-11.0.2-win/lib" 
  
Si vous êtes sur OSX:

  --add-modules=javafx.controls,javafx.graphics,javafx.base,javafx.fxml,javafx.web,javafx.swing  
 --module-path "./lib/javafx-sdk-11.0.2-osx/lib" 
  
## Démarrage 

Exécuter le fichier JAR g04-iteration-3.jar dans le dossier dist  
!Attention! Lors du premier démarrage à la création de compte, le programme prend du temps à afficher la liste des articles car il doit la télécharger
<!-- TO DO: Informations sur le démarrage -->

# Configuration :

# Tests

Pour éxécuter les tests, veuillez éxécuter les différentes classes qui sont contenues dans le dossier "test"
Attention! Les tests de la classe HTMLArticleDownloader prennent quelques minutes.
Vous pouvez éxécuter les différentes classes de ce dossier séparément.
<!-- TO DO: Informations sur les tests -->

# Misc

## Développement

Ce logiciel a été développé à 8 personnes avec la méthodologie de l'Extreme Programming.  
Ce groupe est composé de:  
 -Nathan Wolper  
 -Florian Baudry  
 -Mashini Ndele-A-Mulenghe  
 -Hong Phuc Pham  
 -Anass El Zherouni  
 -Sonia Malki  
 -Karim Jastrzebski 
 -Mael Panouillot  

## Screenshot

## License