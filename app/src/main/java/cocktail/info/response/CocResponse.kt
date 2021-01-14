package cocktail.info.response

import cocktail.info.data.Cocktail
import com.google.gson.annotations.SerializedName

data class CocResponse(
    @SerializedName("drinks")
    val drinks:ArrayList<Cocktail>
)