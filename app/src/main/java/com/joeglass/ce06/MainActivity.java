package com.joeglass.ce06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
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
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}