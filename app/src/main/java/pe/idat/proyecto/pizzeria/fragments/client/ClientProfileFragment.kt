package pe.idat.proyecto.pizzeria.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import pe.idat.proyecto.pizzeria.R
import pe.idat.proyecto.pizzeria.activities.MainActivity
import pe.idat.proyecto.pizzeria.activities.SelectRolesActivity
import pe.idat.proyecto.pizzeria.activities.client.update.ClientUpdateActivity
import pe.idat.proyecto.pizzeria.models.User
import pe.idat.proyecto.pizzeria.utils.SharedPref


class ClientProfileFragment : Fragment() {

    var myView: View? = null
    var buttonSelectRol: Button? = null
    var buttonUpdateProfile: Button? = null
    var circleImageView:CircleImageView? = null
    var textViewName: TextView? = null
    var textViewEmail: TextView? = null
    var textViewPhone: TextView? = null
    var imageViewLogout: ImageView? = null

    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*primero se accede a la vista myView*/
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        buttonSelectRol = myView?.findViewById(R.id.btn_select_rol)
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        textViewName = myView?.findViewById(R.id.textview_name)
        textViewEmail = myView?.findViewById(R.id.textview_email)
        textViewPhone = myView?.findViewById(R.id.textview_phone)
        circleImageView= myView?.findViewById(R.id.circleimage_user)
        imageViewLogout= myView?.findViewById(R.id.imageview_logout)

        buttonSelectRol?.setOnClickListener{goToSelectRoles()}
        imageViewLogout?.setOnClickListener{logout()}
        buttonUpdateProfile?.setOnClickListener{goToUpdate()}

        getUserFromSession()

        textViewName?.text = "${user?.name} ${user?.lastname}"
        textViewEmail?.text = user?.email
        textViewPhone?.text = user?.phone
        /*para mostrar la imagen*/
        if (!user?.image.isNullOrBlank()) {

            Glide.with(requireContext()).load(user?.image).into(circleImageView!!)
        }
        return myView
    }

    private fun logout() {
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()

        /*si el usuario existe en la sesion*/
        if (!sharedPref?.getData("user").isNullOrBlank()){
            /*obtener la informacion*/
             user = gson.fromJson(sharedPref?.getData("user"), User::class.java)/*convertir la informacion a tipo usuario*/
        }
    }

    private fun goToUpdate() {
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)
    }

    private fun goToSelectRoles() {
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(i)
    }

}