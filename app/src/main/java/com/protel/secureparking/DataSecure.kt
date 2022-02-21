package com.protel.secureparking

data class DataSecure(
    val id : String?,
    val name : String,
    val plat : String,
    val faceImageurl : String,
    val STNKImageurl : String
){
    constructor(): this("","","", "","")
}