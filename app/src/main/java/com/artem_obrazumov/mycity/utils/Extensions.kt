package com.artem_obrazumov.mycity.utils

import android.app.Activity
import android.content.Context
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


// App Extension Methods

/**
 * Checks if [FirebaseAuth.getCurrentUser] is not null to ensure user is logged in
 * @return true if user is logged in
 */
fun FirebaseAuth.isLogged() : Boolean = this.currentUser != null

/**
 * Gets trimmed text from EditText view
 * @return formatted text
 */
fun EditText.getTrimmedText() : String = this.text.toString().trim()

/**
 * Gets user's city value from sharedPreferences
 * @return user's city
 */
fun getUserCity(context: Context) : String =
    context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        .getString("cityName", "NonExistingCity") as String

/**
 * OnClick implementation for RecyclerView viewHolders
 */
fun <T : RecyclerView.ViewHolder> T.onClick(event: (position: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition)
    }
    return this
}

/**
 * Custom method to easily change actionBar title in fragment
 */
fun Fragment.setActionBarTitle(title: String) {
    this.activity?.setActionBarTitle(title)
}

/**
 * Custom method to easily change actionBar title in activity
 */
fun Activity.setActionBarTitle(title: String) {
    (this as AppCompatActivity).supportActionBar?.title = title
}

/**
 * Custom method to easily handle backPress in fragment
 */
fun Fragment.initializeBackPress() {
    val navController: NavController = view!!.findNavController()
    val callback: OnBackPressedCallback =
        object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController.popBackStack()
            }
        }
    this.requireActivity().onBackPressedDispatcher.addCallback(this, callback)
}