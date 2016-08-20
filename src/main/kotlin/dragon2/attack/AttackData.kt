package dragon2.attack

import java.io.Serializable
import java.util.ArrayList

import dragon2.common.constant.AnimeType
import dragon2.common.constant.DamageType
import dragon2.common.constant.AttackEffect
import dragon2.common.constant.EnergyType
import dragon2.common.constant.GameColor
import dragon2.common.constant.TargetType

class AttackData : Serializable {

    // dragon3
    private val id = "none"
    private val animeId = "none"
    private val image = "none.png"

    // dragon2
    var name = "none"
    var label = "none"
    var labelColor: GameColor? = null
    var effect: List<AttackEffect> = ArrayList()
    var damageType: DamageType? = null
    var attackN1: Int = 0
    var targetType: TargetType? = null
    var animeType: AnimeType? = null
    var animeN1: Int = 0
    var energyType: EnergyType? = null
    var energyCost: Int = 0

}
