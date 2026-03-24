package org.example.example.infrastructure.dto.requests.order

import jakarta.validation.constraints.NotBlank

data class OrderStatusUpdateRequest(
    @field:NotBlank(message = "Необходимо указать статус заказа")
    val status: String
)