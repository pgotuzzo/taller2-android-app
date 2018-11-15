package ar.uba.fi.tallerii.comprameli.data.products

import com.google.gson.annotations.SerializedName

data class Answer(@SerializedName("_id") val id: String,
                  @SerializedName("answer") val text: String)