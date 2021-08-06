package com.artem_obrazumov.mycity.data.models;

// Класс места
data class Place(
    var id : String = "",
    var title : String = "",
    var cityName : String = "",
    var address : String = "",
    var description : String = "",
    var mapLocation : String = "",
    var ratingScore : Int = 0,
    var peopleRated : Int = 0,
    var attachment : ArrayList<Attachment>)