# Drawing algorithm : 12-tiled patches #

In ZEditor, in order to draw roads, forest border, ... we use a simple algorithm called here 12-tiled patches. So user can drag his mouse on the map, and a path is drawn magically.

![https://lh3.googleusercontent.com/_q_5wHG9LsPk/TZhHrY3BPEI/AAAAAAAABnc/0loAkwOWAeU/s288/example.png](https://lh3.googleusercontent.com/_q_5wHG9LsPk/TZhHrY3BPEI/AAAAAAAABnc/0loAkwOWAeU/s288/example.png)

Basically, we have a set of tiles (12 to draw a shape) describing all possibilities with a square divided into 4 sub-squares.


## Binary view ##

Let's take a simple square, and cut it twice on the middle of each side.
Then we assign a power of 2 in each region to get this :

| 8 | 4 |
|:--|:--|
| 2 | 1 |

This is sized like a tile in the game, and valued with the sum of each region.
Now we must think about something to draw, a path for example. We assume that a tile without any piece of path is 0-valued, and another with full path is 15-valued.

How much different values have we ? In ZEditor, only 12 values :
  * outside corners : 1, 2, 4, 8
  * straight : 12, 5, 3, 10
  * inside corners : 11, 13, 14, 7
Plus simple ones :
  * empty : 0
  * full : 15

Note that diagonal values are excluded : 6, 9.

It gives this :

| 8 | 12 | 4 |
|:--|:---|:--|
| 10 | 15 | 5 |
| 2 | 3  | 1 |

and (for inside corners) :

| 11 | 13 |
|:---|:---|
| 14 | 7  |

## Graphical view ##

Let's take the forest border example.

![https://lh6.googleusercontent.com/_q_5wHG9LsPk/TZhHrclsEBI/AAAAAAAABng/xweTpoTzSSc/s800/tuto.png](https://lh6.googleusercontent.com/_q_5wHG9LsPk/TZhHrclsEBI/AAAAAAAABng/xweTpoTzSSc/s800/tuto.png)

We have outside corners on the left, and inside ones on the right.

## Java algorithm ##

There is an abstract class called [AbstractPatch12](http://code.google.com/p/zildo/source/browse/trunk/src/zeditor/core/prefetch/complex/AbstractPatch12.java) which deals of this kind of technic.

All we have to do is provide two methods in a derived class :
  1. toBinaryValue
  1. toGraphicalValue

The first one gets a tile value from the map and returns the binary view.

The second one does the opposite, returning the tile value to put it on the map.