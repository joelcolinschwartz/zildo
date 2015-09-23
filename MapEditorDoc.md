# Documentation pour MAPEDIT.EXE #

Il y a certaines petites choses à savoir à propos de l'éditeur:
  * il est très vieux
  * il ne fonctionne qu'en 256 couleurs plein écran
  * il faut le copier dans le dossier où se trouve zildo.jar

Maintenant, vous êtes prêts. Voici un bref manuel d'utilisation :

## Dans tous les modes ##

| **C**|Vide la carte + mode extérieur|
|:-----|:-----------------------------|
| **D**|Vide la carte + mode intérieur|
| **F1..F9**|Charge/retire la n-ième banque de motifs|
| **ESC**|Quitter                       |
| **Page Up/Page Down**|Fait défiler les motifs/sprites à gauche|
| **Flèches directionnelles**|Fait défiler la carte         |
| **R**|Affiche temporairement le numéro de chaque motif à l'écran|
| **Clic Gauche**|Sur les motifs à gauche -> sélectionne le motif|
|      |Sur la carte -> ajoute le motif en cours|
| **Clic Droit**|Sur la carte -> efface le motif|

## Mode Sprite ##

| **E**|Edition d'un personnage (nom, angle, dialogue...)|
|:-----|:------------------------------------------------|
|      |MODE=(0:ami / 1:ennemi)                          |
| **+/- (pavé numérique)**|Changer de phrase                                |
|      |En fin de phrase un '#n' renverra sur la phrase 'n' dans le jeu|

## Explication des motifs prédéfinis ##

| **Nom à l’écran**|Détail|Spécial|Banque|
|:-----------------|:-----|:------|:-----|
| `C1`             |Colline|Faire glisser|1     |
| `CG`             |Entrée de colline gauche|       |1     |
| `CD`             |Entrée de colline droite|       |1     |
| `CB`             |Bordure de colline|Colorie|1     |
| `CM`             |Colline marron|Colorie|1     |
| `R1`             |Petit chemin|Tracer |1     |
| `R2`             |Large chemin|Tracer |1     |
| `O`              |Eau   |Faire glisser|1     |
| `AR`             |	Arbre|       |1     |
| `S`              |Souche|       |1     |
| `ST`             |	Statue|       |1     |
| `V`              |Arche de village|       |2     |
| `GP`             |Grosse pierre|       |2     |
| `M`              |Maison de base (rouge)|Faire glisser|3     |
| `MB`             |Maison bleue|Colorie|3     |
| `MV`             |Maison verte|Colorie|3     |
| `S`              |Souterrain|Tracer |4     |
| `S2`             |Souterrain plus large|Tracer |4     |
| `AR`             |Arbre rouge|       |5     |
| `AJ`             |Arbre jaune|       |5     |
| `R3`             |Route dans le désert|Tracer |6     |

## Options ##

| **Masque**|Activé : les motifs/sprites affichés sont « en l’air » (le feuillage d’un arbre, ou le haut de l’arche par exemple)|
|:----------|:------------------------------------------------------------------------------------------------------------------|
| **Dim**   |Permet de délimiter le coin droit en bas de la carte                                                               |
| **Point** |Permet de placer un point d’enchaînement                                                                           |
| **Motifs / Sprite**|Permet de basculer dans un mode ou dans l’autre                                                                    |
| **Load / Save**|Taper le nom (sans .map) dans le champ et faire load/save pour charger/sauvegarder une carte.                      |

## Exemple ##

Par exemple, pour travailler sur la carte de la Polaky :
  * cliquer sur 'NONAME' en bas à droite de l'écran
  * vider le champ
  * taper 'POLAKY'
  * cliquer sur le bouton 'Load'
  * presser F5 (pour la 5è banque)

Et voilà ! Vous êtes sur la Polaky, et vous pouvez l'éditer !