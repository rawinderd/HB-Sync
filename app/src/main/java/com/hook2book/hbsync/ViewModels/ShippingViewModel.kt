package com.sikhreader.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikhreader.Repository.ShippingRepo
import kotlinx.coroutines.launch

class ShippingViewModel: ViewModel() {
    var shippingRepo: ShippingRepo=ShippingRepo()
}