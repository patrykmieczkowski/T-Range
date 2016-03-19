package com.kitowcy.t_range.dial;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class MediaFile {
    public MediaFile(String path, String title, boolean isChecked) {
        this.path = path;
        this.title = title;
        this.isChecked = isChecked;
    }

    public MediaFile() {
    }

    public String path;
    public String title;
    public boolean isChecked;
}
