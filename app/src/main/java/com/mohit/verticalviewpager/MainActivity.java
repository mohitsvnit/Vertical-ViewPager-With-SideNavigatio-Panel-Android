package com.mohit.verticalviewpager;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    View mainView;
    NavigationView navigationViewLeft, navigationViewRight;
    public float oldX,oldY;
    public ViewPagerAdapter viewPagerAdapter;
    public VerticalViewPager viewPager;
    public Button btnNavLeft;
    public TextView tvNavRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawers();
        initViewPager();
        initNavLeft();
        initNavRight();
    }

    public void initNavLeft(){
        btnNavLeft = (Button) findViewById(R.id.btnNavLeft);
        btnNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Left Navigation Panel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initNavRight(){
        tvNavRight = (TextView) findViewById(R.id.tvNavRight);
        tvNavRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Right Navigation Panel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initDrawers() {
        mainView = findViewById(R.id.main_content);
        navigationViewLeft = (NavigationView) findViewById(R.id.navigation_view_left);
        navigationViewRight = (NavigationView) findViewById(R.id.navigation_view_right);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    Helper.log("Left Drawer Open");
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,GravityCompat.END);
                }else if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,GravityCompat.START);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,GravityCompat.START);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,GravityCompat.END);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(drawerView.getId() == navigationViewLeft.getId()) {
                    mainView.setTranslationX(slideOffset * drawerView.getWidth());
                    drawerLayout.bringChildToFront(navigationViewLeft);
                    drawerLayout.requestLayout();
                }else if(drawerView.getId() == navigationViewRight.getId()){
                    mainView.setTranslationX(slideOffset * -drawerView.getWidth());
                    drawerLayout.bringChildToFront(navigationViewRight);
                    drawerLayout.requestLayout();
                }
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();

                if((newX-oldX) > 50 && Math.abs(newY-oldY) <= 100){
                    swipeDirection("right");
                }else if (newX-oldX < -50 && Math.abs(newY-oldY) <= 100){
                    swipeDirection("left");
                }
                break;
        }


        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    public void initViewPager(){
        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

    }

    public void swipeDirection(String dir){
        if(dir.equals("left")){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.openDrawer(GravityCompat.END);
            }
        }else if(dir.equals("right")){
            if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                drawerLayout.closeDrawer(GravityCompat.END);
            }else{
                drawerLayout.closeDrawer(GravityCompat.END);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }

}
