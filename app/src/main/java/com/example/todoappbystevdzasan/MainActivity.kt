package com.example.todoappbystevdzasan

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {


    private lateinit var myFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*if(savedInstanceState != null){
            myFragment = supportFragmentManager.getFragment(savedInstanceState, "myFragment")!!
        }*/

        setupActionBarWithNavController(findNavController(R.id.navHostFragment))
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        /*supportFragmentManager.putFragment(outState, "myFragment", myFragment)*/
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}