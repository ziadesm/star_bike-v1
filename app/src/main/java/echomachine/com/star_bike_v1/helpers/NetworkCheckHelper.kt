package echomachine.com.star_bike_v1.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkCheckHelper {
    companion object {
        fun isOnline(context: Context): Boolean {
            val cm= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
    }
}