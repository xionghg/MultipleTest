package com.gionee.multipletest.pic;

/**
 * Created by xionghg on 17-2-24.
 */

public class Picture {

    private String name;

    private String pictureUrl;

    public Picture(String name, String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
