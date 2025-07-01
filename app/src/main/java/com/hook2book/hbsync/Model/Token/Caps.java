package com.hook2book.hbsync.Model.Token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Caps {

    @SerializedName("customer")
    @Expose
    private Boolean customer;

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

}