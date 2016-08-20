package dragon2.anime.item


import java.awt.Graphics

import dragon2.Statics

class Number(var n: Int, var x: Int, var y: Int, var a: Int) {
    internal val e = 0.5
    internal val g = 2
    internal var base: Int = 0

    init {
        base = 20
    }

    fun setBase(i: Int) {
        base = i
    }

    fun move() {
        if (y < 0) {
            y++
            return
        }
        y += a
        if (y >= base) {
            a = -Math.max(0.0, a.toDouble() * 0.5).toInt()
            y = base
        } else {
            a += 2
        }
    }

    fun end(): Boolean {
        return a == 0 && y == base
    }

    fun display(g1: Graphics) {
        if (y < 0) {
            return
        } else {
            g1.drawImage(Statics.num[n], x, y, null)
            return
        }
    }
}

