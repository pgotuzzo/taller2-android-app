package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Question(@SerializedName("_id") val id: String? = null,
                    @SerializedName("answer") val answer: Answer? = null,
                    @SerializedName("question") val text: String)
