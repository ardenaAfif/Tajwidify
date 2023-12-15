package com.kuliah.pkm.tajwidify.utils

sealed class RegisterValidation() {
    object Success: RegisterValidation()

    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldState(
    val empty: RegisterValidation,
    val firstName: RegisterValidation,
    val lastName: RegisterValidation,
    val email: RegisterValidation,
    val password: RegisterValidation,
)