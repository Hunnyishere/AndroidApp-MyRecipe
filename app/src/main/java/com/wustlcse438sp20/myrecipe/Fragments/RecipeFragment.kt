package com.wustlcse438sp20.myrecipe.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wustlcse438sp20.myrecipe.Adapter.RecipeAdapter
import com.wustlcse438sp20.myrecipe.Adapter.SimpleItemTouchHelperCallback
import com.wustlcse438sp20.myrecipe.MyApplication
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.RecipeInformationActivity
import com.wustlcse438sp20.myrecipe.ViewModels.RecipeViewModel
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.fragment_recipe.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RecipeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeFragment : Fragment() {

    private var recipeList: ArrayList<RecipeShownFormat> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecipeAdapter
    lateinit var recipeviewModel:RecipeViewModel
    private var user_email:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Calling Application class (see application tag in AndroidManifest.xml)
        var globalVariable = getActivity()?.getApplicationContext() as MyApplication
        user_email = globalVariable.getEmail()!!
        arguments?.let {
        }

//        val activity_intent = activity!!.intent
//        user_email = activity_intent!!.extras!!.getString("user_email")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set the hint for searchView
        recipe_search.queryHint = "Search By Ingredients"
        search_byname.queryHint = "Search By Name"
        //RecyclerView Adapter
        recyclerView = recyclerView_mainpage
        adapter = RecipeAdapter(context,recipeList,"")
        //callback ItemTouch
//        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
//        val touchHelper = ItemTouchHelper(callback)
//        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter = adapter
        adapter.setOnItemClick(object :RecipeAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                val intent  =Intent(context,RecipeInformationActivity::class.java)
                var bundle = Bundle()
                bundle.putInt("recipeId",recipeList[position].id)
                bundle.putString("type","search")
                //bundle.putString("user_email",user_email)
                intent.putExtras(bundle)
                activity?.startActivity(intent)
            }
        })
        //viewmodel
        recipeviewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        //Get random recipe
        recipeviewModel.searchRecipeByRandom()
        //data oberserve
        recipeviewModel.recipeList.observe(this, Observer{
            recipeList.clear()
            for (i in it){
                val recipeShownFormat = RecipeShownFormat(i.id,i.title,i.image)
                recipeList.add(recipeShownFormat)
            }
            Log.v("ingredient获取到的recipe",recipeList.toString())
            adapter.notifyDataSetChanged()
        })
        //recipebyName obeserve
        recipeviewModel.recipeByName.observe(this, Observer {
            recipeList.clear()
            for (i in it.results){
                val recipeShownFormat = RecipeShownFormat(i.id,i.title,"https://spoonacular.com/recipeImages/"+i.image)
                recipeList.add(recipeShownFormat)
            }
            adapter.notifyDataSetChanged()
        })
        //Random data obeserve
        recipeviewModel.recipeRandom.observe(this, Observer {
            recipeList.clear()
            for(i in it.recipes){
                val recipeShownFormat:RecipeShownFormat
                if (i.image ==null)
                    recipeShownFormat = RecipeShownFormat(i.id,i.title,"")
                else
                    recipeShownFormat = RecipeShownFormat(i.id,i.title,i.image)
                recipeList.add(recipeShownFormat)
            }
            Log.v("random获取到的recipe",recipeList.toString())
            adapter.notifyDataSetChanged()
        })
        //searchView
        recipe_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                recipeviewModel.searchRecipeByIngredients(recipe_search.query.toString())
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        search_byname.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                recipeviewModel.searchRecipeByName(search_byname.query.toString())
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

}
