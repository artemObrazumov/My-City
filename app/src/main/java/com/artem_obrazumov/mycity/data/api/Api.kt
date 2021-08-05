package com.artem_obrazumov.mycity.data.api

import com.artem_obrazumov.mycity.data.models.PlaceModel
import com.artem_obrazumov.mycity.data.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class Api {
    companion object: ApiService {
        private val database = FirebaseDatabase.getInstance()
        private val auth = FirebaseAuth.getInstance()

        override suspend fun getPopularCritics(cityName: String): MutableList<UserModel> {
            val critics: MutableList<UserModel> = ArrayList()
            try {
                val reference = database.getReference("Users")
                val query: Query = reference.orderByChild("cityName").equalTo(cityName)
                val dataSnapshot = query.get().await()
                for (snapshot in dataSnapshot.children) {
                    val critic = snapshot.getValue(UserModel::class.java)!!
                    critics.add(critic)
                }
            } catch (e: Exception) {
                critics.clear()
            }
            return critics
        }

        override suspend fun getPopularPlaces(cityName: String): MutableList<PlaceModel> {
            val places: MutableList<PlaceModel> = ArrayList()
            try {
                val reference = database.getReference("Places")
                val query = reference.orderByChild("cityName").equalTo(cityName)
                val dataSnapshot = query.get().await()
                for (snapshot in dataSnapshot.children) {
                    val place = snapshot.getValue(PlaceModel::class.java)!!
                    places.add(place)
                }
            } catch (e: Exception) {
                places.clear()
            }
            return places
        }

        override suspend fun getUserData(userId: String): UserModel =
            database.getReference("Users/${userId}").get().await()
                .getValue(UserModel::class.java)!!

        override suspend fun getCitiesList() : MutableList<String> {
            val cities: MutableList<String> = ArrayList()
            try {
                val reference = FirebaseDatabase.getInstance().getReference("Cities")
                val dataSnapshot = reference.get().await()
                for (snapshot in dataSnapshot.children) {
                    cities.add(snapshot.key!!)
                }
            } catch (e: Exception) {
                cities.clear()
            }
            return cities
        }


        override suspend fun saveUserdataToDatabase(user: UserModel) {
            val reference = database.getReference("Users/${user.authId}")
            reference.setValue(user)
        }


        override suspend fun registerUser(
            email: String,
            password: String
        ) : Task<AuthResult> = suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> continuation.resume(task) }
        }

        override suspend fun authorizeUser(
            email: String,
            password: String
        ) : Task<AuthResult> = suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> continuation.resume(task) }
        }
    }
}
