package pe.idat.proyecto.pizzeria.fragments.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.adapters.CategoriesAdapter
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.CategoriesProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientCategoriesFragment : Fragment() {

    val TAG = "CategoriesFragment"
    var myView: View? = null
    /*lista categorias*/
    var recyclerViewCategories: RecyclerView? = null
    var adapter: CategoriesAdapter? = null
    var categoriesProvider: CategoriesProvider? = null
    var user: User? = null
    var sharedPref: SharedPref? = null
    var categories = ArrayList<Category>()
    var toolBar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)

        toolBar = myView?.findViewById(R.id.toolbar)
        toolBar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        toolBar?.title = "Categorias"
        (activity as AppCompatActivity).setSupportActionBar(toolBar)

        recyclerViewCategories = myView?.findViewById(R.id.recycletview_categories)
        recyclerViewCategories?.layoutManager = LinearLayoutManager(requireContext())/*especifica que los elementos se mostraran de forma vertical*/
        sharedPref = SharedPref(requireActivity())
        getUserFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        getCategories()
        return myView
    }

    private fun getCategories() {
        categoriesProvider?.getAll()?.enqueue(object :Callback<ArrayList<Category>>{
            override fun onResponse(call: Call<ArrayList<Category>>, response: Response<ArrayList<Category>>
            ) {
                if (response.body() != null) {
                    categories = response.body()!!
                    adapter = CategoriesAdapter(requireActivity(), categories)
                    recyclerViewCategories?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error:  ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getUserFromSession() {
        val gson = Gson()
        /*si el usuario existe en la sesion*/
        if (!sharedPref?.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/
        }
    }
}