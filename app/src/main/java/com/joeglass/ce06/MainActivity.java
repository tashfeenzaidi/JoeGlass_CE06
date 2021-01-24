// Joe Glass

// JAV2 - C20201201

// MainActivity.java
package com.joeglass.ce06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
    ItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(CONTENT_VIEW_ID);
        setContentView(frameLayout, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null){
            itemFragment =  ItemFragment.newInstance(2);
            getSupportFragmentManager().beginTransaction().add(CONTENT_VIEW_ID,itemFragment).commit();

        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                itemFragment.updateList();
            }
        };
        registerReceiver(receiver,intentFilter);

        requestRead();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.download){
//            registerService();

            if (NetworkUtil.getNetworkStatus(this)){
                initWorker();
            }else {
                Toast.makeText(this,"Please connect the internet ",Toast.LENGTH_SHORT).show();
            }
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void registerService(){
        Intent intent = new Intent(this,ImageDownloaderService.class);
        startService(intent);
    }

    public void initWorker(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        WorkRequest downloadWorkRequest = new OneTimeWorkRequest.Builder(ImageDownloadWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(downloadWorkRequest);
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE){
            if (grantResults.length < 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"You cannot",Toast.LENGTH_LONG).show();
            }
        }
    }
}