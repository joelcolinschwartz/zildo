

# Goal of this document #

With the development of the Android port of Zildo, memory became a crucial consideration. Especially when ADB runner responds by a `OutOfMemoryException` !

So in this document, we will try to reduce the memory usage in order to be accepted by an Android device, which is around 8Mb (on mine, which is my first objective).

# Inventory of fixtures #

Let's try to evaluate memory allocated by the game. In this study, we'll focus on NIO buffers allocated via Sprite and Tile engines, respectively [SpriteEngine](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/gfx/engine/SpriteEngine.java) and [TileEngine](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/fwk/gfx/engine/TileEngine.java). Following is a description of what we have in 1.095 (aka Episode 1).

There is 9 tile banks, and 6 sprite banks. Each one is carrying a VBOBuffers, itself having 4 buffers :
  * vertices ( **float** ) sized by 3 x _numPoints_
  * textures ( **float** ) sized by 2 x _numPoints_
  * indices ( **int** ) sized by 3 x _maxIndices_
  * normals ( **float** ) sized by 2 x _numFaces_

The two variables are defined like this ( multiplied by 2 because we consider 2 maps at a time) :
  * _numPoints_ is 2 x 4 x 64 x 64 : 4 vertices per tile
  * _maxIndices_ is 2 x 6 x 64 x 64 : 6 indices per tile
  * _numFaces_ is _maxIndices_ / 3

Let's calculate the size of each buffer :
  * vertices : 3 x _numPoints_ x 4 = 3 x 32768 x 4 = 393 216
  * textures : 2 x _numPoints_ x 4 = 2 x 32768 x 4 = 262 144
  * indices : 3 x _maxIndices_ x 4 = 3 x 49152 x 4 = 589 824
  * normals : 3 x _numFaces_ x 4 = 3 x 8192 x 4= 98 304

So the total size is this multiplied by 15 (total number of banks) = **20 152 320** bytes.

It's OK for every platforms adressed by LWJGL, but too much for a smartphone. So let's see how we can do better.

# Reduction #

Due to some mistakes or forgotten ideas, there's some immediate enhancement we can make, to gain some Mb.

## First phase (easy) : immediate cut ##

There's three things we can do easily, without risking any danger.

### 1) remove normals ###

Normals aren't used now. It was planned to put lights around torch, one day, but this isn't done yet. So there's no harm with this removal.

### 2) reduce precision for indexes ###

Index buffer manipulates numbers between 0 and a maximum calculated like this : 64 x 64 x 4 = 16384.

So we doesn't need an `IntBuffer` : a `ShortBuffer` is enough.

### 3) reuse index buffers ###

Since all tile primitives draw tiles at the same places, with a similar index buffers, we could allocate just one, and reuse it for all primitive.

Instead of 15 buffers, we would have only 1.

### Total ###

So we have a delta of :

-98 304 x 15 ( _normals removal_ )

- 589 824 x 16 ( _all index buffers removal_ )

+ 3 x 49152 x 2 ( _only one index buffer with ShortBuffer_ )

= **10 616 832 Mb**

## Second phase : find another memory holes ##

There's many possibilities, and I'll write here all ideas. Implementation will come in a second time.

### 1) reduce vertices buffer ###

In the current view of verices, there are 4 dots for each tile. And we have 64 x 64 tiles, at maximum size.

A simple enhancement would be to reuse the edge of a tile for the next one. So, instead of 64 x 64 x 4 dots, we would have (64+1) x (64+1), which divides by 4 the amount of needed size.

But that implies that we have a completely filled vertices buffer, and that isn't the case now ! We just put vertices that we are using.

<font color='red'> <b>Impossible</b> : </font>vertex and texture buffers need to be synchronized. It means that vertex could be reduced, but textures need to have 4 dots per face. So we need to have the same for vertices.

### 2) double buffers if needed ###

Instead of allocating max size for each buffer, we could allocate a default size, relatively small, and double it when it becomes insufficient. It means reallocation, with all existing bytes duplication into another buffer, and previous buffer free.

<font color='red'>It remains at the state of idea.</font> We'll see why in the next chapter.

### 3) remove 'z' component from vertices buffer ###

As we only have two dimension for tiles, there's no sense to have a 'z' component in our vertices. Moreover, it's initialized with 0 everywhere.

## Last phase : conclude ##

After all these ideas, I realised that some where good, but some others couldn't be implemented, like the first one.

Instead of reducing vertex buffer, another solution has been considered.
We manipulated primitive with `TilePrimitive` object. But each one was coming from an orginin composed of two attributes : layer (foreground, background and masked) and sprite bank. So we have 3 differents layers, and 7 (and 8) differents sprite (and tile) banks. So it lacked a class level to modelize a layer. So I added the `TileGroupPrimitive`.

At each level, we share a buffer, as we can see in the following schema :
![https://lh4.googleusercontent.com/-qU8cBw5DT1M/T4g_0XF1PsI/AAAAAAAABqI/bFn7skiQJg8/s469/memory.png](https://lh4.googleusercontent.com/-qU8cBw5DT1M/T4g_0XF1PsI/AAAAAAAABqI/bFn7skiQJg8/s469/memory.png)

All primitives shared the same vertex buffer, composed of the complete grid (64x64x4 vertices). Primitives from a group inside a layer shares the same texture buffer, and each primitve has its own indices buffer.

So instead of having 3 x 7 x 3 (layer **bank** buffers) = 63 buffers, we have 1 + 3 +  7 x 3 = 25 buffers, for tiles. We should note that vertex buffer was very high, and now that it's unique, it's a great gain.

With such techniques, the memory usage has reduced to 1.8Mb !

So, problem is solved.

## Post-conclusion : need to think again about memory ##

Considering the last proposition, memory usage is acceptable, and quite excellent though. But performance isn't the same. This isn't enough, and speed issues need this to be challenged.

To be continued in this document.