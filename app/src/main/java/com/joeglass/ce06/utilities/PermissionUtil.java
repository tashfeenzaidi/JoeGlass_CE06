// Joe Glass

// JAV2 - C20201201

// PermissionUtil.java
package com.joeglass.ce06.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionUtil {

    public static boolean permissionStatus(Context context){
       return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }
}
