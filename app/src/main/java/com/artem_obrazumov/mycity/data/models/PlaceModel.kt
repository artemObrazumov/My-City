package com.artem_obrazumov.mycity.data.models;

// Класс места
data class PlaceModel(
    var id : String = "",
    var title : String = "",
    var cityName : String = "",
    var address : String = "",
    var description : String = "",
    var mapLocation : String = "",
    var ratingScore : Int = 0,
    var peopleRated : Int = 0,
    var photos : ArrayList<String>)