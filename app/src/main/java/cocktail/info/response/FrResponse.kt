package cocktail.info.response

import cocktail.info.data.Filter
import com.google.gson.annotations.SerializedName

data class FrResponse(
    @SerializedName("drinks")
    val filters:ArrayList<Filter>
)