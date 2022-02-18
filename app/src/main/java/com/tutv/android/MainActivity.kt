package com.tutv.android

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemReselectedListener
import com.tutv.android.ui.login.LoginActivity
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = findViewById<BottomNavigationView?>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
        navView.setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener { menuView: MenuItem? -> })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        val searchItem = menu.findItem(R.id.search_button)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        supportActionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.actionAndStatusBar)))
        return true
    }

    fun menuLoginButton_clicked(item: MenuItem?) {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}