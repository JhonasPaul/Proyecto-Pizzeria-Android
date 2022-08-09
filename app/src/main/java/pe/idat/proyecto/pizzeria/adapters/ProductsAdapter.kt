package pe.idat.proyecto.pizzeria.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.client.home.ClientHomeActivity
import pe.idat.proyecto.pizzeria.activities.delivery.home.DeliveryHomeActivity
import pe.idat.proyecto.pizzeria.activities.restaurante.home.RestaurantHomeActivity
import pe.idat.proyecto.pizzeria.models.Category
import pe.idat.proyecto.pizzeria.models.Product
import pe.idat.proyecto.pizzeria.models.Rol
import pe.idat.proyecto.pizzeria.utils.SharedPref

/*2 listar produtos por categoria: parametro producto de tipo lista*/
class ProductsAdapter(val context:Activity, val products:ArrayList<Product>) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>(){

    val sharedPref = SharedPref(context)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)/*instanciamos la vista*/
       return ProductsViewHolder(view)
   }

    /*retorna la cantidad de roles*/
    override fun getItemCount(): Int {
        return products.size
    }

    /* 4 MODIFICAR TODO LO NECESARIO. ir a  ProductsRoutes*/
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position]/*devuelve la posicion de products*/
        holder.textViewName.text = product.name
        holder.textViewPrice.text = "${product.price}$"
        /*mostrar iamgen por url*/                  /*donde se mostrara la imagen*/
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

//        holder.itemView.setOnClickListener { goToRol(rol) }
    }

//    private fun goToRol(rol: Rol) {
//        val i = Intent(context, RestaurantHomeActivity ::class.java)
//        context.startActivity(i)
//    }

    class ProductsViewHolder(view:View):RecyclerView.ViewHolder(view){
        /*3 listar produtos: instanciar las vistas image y textview que estan en el cardviewproduct*/
        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName = view.findViewById(R.id.textview_product_name)
            textViewPrice = view.findViewById(R.id.textview_product_price)
            imageViewProduct = view.findViewById(R.id.imageview_product)
        }
    }
}