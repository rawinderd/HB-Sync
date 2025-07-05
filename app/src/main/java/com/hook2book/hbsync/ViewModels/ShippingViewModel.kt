package com.hook2book.hbsync.Model.ViewModels

import androidx.lifecycle.ViewModel
import com.hook2book.hbsync.Repository.ShippingRepo

class ShippingViewModel : ViewModel() {
    var shippingRepo: ShippingRepo = ShippingRepo()
}