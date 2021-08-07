package com.artem_obrazumov.mycity.data.api

import android.annotation.SuppressLint
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionsScript
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
@SuppressLint("StaticFieldLeak")
class Api {
    companion object: ApiService {
        private val database = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        override suspend fun getPopularCriticsWithLimit(cityName: String): MutableList<User> {
            val critics: MutableList<User> = ArrayList()
            try {
                val reference = database.collection("Users")
                val query = reference.whereEqualTo("cityName", cityName)
                    .orderBy("rating")
                    .limit(3)
                val dataSnapshot = query.get().await()
                for (document in dataSnapshot.documents) {
                    val critic = document.toObject(User::class.java)!!
                    critics.add(critic)
                }
            } catch (e: Exception) {
                critics.clear()
            }
            critics.reverse()
            return critics
        }

        override suspend fun getPopularCritics(cityName: String): MutableList<User> {
            val critics: MutableList<User> = ArrayList()
            try {
                val reference = database.collection("Users")
                val query = reference.whereEqualTo("cityName", cityName)
                    .orderBy("rating")
                val dataSnapshot = query.get().await()
                for (document in dataSnapshot.documents) {
                    val critic = document.toObject(User::class.java)!!
                    critics.add(critic)
                }
            } catch (e: Exception) {
                critics.clear()
            }
            critics.reverse()
            return critics
        }

        override suspend fun getPopularPlaces(cityName: String): MutableList<Place> {
            val places: MutableList<Place> = ArrayList()
            try {
                val reference = database.collection("Places")
                val query = reference.whereEqualTo("cityName", cityName)
                val dataSnapshot = query.get().await()
                for (document in dataSnapshot.documents) {
                    val place = document.toObject(Place::class.java)!!
                    places.add(place)
                }
            } catch (e: Exception) {
                places.clear()
            }
            return places
        }

        override suspend fun getUserData(userId: String): User =
            database.document("Users/${userId}").get().await()
                .toObject(User::class.java)!!

        override suspend fun getCitiesList() : MutableList<String> {
            val cities: MutableList<String> = ArrayList()
            try {
                val reference = database.collection("Cities")
                val dataSnapshot = reference.get().await()
                for (document in dataSnapshot.documents) {
                    cities.add(document.id)
                }
            } catch (e: Exception) {
                cities.clear()
            }
            return cities
        }

        override suspend fun getPlaceData(placeId: String): Place =
            database.document("Places/${placeId}").get().await()
                .toObject(Place::class.java)!!

        override suspend fun getReviews(placeId: String, loadAuthorData: Boolean): MutableList<Review> {
            val reviews: MutableList<Review> = ArrayList()
            try {
                val reviewsSnapshot = database.collection("Place_reviews")
                    .whereEqualTo("placeId", placeId)
                    .get().await()
                for (document in reviewsSnapshot.documents) {
                    val review = document.toObject(Review::class.java)!!
                    if (loadAuthorData) {
                        review.author = getUserData(review.authorId)
                    }
                    reviews.add(review)
                }
            } catch (e: Exception) {
                reviews.clear()
            }

            return reviews
        }

        override suspend fun getInstructionScript(): InstructionsScript =
            try {
                database.document("Instructions_script").get().await()
                    .toObject(InstructionsScript::class.java)!!
            } catch (e: Exception) {
                Defaults.defaultInstruction
            }


        override suspend fun saveUserdataToDatabase(user: User) {
            val reference = database.document("Users/${user.authId}")
            reference.set(user)
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
