package com.example.abc123

import java.io.Serializable

class Board_Model3(
    var key: String = "",
    var img1: String = "",
    var img2: String = "",
    var img3: String = "",
    var title: String = "",
    var detail: String = "",
    val name: String = "",
    var location: String = "",
    var sell_delivery: Boolean = false,
    var sell_direct: Boolean = false,
    val price: String = "",
    var time: String = "",
    var time2: String = "",
    var image_count: Int? = null,
    var board_title: String = "",
    var nickname : String = ""


) : Serializable