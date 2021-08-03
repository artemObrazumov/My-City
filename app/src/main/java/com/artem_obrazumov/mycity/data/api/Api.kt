package com.artem_obrazumov.mycity.data.api;

import com.artem_obrazumov.mycity.data.models.PlaceModel
import com.artem_obrazumov.mycity.data.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class Api {
    companion object: ApiService {
        private val database = FirebaseDatabase.getInstance()
        private val auth = FirebaseAuth.getInstance()

        override fun getPopularCritics(cityName: String): Flow<MutableList<UserModel?>> {
            val reference = database.getReference("Users")
            val query: Query = reference.orderByChild("cityName").equalTo(cityName)
            val critics: MutableList<UserModel?> = ArrayList()

            return callbackFlow {
                val listener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot: DataSnapshot in dataSnapshot.children) {
                            val user: UserModel? = snapshot.getValue(UserModel::class.java)
                            critics.add(user)
                        }
                        offer(critics)
                        close()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
                query.addListenerForSingleValueEvent(listener)

                awaitClose {}
            }
        }

        override fun getPopularPlaces(cityName: String): Flow<MutableList<PlaceModel?>> {
            val reference = database.getReference("Places")
            val query: Query = reference.orderByChild("cityName").equalTo(cityName)
            val places: MutableList<PlaceModel?> = ArrayList()

            return callbackFlow {
                val listener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot: DataSnapshot in dataSnapshot.children) {
                            val place: PlaceModel? = snapshot.getValue(PlaceModel::class.java)
                            places.add(place)
                        }
                        offer(places)
                        close()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
                query.addListenerForSingleValueEvent(listener)

                awaitClose {}
            }
        }

        override fun getUserData(userId: String) : Flow<UserModel?> {
            val reference = database.getReference("Users/${userId}")

            return callbackFlow {
                val listener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(UserModel::class.java)
                        offer(user)
                        close()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
                reference.addListenerForSingleValueEvent(listener)

                awaitClose{}
            }
        }


        override fun saveUserdataToDatabase(user: UserModel) {
            val reference = database.getReference("Users/${user.authId}")
            reference.setValue(user)
        }


        override fun registerUser(
            email: String,
            password: String
        ) : Flow<Task<AuthResult>> {
            return callbackFlow {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        offer(task)
                        close()
                    }

                awaitClose{}
            }
        }

        override fun authorizeUser(
            email: String,
            password: String
        ) : Flow<Task<AuthResult>> {
            return callbackFlow {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        offer(task)
                        close()
                    }

                awaitClose{}
            }
        }
    }
}
