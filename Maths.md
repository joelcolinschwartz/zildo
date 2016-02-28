

# Introduction #

These pages are only a reminder about encountered maths problematic during Zildo development. Some things aren't easy to describe in Javadoc !


# Anticipate a target movement #

It's a common need in video game in order to enhance the IA behavior. For example, in Quake, when a bot is shooting a rocket on a player, he has to target the future location of the player, not his current one.

In Zildo, the first implementation came with the bat. Indeed, if the bat fly on Zildo, it has to anticipate. If it doesn't, Zildo has nothing to worry about : he might never be wounded at all !

## Description ##

Consider a bat at the M position, and Zildo at Z. We know that Zildo is moving along the blue axis, according to the delta of his current location, and previous frame. So where should the bat fly in order to hit him ?

![https://lh5.googleusercontent.com/-Mnel-k7LQfM/T7aXJREqHpI/AAAAAAAABrA/mOC5alIcWF8/s261/anticipe.png](https://lh5.googleusercontent.com/-Mnel-k7LQfM/T7aXJREqHpI/AAAAAAAABrA/mOC5alIcWF8/s261/anticipe.png)

## Solving ##

Let's write Zildo location (rectilinear movement) :
```

x = Zx + n * deltaX
y = Zy + n * deltaY
```
And bat one (on a circle around M, with radius = speed **n) :
```
       (x - Mx)² + (y - My)² = (speed * n)²
```
Each one has his own speed. Zildo's one is included into delta. `n` is the number of frame until when M and Z will collide. It is the only unknown value. From it, we can deduce everything.**

So if both character are colliding, we can inject Zildo's location into the circle equation :

```

(Zx + n * deltaX - Mx)² + (Zy + n * deltaY - My)² = (speed * n)²
```
Let's simplify with `Dx = Zx - Mx` and `Dy = Zy - My`. Rewrite it :
```

(Dx + n * deltaX)² + (Dy + n * deltaY)² = (speed * n)²

<=>

Dx² + 2 * Dx * n * deltaX + n² * deltaX² + Dy² + 2 * Dy * n * deltaY + n² * deltaY² = speed² * n²

<=>

n² * (deltaX² + deltaY² - speed²) + n * (2 * Dx * deltaX + 2 * Dy * deltaY) + Dx² + Dy² = 0
```
So we have a typical quadratic equation (Ax² + Bx + C = 0). Let's find the discriminator delta, and resolve it with following variables :
```

a = deltaX² + deltaY² - speed²
b = 2 * Dx * deltaX + 2 * Dy * deltaY
c = Dx² + Dy²
```
There is one special case, when `a = 0` : so the root is just `-c / n`. But otherwise we have :
```
delta = b² - 4 * a * c

r1 = (-b - sqrt(delta)) / (2 * a)
r2 = (-b + sqrt(delta)) / (2 * a)
```
Negatives can occur, so we have to get the absolute value for `n`.

And once we have the number of frames, we just have to use Zildo's movement equation to determine the collision location.

Problem solved !
