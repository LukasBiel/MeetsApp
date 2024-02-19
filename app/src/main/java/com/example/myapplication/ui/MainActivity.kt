package com.example.myapplication.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.repository.PostRepository
import com.example.myapplication.repository.UserRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth





class MainViewModel : ViewModel() {

    private val _fabVisibility = MutableLiveData<Boolean>()
    val fabVisibility: LiveData<Boolean> = _fabVisibility

    init {
        // Set initial visibility state
        _fabVisibility.value = false
    }

    fun setFabVisibility(isVisible: Boolean) {
        _fabVisibility.value = isVisible
    }
}

class MainActivity<FirebaseUser> : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var FabVisibity: MainViewModel
    private lateinit var auth: FirebaseAuth

    private val CHANNEL_ID = "my_channel_id"
    private val NOTIFICATION_ID = 101


    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }

    }



    private fun reload() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.action_zeroFragment_to_FirstFragment)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        createNotificationChannel()
        val title = "Nowe powiadomienie"
        val message = "Witaj! To jest przykładowe powiadomienie."
        sendNotification(title, message)



        FirebaseApp.initializeApp(this);
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Obsługa kliknięcia na element "Home"
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.action_FirstFragment_to_SecondFragment)

                    true
                }
                R.id.navigation_notifications -> {
                    // Obsługa kliknięcia na element "Notifications"
                    true
                }
                else -> false
            }
        }



    }

    private fun createNotificationChannel() {
        val name = "My Channel"
        val descriptionText = "This is my channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(title: String, message: String) {


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {

            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Brak uprawnień, nie można wysłać powiadomienia
                return@with
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                val navController = findNavController(R.id.nav_host_fragment_content_main)

                if (!navController.popBackStack()) {
                    // If the back stack is empty, perform custom back navigation
                    finish()
                }// Handle the up button press
                true
            }

            R.id.action_settings -> true
            R.id.action_Wyloguj -> {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            R.id.action_Usuń -> {
                val user = Firebase.auth.currentUser

                if (user != null) {
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful()) {
                            // User account deleted successfully
                            Log.d(TAG, "User account deleted.")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // An error occurred while deleting the user account
                            Log.e(TAG, "Error deleting user account", task.getException())
                        }
                    }
                }
                true
            }

            else -> {
                true
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val currentDestination = navController.currentDestination?.id
        return when(currentDestination){
            R.id.zeroFragment->{
                finish()
                true
            }

            R.id.FirstFragment->{
                moveTaskToBack(true)
                true
            }else->
                super.onSupportNavigateUp()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    internal fun createAccount(
        email: String,
        password: String,
        imie: String,
        nazwisko: String,
        wiek: Int,
        plec: String
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI2(user, imie, nazwisko, wiek, plec)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Nieprawidłowy email",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI2(null, imie, nazwisko, wiek, plec)
                }
            }
        // [END create_user_with_email]
    }

    internal fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Nieprawidłowy email lub hasło",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun updateUI(user: com.google.firebase.auth.FirebaseUser?) {
        if (user != null) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.action_zeroFragment_to_FirstFragment)
        } else {

        }
    }

    private fun updateUI2(
        user: com.google.firebase.auth.FirebaseUser?,
        imie: String,
        nazwisko: String,
        wiek: Int,
        plec: String
    ) {
        if (user != null) {
            val userId: String = user.uid
            val userRepository = UserRepository()
            userRepository.addUser(userId, imie, nazwisko, wiek, plec)

            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.action_registerationFragment_to_FirstFragment)
        } else {

        }
    }
}
