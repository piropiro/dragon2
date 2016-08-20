package dragon2.anime.item


import java.awt.Graphics
import mine.util.Point

import dragon2.Statics
import mine.Mine

class Arrow(internal var ba: Point, internal var bb: Point, var max: Int, internal var imgs: IntArray, internal var mflag: Boolean) {
    var x: Int = 0
    var y: Int = 0
    internal var n: Int = 0
    var theta: Double = 0.toDouble()

    init {
        theta = Math.atan2((bb.y - ba.y).toDouble(), (bb.x - ba.x).toDouble())
        n = 0
    }

    fun move() {
        n = Math.min(n + 1, max)
        x = ba.x + (bb.x - ba.x) * n / max
        y = ba.y + (bb.y - ba.y) * n / max
    }

    fun end(): Boolean {
        return n == max
    }

    fun paint(g: Graphics) {
        val i: Int
        if (end())
            i = imgs.size - 1
        else
            i = n % Math.max(1, imgs.size - 1)
        val j = if (mflag) x + 1 else 1
        val k = if (mflag) y + 1 else 1
        val l = imgs[i] % 15 * 32 + 1
        val i1 = imgs[i] / 15 * 32 + 1
        Mine.drawRotateImage(Statics.anime, j, k, l, i1, 30, 30, theta, g)
    }


}
