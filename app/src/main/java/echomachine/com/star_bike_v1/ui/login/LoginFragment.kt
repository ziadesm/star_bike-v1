package echomachine.com.star_bike_v1.ui.login
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import echomachine.com.star_bike_v1.Constants
import echomachine.com.star_bike_v1.R
import echomachine.com.star_bike_v1.helpers.DataWorkerHelper
import echomachine.com.star_bike_v1.helpers.NavigationsHelper
import echomachine.com.star_bike_v1.ui.register.RegisterFragment
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    companion object {
        private const val TAG = "ZiadReceiver"
    }
    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        view?.login_layout_btn_login?.setOnClickListener { signInUser() }
        return view
    }

    private fun signInUser() {
        val email = view?.login_layout_et_email_login?.text.toString()
        val password = view?.login_layout_et_password_login?.text.toString()

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.error_dialog_design)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
            Handler().postDelayed({
                if (task.isSuccessful) {
                    DataWorkerHelper.getDataByWorker(currentUser.uid, requireContext())
                    Log.d(TAG, "signInUser: ${currentUser.uid}")
                    dialog.dismiss()
                    NavigationsHelper.navigateToThisDestination(R.id.register_fragment, requireActivity())
                } else {
                    dialog.dismiss()
                    view?.login_layout_btn_login?.error = "Check your email and password"
                }
            }, 1500)
        }
    }
}