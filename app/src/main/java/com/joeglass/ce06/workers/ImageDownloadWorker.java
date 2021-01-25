// Joe Glass

// JAV2 - C20201201

// ImageDownloadWorker.java
package com.joeglass.ce06.workers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.joeglass.ce06.constants.Constants;
import com.joeglass.ce06.utilities.FileUtil;
import com.joeglass.ce06.utilities.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class ImageDownloadWorker extends Worker {

    public File storageDir ;
    final Context context;
    InputStream inputStream = null;
    private final String[] IMAGES = {
            "Df9sV7x.jpg", "nqnegVs.jpg", "JDCG1tP.jpg",
            "tUvlwvB.jpg", "2bTEbC5.jpg", "Jnqn9NJ.jpg",
            "xd2M3FF.jpg", "atWe0me.jpg", "UJROzhm.jpg",
            "4lEPonM.jpg", "vxvaFmR.jpg", "NDPbJfV.jpg",
            "ZPdoCbQ.jpg", "SX6hzar.jpg", "YDNldPb.jpg",
            "iy1FvVh.jpg", "OFKPzpB.jpg", "P0RMPwI.jpg",
            "lKrCKtM.jpg", "eXvZwlw.jpg", "zFQ6TwY.jpg",
            "mTY6vrd.jpg", "QocIraL.jpg", "VYZGZnk.jpg",
            "RVzjXTW.jpg", "1CUQgRO.jpg", "GSZbb2d.jpg",
            "IvMKTro.jpg", "oGzBLC0.jpg", "55VipC6.jpg"
    };


    public ImageDownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        storageDir = new File(getInputData().getString("file_dir"));

        try {
            if (!storageDir.exists()){
                if (!FileUtil.makeDir(storageDir)){
                    return Result.failure();
                }
            }
            for (String imageName: IMAGES){
                downloadImage(imageName);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }

        return Result.success();
    }

    public void downloadImage(String imageName) throws IOException {

        String name = imageName.substring(0, imageName.lastIndexOf('.'));
        for (File path: Objects.requireNonNull(storageDir.listFiles())){
            String pathName = path.getName();
            pathName = pathName.substring(0,pathName.indexOf("."));
            if (pathName.equals(name)){
                Intent intent = new Intent("UPDATE");
                context.sendBroadcast(intent);
                return;
            }
        }

        File file = FileUtil.getJpgFile(storageDir+"/"+name);
        URL url = new URL(Constants.URL_BASE +imageName);

        if (!file.exists()){
            try {
                inputStream = NetworkUtil.networkCall(url);
                FileUtil.fileInput(file,inputStream);
                Intent intent = new Intent("UPDATE");
                context.sendBroadcast(intent);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
