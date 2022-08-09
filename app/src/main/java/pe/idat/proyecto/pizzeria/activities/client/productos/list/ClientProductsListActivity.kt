package pe.idat.proyecto.pizzeria.activities.client.productos.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.adapters.ProductsAdapter
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.Product
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.providers.ProductsProvider
import pe.idat.proyecto.pizzeria.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*10 listar producto por categoria:*/
class ClientProductsListActivity : AppCompatActivity() {
    var TAG = "ClientProducts"
    lateinit var reciclerViewProducts: RecyclerView
    lateinit var adapter: ProductsAdapter
    lateinit var user: User
    lateinit var productoProvider: ProductsProvider

    lateinit var idCategory: String

    var products: ArrayList<Product> = ArrayList()

    /*sesion*/
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)

        sharedPref = SharedPref(this)

        /*guarda en idCategory el id de la categoria desde el intent categoria(vista de categorias) */
        idCategory = intent.getStringExtra("idCategory").toString()

        /*sesion*/
        getUserFromSession()
        productoProvider = ProductsProvider(user.sessionToken!!)

        reciclerViewProducts = findViewById(R.id.recycletview_products)
        /*establece como se mostraran las imagenes de los productos*/
        reciclerViewProducts.layoutManager = GridLayoutManager(this, 2)

        getProducts()
    }

    /*reciclar de la clase ClientCategoriesFragment*/
    private fun getUserFromSession() {
        val gson = Gson()
        /*si el usuario existe en la sesion*/
        if (!sharedPref?.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/
        }
    }

    private fun getProducts() {
        /*traer informacion del backend*/
        productoProvider.findByCategory(idCategory)?.enqueue(object :Callback<ArrayList<Product>>{
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if (response.body() != null) {
                    products = response.body()!!
                    adapter = ProductsAdapter(this@ClientProductsListActivity, products)
                    reciclerViewProducts.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error: ${t.message}")
            }

        })
    }
}