package pe.idat.proyecto.pizzeria.models

import com.google.gson.Gson

class Category(
    val id:String? = null,
    val name:String,
    val image:String? = null,
) {


    /*transformar el usuario a json*/
    fun toJson():String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return name
    }
}