package com.joeglass.ce06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(CONTENT_VIEW_ID);
        setContentView(frameLayout, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            ItemFragment itemFragment =  ItemFragment.newInstance(2);
            getSupportFragmentManager().beginTransaction().add(CONTENT_VIEW_ID,itemFragment).commit();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.w("Check", "Inside On Receiver");
                Toast.makeText(getApplicationContext(),"UPDATE",Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(receiver,intentFilter);

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
            Intent intent = new Intent(this,ImageDownloaderService.class);
            startService(intent);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }


}