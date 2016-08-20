package dragon2.common

import java.io.Serializable
import java.util.ArrayList
import java.util.HashSet

import dragon2.common.constant.BodyKind
import dragon2.common.constant.GameColor
import dragon2.common.constant.MoveType
import dragon2.common.constant.BodyAttribute
import mine.MineException

class Body : Serializable, Cloneable {


    var name: String
    var x: Int = 0
    var y: Int = 0
    var level: Int = 0
    var exp: Int = 0
    var hp: Int = 0
    var hpMax: Int = 0
    var str: Int = 0
    var strMax: Int = 0
    var def: Int = 0
    var defMax: Int = 0
    var mst: Int = 0
    var mstMax: Int = 0
    var mdf: Int = 0
    var mdfMax: Int = 0
    var hit: Int = 0
    var hitMax: Int = 0
    var mis: Int = 0
    var misMax: Int = 0
    var moveStep: Int = 0
    var moveType: MoveType = MoveType.NONE
    var color: GameColor = GameColor.NONE
    var img: Int = 0
    var atk: IntArray
    var kind: BodyKind = BodyKind.CHARA


    var attrList: List<BodyAttribute>
    var range: Int = 0
    var scope: Int = 0
    var limitTurn: Int = 0
    var goalX: Int = 0
    var goalY: Int = 0
    var store: Int = 0
    private var attrSet: MutableSet<BodyAttribute>

    init {
        atk = IntArray(6)
        attrList = mutableListOf()
        attrSet = mutableSetOf()
        name = "none"
    }

    fun copy(): Body {
        try {
            return clone() as Body
        } catch (clonenotsupportedexception: CloneNotSupportedException) {
            throw MineException(clonenotsupportedexception)
        }

    }

    fun setMax() {
        hp = hpMax
        str = strMax / 10
        def = defMax / 10
        mst = mstMax / 10
        mdf = mdfMax / 10
        hit = hitMax / 10
        mis = misMax / 10
        store = 8
    }

    fun newType() {
        attrSet = mutableSetOf()
        this@Body.mergeTypeState(attrList)
    }

    fun mergeTypeState(ai: List<BodyAttribute>) {
        for (type in ai) {
            attrSet.add(type)
        }
    }

    fun setTypeState(type: BodyAttribute, flag: Boolean) {
        if (flag) {
            attrSet.add(type)
        } else {
            attrSet.remove(type)
        }
    }

    fun isType(type: BodyAttribute): Boolean {
        return attrSet.contains(type)
    }

    val isAlive: Boolean
        get() = hp > 0

}
