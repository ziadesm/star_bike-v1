package echomachine.com.star_bike_v1

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        val navControler = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(navControler)
        navControler.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.splash_fragment, R.id.login_fragment, R.id.register_fragment -> {
                    window.decorView.apply {
                        // Hide both the navigation bar and the status bar.
                        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
                        // a general rule, you should design your app to hide the status bar whenever you
                        // hide the navigation bar.
                        systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                    }
                    supportActionBar?.hide()
                }
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}