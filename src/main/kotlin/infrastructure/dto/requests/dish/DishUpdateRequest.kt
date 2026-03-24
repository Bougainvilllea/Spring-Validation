package infrastructure.dto.requests.dish

import java.math.BigDecimal

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class DishUpdateRequest(
    @field:NotBlank(message = "Наименование блюда обязательно для заполнения")
    val name: String,

    val description: String? = null,

    @field:NotNull(message = "Необходимо указать стоимость блюда")
    @field:Min(value = 1, message = "Стоимость должна быть положительным числом")
    val price: BigDecimal,

    @field:NotNull(message = "Необходимо указать статус доступности блюда")
    val isAvailable: Boolean,

    @field:NotNull(message = "Необходимо указать идентификатор ресторана")
    val restaurantId: Long
)