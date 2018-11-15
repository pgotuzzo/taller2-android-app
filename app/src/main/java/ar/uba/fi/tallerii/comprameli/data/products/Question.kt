package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Question(@SerializedName("_id") val id: String,
                    @SerializedName("answer") val answer: Answer?,
                    @SerializedName("question") val text: String)
