package com.artem_obrazumov.mycity.ui.activities.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.models.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class AuthorizationViewModel(): ViewModel() {

    fun authorizeUser(auth: FirebaseAuth, email: String, password: String,
        listener: OnCompleteListener<AuthResult>) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(listener)
        }
    }
}