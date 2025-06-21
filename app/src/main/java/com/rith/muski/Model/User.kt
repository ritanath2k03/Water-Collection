package com.rith.muski.Model



data class User(
    val name: String,
    val email: String,
    var phone: String,
    var address: String,
    val uId: Long? = 0
) {

}
