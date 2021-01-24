// Joe Glass

// JAV2 - C20201201

// FileUtil.java
package com.joeglass.ce06.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FileUtil {

    public static File getJpgFile(String name){
        return new File(name+".jpg");
    }

    public static void fileInput(File file,InputStream inputStream) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        int size = 4096;
        byte[] buf = new byte[size];
        int byteRead;
        if(inputStream != null){
            while (((byteRead = inputStream.read(buf)) != -1)) {
                fos.write(buf, 0, byteRead);
            }
        }
        fos.close();
    }

    public static boolean makeDir(File file){
        return file.mkdirs();
    }

    public static File[] getFlies(File filePath){
        if (filePath.exists()){
            if (Objects.requireNonNull(filePath.listFiles()).length>0){
                return  filePath.listFiles();
            }
        }
        return null;
    }

}
