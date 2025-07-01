package com.hook2book.hbsync.Model.InitialVaribales;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InitialVariablesMain {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<VariableData> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VariableData> getData() {
        return data;
    }

    public void setData(List<VariableData> data) {
        this.data = data;
    }

}
