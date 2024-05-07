package com.example.attendance

class User{
    var session_name:String?=null
    var up_date:String? = null
    var location:String? = null
    constructor(){

    }

    constructor(session_name:String?,up_date:String?,location:String?){
        this.up_date = up_date
        this.session_name = session_name
        this.location = location
    }

}