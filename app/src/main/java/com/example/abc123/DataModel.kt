package com.example.test28

data class DataModel(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var birth: String = "",
    var nickname: String = "",
    var uid: String = "",
    var profileImageUrl: String? = null,
    var school: String? = "",
    var major: String? = ""
)

data class SNSDataModel(
    var name: String = "",
    var birth: String = "",
    var nickname: String = ""
)

data class MyBoardTitleModel(
    var Boardtitle: String = "",

    )

data class MyCommentTitleModel(
    var Boardtitle: String = "",
    var Comment: String = ""
)