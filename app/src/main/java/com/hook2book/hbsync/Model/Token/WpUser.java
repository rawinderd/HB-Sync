package com.hook2book.hbsync.Model.Token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WpUser {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("caps")
    @Expose
    private Caps caps;
    @SerializedName("cap_key")
    @Expose
    private String capKey;
    @SerializedName("roles")
    @Expose
    private List<String> roles;
    @SerializedName("allcaps")
    @Expose
    private AllCaps allCaps;
    @SerializedName("filter")
    @Expose
    private Object filter;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Caps getCaps() {
        return caps;
    }

    public void setCaps(Caps caps) {
        this.caps = caps;
    }

    public String getCapKey() {
        return capKey;
    }

    public void setCapKey(String capKey) {
        this.capKey = capKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public AllCaps getAllCaps() {
        return allCaps;
    }

    public void setAllCaps(AllCaps allCaps) {
        this.allCaps = allCaps;
    }

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

}