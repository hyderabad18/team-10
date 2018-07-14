package com.cfg.iandeye.volunter;

/**
 * Created by baswarajmamidgi on 01/04/17.
 */

public class Fileupload {
    private String name;
    private String url;

    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }


    Fileupload(String name, String url){
        this.name=name;
        this.url=url;
    }
}
