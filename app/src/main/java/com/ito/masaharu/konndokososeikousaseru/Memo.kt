package com.ito.masaharu.konndokososeikousaseru

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Memo(
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var title: String = "",
    open var createdAt: Date = Date(System.currentTimeMillis()),
    open var content: String = ""
) : RealmObject()