package com.example.mushroomidentification

var recipeList = mutableListOf<Recipe>()

val RECIPE_ID_EXTRA = "bookExtra"

class Recipe (
    var cover:Int,
    var title:String,
    var short_desc:String,
    var main_desc:String,

    val id:Int? = recipeList.size
    )