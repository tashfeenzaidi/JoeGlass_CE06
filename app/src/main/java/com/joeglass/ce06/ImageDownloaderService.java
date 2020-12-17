package com.joeglass.ce06;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.FileUtils;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloaderService extends Service {

    private final String URL_BASE = "https://i.imgur.com/";
    private final String APP_NAME = "JoeGlass_CE06";
    public static File storageDir ;
    InputStream  inputStream = null;
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!storageDir.exists()){
            storageDir.mkdirs();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            for (String imageName: IMAGES){
                downloadImage(imageName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    public void downloadImage(String imageName) throws IOException {


        String name = imageName.substring(0, imageName.lastIndexOf('.'));
        for (File path:storageDir.listFiles()){
            String pathName = path.getName();
            pathName = pathName.substring(0,pathName.indexOf("."));
            if (pathName.equals(name)){
                Intent intent = new Intent("UPDATE");
                sendBroadcast(intent);
                return;
            }
        }

        File file = new File(storageDir+"/"+name +".txt");

        URL url = new URL(URL_BASE+imageName);

        if (!file.exists()){

            URLConnection urlConnection = url.openConnection();

            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
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
                        while (((byteRead = inputStream.read(buf)) != -1)) {
                            fos.write(buf, 0, byteRead);
                        }
                        fos.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    Intent intent = new Intent("UPDATE");
                    sendBroadcast(intent);

                }
            }.execute();


        }

    }
}
