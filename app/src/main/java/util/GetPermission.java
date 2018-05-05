package util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.ff.wei.MapActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孵孵 on 2018/4/19 0019.
 */

public class GetPermission{
    static Context context;
    static Activity act;
    static int requestCode=1;
    public static void makePermission(Context context2,Activity act2){
        context=context2;
        act=act2;
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissionStr = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(act, permissionStr, requestCode);//requestCode=1
        }
    }
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int x = 0; x < grantResults.length && x < permissions.length; ++x) {
                if (grantResults[x] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "同意啊", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(act, new String[]{permissions[x]}, 1);   //requestCode=1
                }
            }
        }
    }
}
