package com.sikhreader.EnumClasses

enum class ApiStatus {
    SUCCESS, ERROR, LOADING
}  // for your case might be simplify to use only sealed class

sealed class ApiResult<out T>(
    val status: ApiStatus,
    val data: T?,
    val message: String?,
    val totalProducts: String?,
    val totalPages: String?
) {


    data class Success<out R>(
        val _data: R?, val _totalProducts: String?, val _totalPages: String?
    ) : ApiResult<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null,
        totalProducts = _totalProducts,
        totalPages = _totalPages
    )

    data class Error(
        val exception: String?, val _totalProducts: String?, val _totalPages: String?
    ) : ApiResult<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception,
        totalProducts = _totalProducts,
        totalPages = _totalPages
    )

    data class Loading<out R>(
        val _data: R?,
        val isLoading: Boolean,
        val _totalProducts: String?,
        val _totalPages: String?
    ) : ApiResult<R>(
        status = ApiStatus.LOADING,
        data = _data,
        message = null,
        totalProducts = _totalProducts,
        totalPages = _totalPages
    )
}