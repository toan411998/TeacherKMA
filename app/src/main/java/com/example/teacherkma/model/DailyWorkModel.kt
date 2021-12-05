package com.example.teacherkma.model

data class DailyWorkModel(
    var id: String = "-1",
    var teacherId: String = "",
    var subjectId: String = "",
    var subjectName: String = "",
    var date: String = "",
    var lesson: Int = 0,
    var room: String = "",
    var state: Int = 0,
    var startTime: String = "",
    var endTime: String = ""
) {
    override fun toString(): String {
        return "{id='$id', teacherId='$teacherId', subjectId='$subjectId', subjectName='$subjectName', date='$date', lesson='$lesson', room='$room', state='$state', startTime='$startTime', endTime='$endTime'}"
    }
}