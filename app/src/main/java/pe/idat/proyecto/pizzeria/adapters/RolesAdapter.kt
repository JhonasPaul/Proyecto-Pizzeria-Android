package pe.idat.proyecto.pizzeria.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.models.Rol

class RolesAdapter(val context:Activity, val roles:ArrayList<Rol>) : RecyclerView.Adapter<RolesAdapter.RolesViewHolder>(){

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)/*instanciamos la vista*/
       return RolesViewHolder(view)
   }

    /*retorna la cantidad de roles*/
    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]/*devuelve cada rol*/
        holder.textViewRol.text = rol.name
        /*mostrar iamgen por url*/                  /*donde se mostrara la imagen*/
        Glide.with(context).load(rol.image).into(holder.imageViewRol)
    }

    class RolesViewHolder(view:View):RecyclerView.ViewHolder(view){
        /*isntanciar las vistas image y textview*/
        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.textview_rol)
            imageViewRol = view.findViewById(R.id.imageview_rol)
        }
    }
}