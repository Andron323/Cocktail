package cocktail.info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import cocktail.info.adapter.FilterAdapter
import cocktail.info.data.Filter
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var list = ArrayList<Filter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_filter, container, false)

        val activity: MainActivity? = activity as MainActivity?
        val myDataFromActivity: ArrayList<Filter> = activity!!.sendParams()

        val listOfFilter = v.findViewById<ListView>(R.id.list_view_of_filter)
        listOfFilter.adapter= FilterAdapter(
            context,
            R.layout.item_list_filter,
            myDataFromActivity
        )

        val btnApply = v.findViewById<Button>(R.id.btn_apply)
        btnApply.setOnClickListener {

            val sharedPreferences = context!!.getSharedPreferences("shared", Context.MODE_PRIVATE)
            val saveLength: IntRange = 0..sharedPreferences.getInt("lengthCheck",0)
            for (h in saveLength){
                val saveString = sharedPreferences.getString("check$h",null)
                if (saveString!=null && Filter(saveString) !in list){
                    list.add(Filter(saveString))
                }
            }

            activity?.let {
                (it as MainActivity).filterMassage(list)
            }
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Filter.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}