package echomachine.com.star_bike_v1.ui.register
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import echomachine.com.star_bike_v1.R
import echomachine.com.star_bike_v1.helpers.NavigationsHelper
import echomachine.com.star_bike_v1.helpers.NetworkCheckHelper
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.register_layout_btn_register.setOnClickListener{registerTheUser()}
        return view
    }

    private fun registerTheUser() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.error_dialog_design)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        if (context?.let { !NetworkCheckHelper.isOnline(it) }!!) {
            dialog.show()
            return
        }

        val name = view?.register_layout_et_name?.text.toString()
        val email = view?.register_layout_et_email?.text.toString()
        val password = view?.register_layout_et_password?.text.toString()
        val phone = view?.register_layout_et_phone_number?.text.toString()
        val location = view?.register_layout_et_location?.text.toString()

        if (TextUtils.isEmpty(name)) {
            view?.register_layout_et_name?.error = "Please we need your name"
            view?.register_layout_et_name?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            view?.register_layout_et_email?.error = "Please fill right email address"
            view?.register_layout_et_email?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            view?.register_layout_et_email?.error = "Please fill right email address"
            view?.register_layout_et_email?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(phone)) {
            view?.register_layout_et_phone_number?.error = "Please enter your phone to reach you"
            view?.register_layout_et_phone_number?.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password) || password.length < 6) {
            view?.register_layout_et_password?.error = "Empty or less than 6 characters"
            view?.register_layout_et_password?.requestFocus()
            return
        }
        dialog.show()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) {task ->
            Handler().postDelayed({
                if (task.isSuccessful) {
                    dialog.dismiss()
                    NavigationsHelper.navigateToThisDestination(R.id.login_fragment, requireActivity())
                }
            }, 1500)
        }
    }
}