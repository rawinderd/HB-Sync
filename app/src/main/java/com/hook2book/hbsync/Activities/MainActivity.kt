package com.hook2book.hbsync.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.hook2book.hbsync.Activities.SellerDataForm
import com.hook2book.hbsync.EnumClasses.ApiStatus
import com.hook2book.hbsync.R
import com.hook2book.hbsync.UtilityClass.BaseActivity
import com.hook2book.hbsync.UtilityClass.Preferences
import com.hook2book.hbsync.UtilityClass.Prevalent
import com.hook2book.hbsync.ViewModels.MainViewModel
import com.hook2book.hbsync.databinding.ActivityMainBinding
import com.hook2book.hbsync.fragments.CouponFragment
import com.hook2book.hbsync.fragments.HomeFragment
import com.hook2book.hbsync.fragments.OrderFragment
import com.hook2book.hbsync.fragments.ProductsFragment
import com.hook2book.hbsync.fragments.UserFragment

import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomBar: BottomNavigationView
    private var mainViewModel: MainViewModel = MainViewModel()
    private lateinit var toolbar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar_simple)
        setToolbar(toolbar, false, "Home", true)
        bottomBar = findViewById(R.id.bottomNav)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        bottomBar.menu.getItem(4).setOnMenuItemClickListener {
            Toasti("User Clicked")
            true
        }


        /* bottomBar.setOnLongClickListener {
            when (it.id) {
                R.id.user -> {
                    Toasti("User long clicked")
                }
                R.id.coupon -> {
                    Toasti("coupon long clicked")
                }
            }
            true
        }
*//* when(it.id)
                {
                    R.id.user -> {
                        Toasti("User long clicked")
                    }
                    R.id.coupon -> {
                        Toasti("coupon long clicked")
                    }
                }
                *//*Toasti("Long Clicked")
               // val intent = Intent(this, PublisherOrders::class.java)
                //startActivity(intent)*//*
                true

        )*/

        bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.products -> {
                    loadFragment(ProductsFragment())
                    true
                }

                R.id.coupon -> {
                    loadFragment(CouponFragment())
                    true
                }

                R.id.order -> {
                    loadFragment(OrderFragment())
                    true
                }

                R.id.user -> {
                    loadFragment(UserFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
        mainViewModel.fetchUserData(Paper.book().read<String>(Prevalent.PubId).toString())
        mainViewModel.getUserData().observe(this) {
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    bottomBar.menu.getItem(4).setTitle(it.data?.data_outer?.get(0)?.name)
                    Preferences.saveAccountInfo(this, it.data)
                }
                ApiStatus.ERROR -> {
                    if (it.message == "Record not found") {
                        Toasti("Please Enter Account Information")
                        val intent = Intent(this, SellerDataForm::class.java)
                        startActivity(intent)
                    }
                    //Toasti("Error in Fetching Data")
                }
                ApiStatus.LOADING -> TODO()
            }

        }
        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        if (navigationView != null) {
            navigationView.bringToFront()
            navigationView.requestLayout()
            navigationView.setNavigationItemSelectedListener(this)
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_punjabi -> Toast.makeText(this, "Punjabi", Toast.LENGTH_LONG).show()
            R.id.nav_add_product_to_publishers -> {
                val sellerData = Preferences.loadAccountInfo(this)
                Toasti(sellerData.data_outer.get(0).name)
            }
            R.id.nav_account -> Toast.makeText(this, "Account", Toast.LENGTH_LONG).show()
            R.id.nav_address_book -> Toast.makeText(this, "Address Book", Toast.LENGTH_LONG).show()
            R.id.nav_logout -> {

                Paper.book().destroy()
                Preferences.saveLoginStatus(this, "1")
                val intent: Intent = Intent(
                    this, RegisterActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}