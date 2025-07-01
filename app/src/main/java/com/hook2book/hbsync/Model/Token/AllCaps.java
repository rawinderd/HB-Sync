package com.hook2book.hbsync.Model.Token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCaps {

    @SerializedName("read")
    @Expose
    private Boolean read;
    @SerializedName("customer")
    @Expose
    private Boolean customer;

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

}