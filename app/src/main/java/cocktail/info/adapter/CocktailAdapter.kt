package cocktail.info.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import cocktail.info.R
import cocktail.info.data.Cocktail
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import com.squareup.picasso.Picasso

class CocktailAdapter : RecyclerView.Adapter<CocktailAdapter.UsersViewHolder>(){

    private var list = ArrayList<Cocktail>()
    private var label:String=""
    private var counter:String=""
    var idList = mutableListOf<String>()
    var map = HashMap<String,String>()

    inner class UsersViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){

        fun bind(cocktail: Cocktail, position: Int, itemViewType: Int){
            with(itemView){
                val nameOfCocktail:TextView=findViewById(R.id.name_of_cocktail)
                nameOfCocktail.text=cocktail.nameOfCocktail
                val imgCocktail:ImageView=findViewById(R.id.img_cocktail)
                Picasso.get().load(cocktail.img)
                    .fit().centerCrop()
                    .into(imgCocktail)
                val card: RelativeLayout =findViewById(R.id.card)
                val labelOfCocktail:TextView=findViewById(R.id.label_of_cocktail)
                labelOfCocktail.text=label

                if(cocktail.id in idList){
                    labelOfCocktail.visibility = View.VISIBLE
                    labelOfCocktail.text = map[cocktail.id]
                }

                if (counter.equals(cocktail.id)||cocktail.id in idList){
                    labelOfCocktail.visibility = View.VISIBLE
                    idList.add(cocktail.id)
                    map.put(cocktail.id,label)
                }
                else{
                    labelOfCocktail.visibility = View.GONE
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_cocktail,parent,false)
        return  UsersViewHolder(view)
    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(list[position],position,holder.itemViewType)
    }

    fun addList(
            items: ArrayList<Cocktail>,
            listResponse2: String,
            count: String
    ){
        list.addAll(items)
        label=listResponse2
        counter=count
        notifyDataSetChanged()
    }
    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}