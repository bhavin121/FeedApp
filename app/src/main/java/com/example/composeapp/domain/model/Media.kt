package com.example.composeapp.domain.model

sealed class Media {
    class Audio(val url:String) : Media()
    /**
     * @param aspectRatio width:height
     * @param url video url
     */
    class Video(val url: String, val aspectRatio:Double, val thumbnailUrl:String) : Media()
    class Photo(val urls:List<String>) : Media()
}