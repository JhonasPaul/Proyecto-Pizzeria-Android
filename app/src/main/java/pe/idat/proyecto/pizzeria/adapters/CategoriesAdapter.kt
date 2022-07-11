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
import pe.idat.proyecto.pizzeria.models.Rol
import pe.idat.proyecto.pizzeria.utils.SharedPref

class CategoriesAdapter(val context:Activity, val categories:ArrayList<Category>) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){

    val sharedPref = SharedPref(context)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)/*instanciamos la vista*/
       return CategoriesViewHolder(view)
   }

    /*retorna la cantidad de roles*/
    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]/*devuelve cada una de las categorias*/
        holder.textViewCategory.text = category.name
        /*mostrar iamgen por url*/                  /*donde se mostrara la imagen*/
        Glide.with(context).load(category.image).into(holder.imageViewCategory)

//        holder.itemView.setOnClickListener { goToRol(rol) }
    }

//    private fun goToRol(rol: Rol) {
//        val i = Intent(context, RestaurantHomeActivity ::class.java)
//        context.startActivity(i)
//    }

    class CategoriesViewHolder(view:View):RecyclerView.ViewHolder(view){
        /*isntanciar las vistas image y textview*/
        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }
    }
}