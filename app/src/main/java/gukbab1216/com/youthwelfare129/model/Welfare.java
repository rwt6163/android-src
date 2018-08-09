package gukbab1216.com.youthwelfare129.model;

import android.graphics.drawable.Drawable;

public class Welfare {

    public int image;
    public Drawable imageDrw;
    public String title;
    public String explanation;
    public String url;
    public String address;

    public boolean section = false;

    public Welfare() {
    }

    public Welfare(String title, boolean section) {
        this.title = title;
        this.section = section;
    }

}
