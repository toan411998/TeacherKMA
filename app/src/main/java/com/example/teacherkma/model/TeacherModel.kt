package com.example.teacherkma.model

data class TeacherModel(
    var id: String,
    var username: String,
    var name: String,
    var gender: String,
    var majorsId: String,
    var image: String,
    var numOfStudyLesson: Int = 0,
    var numOfTeachingLesson: Int = 0,
    var date: String,
    var address: String,
    var phone: String,
    var mail: String,
    var password: String
) {
    override fun toString(): String {
        return "{id='$id', username='$username', name='$name', gender='$gender', majorsId='$majorsId', image='$image', numOfStudyLesson=$numOfStudyLesson, numOfTeachingLesson=$numOfTeachingLesson, date='$date', address='$address', phone='$phone', mail='$mail', password='$password'}"
    }
}
