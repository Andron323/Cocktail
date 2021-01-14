package cocktail.info.data

import com.google.gson.annotations.SerializedName

data class Filter(
    @SerializedName("strCategory")
    val filterName:String
)