package infrastructure.dto.requests.restaurant

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class RestaurantCreateRequest(
    @field:NotBlank(message = "Наименование ресторана обязательно для заполнения")
    @field:Size(min = 2, max = 100, message = "Наименование должно содержать от 2 до 100 символов")
    val name: String,

    @field:NotBlank(message = "Адрес ресторана обязателен для заполнения")
    val address: String
)