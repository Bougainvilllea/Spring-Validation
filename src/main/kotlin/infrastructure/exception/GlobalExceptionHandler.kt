package infrastructure.exception

import org.example.example.infrastructure.dto.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.MediaType
import shared.exception.BusinessException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.validation.FieldError

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.allErrors.joinToString("; ") { error ->
            val fieldName = (error as? FieldError)?.field ?: error.objectName
            "$fieldName: ${error.defaultMessage}"
        }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Request validation failed: $errors"
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val message = when (val cause = ex.cause) {
            is InvalidFormatException -> {
                val fieldName = cause.path.joinToString(".") { it.fieldName }
                "Field '$fieldName' has incorrect format. Expected type: ${cause.targetType.simpleName}"
            }
            else -> "Request body contains malformed JSON. Please verify the format."
        }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = message
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Provided input is invalid"
        )

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка User исключений
    @ExceptionHandler(BusinessException.UserNotFound::class)
    fun handleUserNotFound(ex: BusinessException.UserNotFound): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "User Not Found",
            message = ex.message ?: "The requested user does not exist"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка Dish исключений
    @ExceptionHandler(BusinessException.DishNotFound::class)
    fun handleDishNotFound(ex: BusinessException.DishNotFound): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Dish Not Found",
            message = ex.message ?: "The requested dish could not be found"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    @ExceptionHandler(BusinessException.DishNameAlreadyExists::class)
    fun handleDishNameExists(ex: BusinessException.DishNameAlreadyExists): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Dish Name Already Exists",
            message = ex.message ?: "A dish with this name is already registered"
        )
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка Restaurant исключений
    @ExceptionHandler(BusinessException.RestaurantNotFound::class)
    fun handleRestaurantNotFound(ex: BusinessException.RestaurantNotFound): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Restaurant Not Found",
            message = ex.message ?: "The specified restaurant does not exist"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    @ExceptionHandler(BusinessException.RestaurantNameAlreadyExists::class)
    fun handleRestaurantNameExists(ex: BusinessException.RestaurantNameAlreadyExists): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Restaurant Name Already Exists",
            message = ex.message ?: "A restaurant with this name is already present"
        )
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка Order исключений
    @ExceptionHandler(BusinessException.OrderNotFound::class)
    fun handleOrderNotFound(ex: BusinessException.OrderNotFound): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Order Not Found",
            message = ex.message ?: "The requested order does not exist"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    @ExceptionHandler(BusinessException.InvalidOrderStatusTransition::class)
    fun handleInvalidOrderStatusTransition(ex: BusinessException.InvalidOrderStatusTransition): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Invalid Status Transition",
            message = ex.message ?: "The requested status change is not permitted"
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка OrderValidationError - возвращаем 400 BAD REQUEST
    @ExceptionHandler(BusinessException.OrderValidationError::class)
    fun handleOrderValidationError(ex: BusinessException.OrderValidationError): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = ex.message ?: "Order details are invalid"
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка общих валидационных исключений
    @ExceptionHandler(
        BusinessException.InvalidUserData::class,
        BusinessException.DishValidationError::class,
        BusinessException.UserValidationError::class,
        BusinessException.RestaurantValidationError::class
    )
    fun handleBusinessValidationError(ex: BusinessException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = ex.message ?: "Provided information does not meet requirements"
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Обработка Email конфликта
    @ExceptionHandler(BusinessException.EmailAlreadyExists::class)
    fun handleEmailExists(ex: BusinessException.EmailAlreadyExists): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Email Already Exists",
            message = ex.message ?: "This email address is already associated with an account"
        )
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }

    // Общая обработка исключений
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        ex.printStackTrace()

        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "Something went wrong on our end. Please try again later."
        )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse)
    }
}