package com.kuliah.pkm.tajwidify.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kuliah.pkm.tajwidify.data.User
import com.kuliah.pkm.tajwidify.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
): ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val user = _user.asStateFlow()

    private val _passwordUpdateStatus = MutableLiveData<Resource<Boolean>>()
    val passwordUpdateStatus: LiveData<Resource<Boolean>> = _passwordUpdateStatus

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch { _user.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _user.emit(Resource.Error(error.message.toString()))
                    }
                } else {
                    val user = value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch {
                            _user.emit(Resource.Success(user))
                        }
                    }
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun uploadProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            _user.emit(Resource.Loading())
            val userId = auth.uid ?: return@launch
            val storageRef = storage.reference.child("profile_images/$userId.jpg")
            try {
                val uploadTaskSnapshot = storageRef.putFile(imageUri).await()
                val downloadUrl = uploadTaskSnapshot.storage.downloadUrl.await().toString()
                updateProfileImageUrl(downloadUrl)
                _user.emit(Resource.Success(null))
            } catch (e: Exception) {
                Log.e("UploadFile", e.message.toString())
                _user.emit(Resource.Error(e.message))
            }
        }
    }

    private fun updateProfileImageUrl(imageUrl: String) {
        val userId = auth.uid ?: return
        firestore.collection("user").document(userId)
            .update("imagePath", imageUrl)
            .addOnSuccessListener {
                getUser()
            }
            .addOnFailureListener {
                Log.e("UpdateProfile", it.message.toString())
            }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser
        user?.let {
            val credential = EmailAuthProvider.getCredential(it.email!!, oldPassword)
            reauthenticateAndChangePassword(it, credential, newPassword)
        }
    }

    private fun reauthenticateAndChangePassword(user: FirebaseUser, credential: AuthCredential, newPassword: String) {
        viewModelScope.launch {
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    _passwordUpdateStatus.postValue(Resource.Success(true))
                                } else {
                                    _passwordUpdateStatus.postValue(Resource.Error("Password update failed: ${passwordUpdateTask.exception?.message}"))
                                }
                            }
                    } else {
                        _passwordUpdateStatus.postValue(Resource.Error("Re-authentication failed: ${task.exception?.message}"))
                    }
                }
        }
    }
}