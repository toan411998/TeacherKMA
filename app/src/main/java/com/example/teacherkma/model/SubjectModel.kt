package com.example.teacherkma.model

data class SubjectModel(
    var id: String,
    var name: String,
    var image: String,
    var numberOfLesson: Int,
    var type: String
) {
    override fun toString(): String {
        return "{id='$id', name='$name', image='$image', numberOfLesson=$numberOfLesson, type='$type'}"
    }
}
