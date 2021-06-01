package com.capstoneproject.cmask.model

class History(
    var date: String? = null,
    var feature: String? = null,
    var keterangan: String? = null,
    var nilaiAkurat: Double? = null,
    var time: String? = null,
    var photo: String? = null
) {
    override fun toString(): String {
        return "$date $feature $keterangan $nilaiAkurat $time $photo"
    }
}
