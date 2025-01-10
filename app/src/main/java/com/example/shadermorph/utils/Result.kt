package com.example.shadermorph.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure(val throwable: Throwable? = null) : Result<Nothing>
    // object Loading : Result<Nothing>
}

inline fun <T> Result<T>.mapSuccess(
    crossinline onResult: Result.Success<T>.() -> Result<T>,
): Result<T> {
    if (this is Result.Success) {
        return onResult(this)
    }
    return this
}

fun <T> Flow<T>.toResult(): Flow<Result<T>> = flow {
    try {
        collect { value -> emit(Result.Success(value)) }
    } catch (e: Throwable) {
        emit(Result.Failure(e))
    }
}