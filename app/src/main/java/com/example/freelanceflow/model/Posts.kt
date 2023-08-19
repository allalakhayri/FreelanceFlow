package com.example.freelanceflow.model

class Posts {
    var uid: String? = null
    var time: String? = null
    var date: String? = null
    var postimage: String? = null
    var description: String? = null
    var profileimage: String? = null
    var fullname: String? = null

    constructor(
        uid: String?,
        time: String?,
        date: String?,
        postimage: String?,
        description: String?,
        profileimage: String?,
        fullname: String?
    ) {
        this.uid = uid
        this.time = time
        this.date = date
        this.postimage = postimage
        this.description = description
        this.profileimage = profileimage
        this.fullname = fullname
    }
}