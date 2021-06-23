package com.example.nidhi.whatsappclone.models

import java.sql.Time
import java.util.*

data class Inbox(
    val msg: String,
    var from: String,
    var name: String,
    var image: String,
    val time: Date = Date() ,
    var count: Int = 0
        )

{
    constructor() : this("", "", "", "", Date(), 0)
}
