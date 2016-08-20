package dragon2.anime.item


class Abc(var w: Int, var xf: Int, internal var wait: Int) {
    var x: Int = 0
    var y: Int = 0
    internal var time1: Int = 0
    internal var time2: Int = 0
    internal val v = 2

    init {
        x = xf - 32
        y = 10
        time1 = 0
        time2 = 0
    }

    fun move1() {
        if (++time1 > wait)
            x = Math.min(x + 2, xf)
    }

    fun move2() {
        if (++time2 > wait)
            x += 2
    }

    fun end1(): Boolean {
        return x == xf
    }

    fun end2(): Boolean {
        return x > 32
    }


}
