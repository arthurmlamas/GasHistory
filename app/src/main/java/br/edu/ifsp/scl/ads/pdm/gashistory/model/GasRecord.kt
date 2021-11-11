package br.edu.ifsp.scl.ads.pdm.gashistory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GasRecord(
    var date: Date? = null,
    var price: Double? = null
): Parcelable
