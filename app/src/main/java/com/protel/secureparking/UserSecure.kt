package com.protel.secureparking

data class UserSecure (
    val id : String?,
    val email : String,
    val nama : String,
    val password : String
){
    constructor(): this("","","","")
}