package com.joeglass.ce06;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class ImageDownloadWorker extends Worker {

    public static File storageDir ;
    Context context;
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
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            if (!storageDir.exists()){
                storageDir.mkdirs();
            }

            for (String imageName: IMAGES){
                downloadImage(imageName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

        File file = new File(storageDir+"/"+name +".jpeg");

        String URL_BASE = "https://i.imgur.com/";
        URL url = new URL(URL_BASE +imageName);

        if (!file.exists()){

            URLConnection urlConnection = url.openConnection();

            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");
            try {
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
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
                Intent intent = new Intent("UPDATE");
                context.sendBroadcast(intent);
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }
}
