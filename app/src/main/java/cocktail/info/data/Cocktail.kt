package cocktail.info.data

import com.google.gson.annotations.SerializedName

data class Cocktail(
    @SerializedName("strDrink")
    val nameOfCocktail:String,
    @SerializedName("strDrinkThumb")
    val img :String,
    @SerializedName("idDrink")
    val id :String
)