package util;

import android.util.Log;

import com.baidu.mapapi.map.MapView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 孵孵 on 2018/4/19 0019.
 */

public class FileOperation {

    public static boolean copyFile(InputStream src,String dst){
        FileOutputStream out = null;
        try {
            Log.d("Status","ok");
            byte[] b = new byte[src.available()];
            src.read(b);
            String moduleName ="/storage/emulated/0/";
            File f = new File(dst);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
