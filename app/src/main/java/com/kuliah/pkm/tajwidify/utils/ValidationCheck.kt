package com.kuliah.pkm.tajwidify.utils

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email tidak boleh kosong")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Format email salah!")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Harap isi password dahulu")
    if (password.length < 6)
        return RegisterValidation.Failed("Password minimal 6 karakter")

    return RegisterValidation.Success
}

fun validateFirstName(firstName: String): RegisterValidation {
    if (firstName.isEmpty())
        return RegisterValidation.Failed("Harap isi nama pertama!")

    return RegisterValidation.Success
}

fun validateLastName(lastName: String): RegisterValidation {
    if (lastName.isEmpty())
        return RegisterValidation.Failed("Harap isi nama terakhir!")

    return RegisterValidation.Success
}

fun validateEmpty(firstName: String, lastName: String,password: String, email: String): RegisterValidation {
    if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && password.isEmpty())
        return RegisterValidation.Failed("Harap diisi semua data dengan lengkap!")

    return RegisterValidation.Success
}