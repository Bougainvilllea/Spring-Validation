package org.example.example.infrastructure.dto.requests.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserData(
    @field:NotBlank(message = "Электронная почта обязательна для заполнения")
    @field:Email(message = "Проверьте корректность введенного email-адреса")
    val email: String,

    @field:NotBlank(message = "Имя обязательно для заполнения")
    @field:Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    val firstName: String,

    @field:NotBlank(message = "Фамилия обязательна для заполнения")
    @field:Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    val lastName: String,

    val isActive: Boolean = true
)