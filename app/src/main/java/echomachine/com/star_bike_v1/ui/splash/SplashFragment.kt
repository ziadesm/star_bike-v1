package echomachine.com.star_bike_v1.ui.splash
import android.os.Handler
import androidx.fragment.app.Fragment
import echomachine.com.star_bike_v1.R
import echomachine.com.star_bike_v1.helpers.NavigationsHelper

class SplashFragment : Fragment(R.layout.fragment_splash) {
    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            activity?.let { NavigationsHelper.navigateToThisDestination(R.id.register_fragment, it) }
        },4000)
    }
}