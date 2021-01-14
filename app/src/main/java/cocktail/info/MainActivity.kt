package cocktail.info

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cocktail.info.adapter.CocktailAdapter
import cocktail.info.data.Filter
import cocktail.info.response.FrResponse
import cocktail.info.response.CocResponse
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.lastIndex
import kotlin.collections.mutableListOf
import kotlin.collections.set


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: CocktailAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var score = 0
    private var masPage = mutableListOf<String>()
    private var listResponseFiltr = ArrayList<Filter>()
    private var totalpage = 0
    private var isLoading = false
    private var isFirstLaunch = true
    private var strLabel = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFilters()

        layoutManager = LinearLayoutManager(this)
        var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout =findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener(this)
        setupRecyclerView()

        Thread(Runnable { Thread.sleep(1100)
            getCocktails(false) }).start()

        var rvUsers:androidx.recyclerview.widget.RecyclerView =findViewById(R.id.rvUsers)
        rvUsers.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibilityItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (!isLoading && score!=totalpage){
                    if (visibilityItemCount+pastVisibleItem >= total){
                        score++
                        getCocktails(false)
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        val nameOfActivity: TextView = findViewById(R.id.name_of_activity)
        val filterIco:ImageView = findViewById(R.id.filter_ico)
        val leftBack:ImageView = findViewById(R.id.left_back)

        filterIco.setOnClickListener{
            filterIco.visibility = View.GONE
            nameOfActivity.text = getString(R.string.filters)
            leftBack.visibility = View.VISIBLE
                val filterFragment:Fragment= FilterFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, filterFragment).commit()
        }

        leftBack.setOnClickListener{
            filterIco.visibility = View.VISIBLE
            nameOfActivity.text = getString(R.string.drinks)
            leftBack.visibility = View.GONE
            for (Filter_Fragment in supportFragmentManager.fragments) {
                supportFragmentManager.beginTransaction().remove(Filter_Fragment!!).commit()
            }
        }

    }

    fun sendParams(): ArrayList<Filter> {
        return listResponseFiltr
    }

    private fun getFilters() {
        if (isFirstLaunch){
            val parameters = HashMap<String,String>()
            parameters["c"]="list"

            RetrofitClient.INSTANCE_FILTER.getFilter(parameters).enqueue(object :retrofit2.Callback<FrResponse>{
                override fun onFailure(call: Call<FrResponse>, t: Throwable) {
                    println(t)
                }

                override fun onResponse(
                        call: Call<FrResponse>,
                        response: retrofit2.Response<FrResponse>
                ) {
                    totalpage = masPage.lastIndex
                    listResponseFiltr = response.body()?.filters!!

                    var h = 0
                    for (s1 in listResponseFiltr){
                        val s = s1.filterName
                        println(s)
                        masPage.add(s)
                            val sharedPreferences = getSharedPreferences("shared",
                                Context.MODE_PRIVATE)
                            val editop = sharedPreferences.edit()
                            editop.apply{
                                putString("check$h",s)
                                putInt("lengthCheck", h)
                                h++
                            }.apply()
                        h++
                    }
                }
            })
            isFirstLaunch = false
        }
    }


    fun filterMassage(listFilt: ArrayList<Filter>) {
        masPage.clear()
        for (s1 in listFilt){
            val s = s1.filterName
            println(s)
            masPage.add(s)
        }
        onRefresh()
    }

    private fun getCocktails(isOnRefresh: Boolean) {

        val progressBar:ProgressBar = findViewById(R.id.progressBar)
        isLoading = true
        if (!isOnRefresh)progressBar.visibility=View.VISIBLE
        val parameters = HashMap<String,String>()
        parameters["c"]=masPage[score]

        RetrofitClient.INSTANCE_COCKTAIL.getCocktail(parameters).enqueue(object :retrofit2.Callback<CocResponse>{
            override fun onFailure(call: Call<CocResponse>, t: Throwable) {
                println(t)
            }

            override fun onResponse(
                    call: Call<CocResponse>,
                    response: retrofit2.Response<CocResponse>
            ) {
                totalpage = masPage.lastIndex

                var listResponse = response.body()?.drinks
                strLabel=masPage[score]
                if (listResponse != null){
                    val id = listResponse[0]
                    val count = id.id
                    adapter.addList(listResponse,strLabel,count)
                }
                if(score == totalpage){
                    progressBar.visibility = View.GONE
                }else{
                    progressBar.visibility = View.INVISIBLE
                }
                isLoading =false
                var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout =findViewById(R.id.swipeRefresh)
                swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun setupRecyclerView() {
        var rvUsers:androidx.recyclerview.widget.RecyclerView =findViewById(R.id.rvUsers)
        rvUsers.setHasFixedSize(true)
        rvUsers.layoutManager = layoutManager
        adapter = CocktailAdapter()
        rvUsers.adapter=adapter
    }

    override fun onRefresh() {
        adapter.clear()
        score = 0;
        if (masPage.isNotEmpty()){
            getCocktails(true)
        }else{
            masPage.add("null")
            getCocktails(true)
        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}

