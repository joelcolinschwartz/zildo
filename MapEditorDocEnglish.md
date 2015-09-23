# Documentation for MAPEDIT.EXE #

There is several things to know about it:
  * it is very old
  * it only works in 256 colors full screen mode
  * you should copy this file into the directory where you unzipped the game, nearby zildo.jar.

Now you're ready for it. Here is a brief manual to use it :

## All modes ##

| **C**|Clear map + outside view|
|:-----|:-----------------------|
| **D**|Clear map + inside view |
| **F1..F9**|Load/unload  n-th tiles bank|
| **ESC**|Quit                    |
| **Page Up/Page Down**|Scroll tiles/sprites on the left|
| **Keypad**|Scroll map              |
| **R**|Display temporarily tiles number at screen|
| **Left click**|Tiles on the left -> select tile|
|      |Map -> add current tile |
| **Right click**|Map -> clear tile       |

## Sprite Mode ##

| **E**|Edit character (name, angle, dialog...)|
|:-----|:--------------------------------------|
|      |MODE=(0:friend / 1:enemy)              |
| **+/- (numeric pad)**|Change sentence                        |
|      |At the end, type '#n' to redirect to the n-th sentence|

## Predefined patterns ##

| **Name**|Detail|Special|Bank|
|:--------|:-----|:------|:---|
| `C1`    |Hill  |Drag   |1   |
| `CG`    |Left hill start|       |1   |
| `CD`    |Right hill start|       |1   |
| `CB`    |Hill border|Fill   |1   |
| `CM`    |Brown hill|Fill   |1   |
| `R1`    |Small road|Trace  |1   |
| `R2`    |Large road|Trace  |1   |
| `O`     |Water |Drag   |1   |
| `AR`    |Tree  |       |1   |
| `S`     |Stub  |       |1   |
| `ST`    |Statue|       |1   |
| `V`     |Village arch|       |2   |
| `GP`    |Big rock|       |2   |
| `M`     |Red house|Drag   |3   |
| `MB`    |Blue house|Fill   |3   |
| `MV`    |Green house|Fill   |3   |
| `S`     |Cave  |Tracer |4   |
| `S2`    |Larger cave|Trace  |4   |
| `AR`    |Red tree|       |5   |
| `AJ`    |Yellow tree|       |5   |
| `R3`    |Desert road|Trace  |6   |

## Options ##

| **Masque**|Activated : Tiles/Sprites added on foreground (tree's leafs, or village arch  for example)|
|:----------|:-----------------------------------------------------------------------------------------|
| **Dim**   |Size the map                                                                              |
| **Point** |Place a chaining point                                                                    |
| **Motifs / Sprite**|Switch mode                                                                               |
| **Load / Save**|type name (without .map) in the field and click load/save to load/save a map.             |

## Example ##

For example, to edit the Polaky map, do the following:
  * click on the 'NONAME' text on right-bottom corner
  * clear the field
  * type 'POLAKY'
  * click on load button
  * press F5 (for fifth tiles bank)

And voilà ! You're viewing Polaky and can edit it !