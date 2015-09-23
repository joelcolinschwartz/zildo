# Documentation technique #




---


Le projet se divise en 3 parties:
  * **fwk** : framework
  * **gui** : l'interface utilisateur
  * **monde** : le "métier", ou modèle

Ca s'apparente un peu à du MVC, où le framework symboliserait la couche Contrôleur, mais il est avant tout le conteneur de tout ce qui est technique dans Zildo.

La partie GUI, encore pauvre, représente tout ce qui se rajoute au visuel des tiles et des sprites.

Tout le côté RPG, jeu de rôle, qualifié de "métier", est dans le package ["monde"](http://code.google.com/p/zildo/source/browse/trunk/src/zildo/monde/).


## zildo.fwk ##

Bientôt...

## zildo.gui ##

Bientôt...

## zildo.monde ##

### 1.1) Les tiles ###

Les tiles sont les "tuiles" de 16x16 qui composent la carte où les protagonistes de Zildo se déplacent.

On distingue les tiles de premier plan et d'arrière plan. Ce qui est au premier plan peut cacher une partie des éléments qui sont proches. Par exemple, si Zildo se trouve vers un arbre, c'est l'arbre qui cachera Zildo. De même pour une porte de maison, où l'entrée d'un village.

Les tiles peuvent être animées, comme les fleurs, où l'eau sur le rivage.

La classe qui modélise ces tiles est la classe [Case](http://code.google.com/p/zildo/source/browse/trunk/src/zildo/monde/Case.java).

### 1.2) Les sprites ###

La classe qui régit tout sprite, du plus statique au plus évolué, hérite de la classe `SpriteEntity`. Cette classe représente les infos minimales pour modéliser une entité quelconque sur une carte.

Depuis cette classe, on déroule pour complexifier la notion d'entité et on observe la hiérarchie de classe suivante:
```
SpriteEntity
|
+->Element
   |
   +->Perso
```
On a là les 3 classes essentielles de toute entité qui peut se trouver sur une carte.

> #### a/ `SpriteEntity` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/src/zildo/monde/decors/SpriteEntity.java) ####

La première représente les éléments statiques, qui ne se déplacent pas, et ne bloquent pas le passage. On trouvera par exemple : les feuilles des buissons qui s'éparpillent, la fumée qui se dissipe d'une cheminée, ...

Cette classe dispose de coordonnées (X, Y) à l'écran, d'informations sur son visuel, et divers indicateurs (visible, premier/arrière plan, effet spécial...)

> #### b/ `Element` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/src/zildo/monde/decors/Element.java) ####

Il s'agit d'une entité plus particulière, qui peut se mouvoir dans le monde. Elle est sujette aux lois de la mécanique. De plus, elle peut être bloquante pour les autres éléments de la carte.

Elle dispose de coordonnées dans le monde (X, Y, Z), de vitesse, d'accélération, et de forces de frottements. Elle peut être liée à une autre entité.

> #### c/ `Perso` [(SVN)](http://code.google.com/p/zildo/source/browse/trunk/src/zildo/monde/persos/Perso.java) ####

La classe de base la plus évoluée pour représenter un personnage sur une carte. On a ici des infos sur le comportement, la zone d'action, le caractère hostile/amical, ou encore le dialogue du personnage.

> #### d/ Hiérarchie plus complète ####

Ces classes de base s'étoffent par d'autres classes qui représentent des personnages plus précis, dans leur comportement, ou leur représentation graphique. Voici une hiérarchie plus détaillée.

http://lh4.ggpht.com/_q_5wHG9LsPk/SfRjrW3Cf_I/AAAAAAAABJE/Q02fzQO9vtk/s800/spriteClassesHierarchy.JPG

On voit que les personnages se scindent en 2 catégories:
  * les non joueurs (`PersoNJ`)
  * les joueurs (`PersoZildo`) qui n'existent qu'en un seul exemplaire pour l'instant