package com.example.abc123

import java.io.Serializable

class Board_Model2(
    var key: String = "",
    var img1: String = "",
    var img2: String = "",
    var img3: String = "",
    var title: String = "",
    var detail: String = "",
    val name: String = "",
    val comment_count: Int? = null,
    val views_count: Int? = null,
    var time: String = "",
    var time2: String = "",
    var image_count: Int? = null,
    var board_title: String = "",
    var nickname : String = ""

) : Serializable