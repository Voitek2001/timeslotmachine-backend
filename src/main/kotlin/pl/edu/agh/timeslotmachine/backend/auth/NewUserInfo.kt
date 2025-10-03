package pl.edu.agh.timeslotmachine.backend.auth

import pl.edu.agh.timeslotmachine.backend.user.IndexNo

data class NewUserInfo(
    val username: String,
    val password: String,
    val indexNumber: IndexNo,
    val email: String,
    val name: String,
    val surname: String
)