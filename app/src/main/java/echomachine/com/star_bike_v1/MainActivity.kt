package echomachine.com.star_bike_v1
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.register_fragment,
                R.id.login_fragment,
                R.id.splash_fragment,
                R.id.home_fragment
            ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration!!)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.splash_fragment, R.id.login_fragment, R.id.register_fragment -> {
                    window.decorView.apply {
                        systemUiVisibility =
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
                    }
                    drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toolbar?.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean{
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }
}