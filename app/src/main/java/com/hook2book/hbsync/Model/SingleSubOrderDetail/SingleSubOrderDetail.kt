package com.hook2book.hbsync.Model.SingleSubOrderDetail

data class SingleSubOrderDetail(
    val data_outer: List<DataOuter>,
    val message: String,
    val status: String
)