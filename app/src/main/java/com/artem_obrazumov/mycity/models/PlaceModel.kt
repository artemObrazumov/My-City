package com.artem_obrazumov.mycity.models;

// Класс места
data class PlaceModel(
    var id : String = "",
    var title : String = "",
    var address : String = "",
    var description : String = "",
    var mapLocation : String = "",
    var rating : Float = 0f,
    var photos : ArrayList<String>)