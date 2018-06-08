package com.example.baraka62.checkit;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by baraka62 on 11/30/2017.
 */

public class User {
    private String userName,imgUrl;

    public User(){

    }
    public User(String name, String imgUrl){
        this.userName = name;
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }
    public String getImgUrl(){
        return imgUrl;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public void setImgUrl(String profilePic) {
        this.imgUrl = profilePic;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("imgUrl", imgUrl);

        return result;
    }
}
