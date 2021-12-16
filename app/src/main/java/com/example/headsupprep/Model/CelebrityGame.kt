package com.example.headsupprep.Model


class CelebrityGame {

    var pk: Int? = null
    var name: String? = null
    var taboo1: String? = null
    var taboo2: String? = null
    var taboo3: String? = null

    constructor (pk: Int?, name: String?, taboo1: String?, taboo2: String?, taboo3: String?) {
        this.pk = pk
        this.name = name
        this.taboo1 = taboo1
        this.taboo2 = taboo2
        this.taboo3 = taboo3

    }

}