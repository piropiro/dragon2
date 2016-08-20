package dragon2.anime

import java.io.Serializable

import dragon2.common.constant.AnimeType

class AnimeData : Serializable {


    // dragon3
    val id = "none"
    val type = AnimeType.NONE
    val image = "none.png"

    // dragon2
    val name = "none"
    val sleep = 50
    var anime = IntArray(1)
}
