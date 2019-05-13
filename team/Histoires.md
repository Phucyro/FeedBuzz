# Histoires
Informations récapitulatives concernant les différentes histoires.

#### Quelques précisions
Un point correspond à une heure de travail par binôme (approximatif). Par itération il faut accomplir X points.

----------------------


## Pondération

| Priorité/3 | N° | Description | Difficulté/3 | Risque/3 | Heures/? | Points |
| ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| 1 | /1 | Visualisation des articles | 2 | 3 | 40 | 24 |
|   | /3 | Récupération des articles à partir des sources extérieures |  |  |  | 30 |
|   | 4 | Récupération des articles à partir de pages web |  |  |  | 34 |
| 2 | 2 | Recherche et filtres sur les articles stockés localement | 3 | 3 | 25 | 24 |
| 2 | 5 | Création d’un système de recommandation | 3 | 3 | 54 | 55 |
| 2 | /7 | Intégrité des données | 2 | 2 | 15 | 16 |
|   | 9 | Vérification de la fiabilité d’un article |  |  |  | 40 |
|   |10 | Gestion des articles |  |  |  | 36 + 19 |
| 2 |/11 | Gestion de plusieurs utilisateurs | 2 | 2 |  | 20 |
| 3 |/12 | Support pour des médias différents (vidéo, images, etc.) | 1 | 2 | 30 | 40 |
| 3 | 6 | Sécurité des données | 1 | 2 | ? | 30 |
|   | 8 | Intégration avec des réseaux sociaux |  |  |  | 54 |
|   |13 | Continuous learning pour le système de recommandation |  |  |  | 50 + 30 |
|   |14 | Filtrage des articles sur base de la rélevance géographique (sélection d’une région à l’aide d’une carte)|  |  |  | 60 |
| 3 |/15 | Section d’aide | 3 | 3 | 15 | 20 |

----------------------


## Description

### Visualisation des articles

**Instructions originales:**           
- Fenêtre principale et affichage.
- Survol des articles et affichage d'une petite description, dans une bulle, concernant un article.
- Fenêtre spécifique à l'article.
- Interface pour les tags.
- Visualisation des sources. 
- copier le lien de l'article.

**Question:**       
- _Pas de question._

**Choix d'implémentations:**
- Les articles sont affichés les uns à la suite des autres.
- Chaque "cellule" d'article est composée : d'une image/gif, du titre, d'un tag et de la source.
- Avant de pouvoir ouvrir un article, il faut le sélectionner en cliquant dessus et ensuite on peut le lire.
- De même pour copier le lien d'un article, il faut le sélectionner avant.
- Affichage d'autres options dans la fenêtre principale (aide, configurations, etc...).

### Récupération des articles à partir des sources extérieures

**Instructions originales:**           
- Sauvegarde des fichiers selon les chiffres données par le GUI.
- Parsing du flux.
- Interface graphique pour choisir les sources et les dates limites/nombre d'articles à télécharger.
- Supprimer les articles.
- Téléchargement des articles au lancement du programme.
- Recherche des articles par leur titre.

**Question:**   
- _Pas de question._    

**Choix d'implémentations:**
- Création d'une base de donnée regroupant les articles à télécharger.


### Intégrité des données

**Instructions originales:**       
- Encryptage des données.    
- Faire le hash et la comparaison d'intégrité.
- Mettre à jour l'article si le hash n'est pas bon.
- Afficher l'intègrité du fichier.

**Question:** 
- _Pas de question._

**Choix d'implémentations:**
- L'affichage de l'intégrité, lors de la lecture de l'article, est soit vert si intègre, soit rouge si non.


### Support pour des médias différents (vidéo, images, etc.)

**Instructions originales:**       
- Affichage des médias originels issus des articles.
- Respect du format originel de l'article.
- Possibilité de voir une vidéo, d'écouter un audio s'il y en a.

**Question:** 
- _Pas de question._

**Choix d'implémentations:**
- Utilisation de webview pour conserver le format de l'article.

### Connexion

**Instructions originales:**     
- Fenêtre de connexion.
- Gestion des connexions "sign in" et "register".
- Volonté de devoir se connecter pour utiliser l'application.

**Question:** 
- _Pas de question._

**Choix d'implémentations:**
- Ouverture d'une fenêtre au lancement du programme.
- Création d'une base de donnée pour les utilisateurs.


### Section d'aide

**Instructions originales:**       
- Section d'aide pour l'utilisateur.
- Pas d'ouverture d'une seconde fenêtre.

**Question:** 
- _Pas de question._

**Choix d'implémentations:**
- Rajout dans la fenêtre principale
- Utilisation de screenshots pour illustrer les différentes manières d'utiliser l'application

### Création d’un système de recommendation

**Instructions originales:**       
- Système de recommendation pour l'utilisateur

**Question:** 
- _Pas de question._

**Choix d'implémentations:**
- recommendation sur base du temps passé sur un article, d'un système de like/dislike et le tag de l'article.

**Instructions supplémentaires:**
- Révision du système d'assignation des tags
- Implémentation d'une word list pour déterminer les tags.