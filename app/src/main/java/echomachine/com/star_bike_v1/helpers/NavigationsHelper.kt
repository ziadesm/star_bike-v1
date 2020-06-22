package echomachine.com.star_bike_v1.helpers

import android.app.Activity
import androidx.navigation.Navigation
import echomachine.com.star_bike_v1.R

class NavigationsHelper {
    companion object {
        fun navigateToThisDestination(id: Int, activity: Activity) {
            val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
            navController.navigate(id)
        }
    }
}