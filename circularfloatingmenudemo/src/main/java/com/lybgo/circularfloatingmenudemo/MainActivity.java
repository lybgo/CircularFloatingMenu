package com.lybgo.circularfloatingmenudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lybgo.circularfloatingmenu.CircularFloatingMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Bind(R.id.circularFloatingMenu)
    CircularFloatingMenu mCircularFloatingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initCircularFloatingMenu();
    }

    private void initCircularFloatingMenu() {
        mCircularFloatingMenu.setOnItemClickListener(new CircularFloatingMenu.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                Log.w(TAG,"onItemClick index:" + index);
            }

            @Override
            public void onMenuClick(View view, boolean isOpen) {
                Log.w(TAG,"onMenuClick isOpen:" + isOpen);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
