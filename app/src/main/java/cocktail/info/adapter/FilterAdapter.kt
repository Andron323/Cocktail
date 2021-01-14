package cocktail.info.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import cocktail.info.*
import cocktail.info.data.Filter
import java.util.ArrayList

class FilterAdapter(var mCtx: Context?, var resourcec:Int, var items: ArrayList<Filter>):ArrayAdapter<Filter>(mCtx!!,resourcec,items) {

    var checkedL = mutableListOf<Int>()
    var checkedLi = mutableListOf<String>()
    var RF:Boolean=true

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(resourcec,null)

        val nameOfCocktail:TextView =view.findViewById(R.id.name_of_filter)
        val checkBox:CheckBox =view.findViewById(R.id.checkBox)

        if (position in checkedL){
            checkBox.isChecked = true
            checkBox.isEnabled=false
        }

        var mitem: Filter = items[position]

        val sharedPreferences = context!!.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val saveLength: IntRange = 0..sharedPreferences.getInt("lengthCheck",0)
        if (RF) {
            for (h in saveLength) {
                val saveString = sharedPreferences.getString("check$h", null)

                    if (saveString !in checkedLi && saveString != null) {
                        checkedLi.add(saveString)
                    }
            }
            RF = false
        }
        for (h in saveLength){
            val saveString = sharedPreferences.getString("check$h", null)
            if (mitem.filterName.equals(saveString)){
                checkBox.isChecked = true
                if (saveString !in checkedLi){
                    checkedLi.add(saveString)
                }
            }
        }

        nameOfCocktail.text = mitem.filterName

        checkBox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                checkedLi.add(mitem.filterName)

                var h = 0
                for (str in checkedLi){
                    val sharedPreferences = context!!.getSharedPreferences("shared",Context.MODE_PRIVATE)
                    val editop = sharedPreferences.edit()
                    editop.apply{
                        putString("check$h",str)
                        putInt("lengthCheck", h)
                        h++
                    }.apply()
                }
            }else{
                checkedLi.remove(mitem.filterName)

                var h = 0
                if (checkedLi.isNotEmpty()){
                    for (str in checkedLi){
                        val sharedPreferences = context!!.getSharedPreferences("shared",Context.MODE_PRIVATE)
                        val editop = sharedPreferences.edit()
                        editop.apply{
                            putString("check$h",str)
                            putInt("lengthCheck", h)
                            h++
                        }.apply()
                    }
                }else{
                    val sharedPreferences = context!!.getSharedPreferences("shared",Context.MODE_PRIVATE)
                    val editop = sharedPreferences.edit()
                    editop.apply(){
                        clear()
                    }.apply()
                }
            }
        }
        return view
    }

}