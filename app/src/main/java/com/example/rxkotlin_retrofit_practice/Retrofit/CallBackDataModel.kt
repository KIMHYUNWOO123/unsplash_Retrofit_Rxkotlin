package com.example.rxkotlin_retrofit_practice.Retrofit


data class Result(
    val results : List<CallBackDataModel>
)

//data class User(
//    var user : CallBackDataModel
//)
data class CallBackDataModel(
    val urls : Image_Url
)

data class Image_Url (
    val raw : String
        )


