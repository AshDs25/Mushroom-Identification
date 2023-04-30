package com.example.mushroomidentification

class MushroomItem {

    var id : Int = 0
    var c_name: String = ""
    var sci_name: String = ""
    var accuracy: String = ""

    constructor(c_name:String, sci_name:String, accuracy: String){
        this.c_name = c_name
        this.sci_name = sci_name
        this.accuracy = accuracy
    }
    constructor(){
    }

//
//    public fun getCommonName() : String {
//        return c_name
//    }
//
//    fun getScientificName() : String {
//        return sci_name
//    }
//
//    @JvmName("getAccuracy1")
//    fun getAccuracy() : String {
//        return accuracy
//    }

}

