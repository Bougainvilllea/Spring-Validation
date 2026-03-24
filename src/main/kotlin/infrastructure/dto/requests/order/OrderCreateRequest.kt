package org.example.example.infrastructure.dto.requests.order

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull


data class OrderCreateRequest(
    @field:NotNull(message = "Идентификатор пользователя обязателен")
    val userId: Long,

    @field:NotEmpty(message = "Необходимо добавить хотя бы одну позицию в заказ")
    val dishIds: List<Long>
)