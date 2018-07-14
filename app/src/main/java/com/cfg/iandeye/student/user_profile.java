
package com.cfg.iandeye.student;

/**
 * Created by lenovo on 7/14/2018.
 */

public class user_profile {
    private String name;
    private  String std;
    private String location;

    public user_profile()
    {

    }

    public user_profile(String name, String std, String location) {
        this.name = name;
        this.std = std;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
