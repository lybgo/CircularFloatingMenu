package com.lybgo.circularfloatingmenudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.lybgo.circularfloatingmenu.CircularFloatingMenu;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Bind(R.id.menu1)
    CircularFloatingMenu mMenu1;
    @Bind(R.id.menu2)
    CircularFloatingMenu mMenu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initCircularFloatingMenu();
    }

    private void initCircularFloatingMenu() {
        mMenu1.setOnItemClickListener(new CircularFloatingMenu.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                Log.w(TAG,"onItemClick index:" + index);
            }

            @Override
            public void onMenuClick(View view, boolean isOpen) {
                Log.w(TAG,"onMenuClick isOpen:" + isOpen);
            }
        });

        mMenu2.setOnItemClickListener(new CircularFloatingMenu.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                Log.w(TAG,"mMenu2 onItemClick index:" + index);
            }

            @Override
            public void onMenuClick(View view, boolean isOpen) {
                Log.w(TAG,"mMenu2 onMenuClick isOpen:" + isOpen);
                ViewPropertyAnimator.animate(view).rotation(isOpen?90:0).setDuration(300).start();
            }
        });
        mMenu2.setOnItemTranslationListener(new CircularFloatingMenu.OnItemTranslationListener() {
            Interpolator outInterpolator = new OvershootInterpolator();
            Interpolator inInterpolator = new AnticipateInterpolator();
            float defaultRotation = -180;
            float defaultAlpha = 0f;

            @Override
            public void translationItem(View v, int x, int y, boolean isOpen) {
                if (isOpen) {
                    ViewHelper.setRotation(v, defaultRotation);
                    ViewHelper.setAlpha(v, defaultAlpha);
                }
                Interpolator interpolator = isOpen ? outInterpolator : inInterpolator;
                float toRotation = isOpen ? 0 : defaultRotation;
                float toAlpha = isOpen ? 1 : defaultAlpha;

                ViewPropertyAnimator.animate(v).translationX(x).translationY(y).rotation(toRotation).alpha(toAlpha)
                        .setInterpolator(interpolator).setDuration(500).start();
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
