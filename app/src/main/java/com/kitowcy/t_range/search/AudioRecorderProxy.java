package com.kitowcy.t_range.search;

import android.os.Environment;

import com.kitowcy.t_range.dial.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz Marczak
 *
 * @since 19.03.16
 */
public class AudioRecorderProxy {



    public static List<MediaFile> getAllRecordings(){

        List<MediaFile> mediaFileList = new ArrayList<>();

        File storageDirAlpha = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File storageDir = new File(storageDirAlpha, "/audalize");
        storageDir.mkdirs();

        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                mediaFileList.add(new MediaFile(f.getAbsolutePath(), f.getName(), true));
            }
        }
        return mediaFileList;
    }
}
