package com.example.angry.project2345;

/**
 * Created by angry on 07.04.2017.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityLogInRegister extends FragmentActivity {

    ViewPager mPager;
    DemoParallaxAdapter mAdapter;
    private boolean touched=false,act=false;
    Timer t;
    TimerTask tt;

    public static Activity activity;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_pager_act);
        activity = this;
        mPager = (ViewPager) findViewById(R.id.pagersingin);
        mPager.setBackgroundColor(0xFF000000);

        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.image));
        pt.setBorder(20);
        mPager.setPageTransformer(false, pt);

        mAdapter = new DemoParallaxAdapter(getSupportFragmentManager());
        mAdapter.setPager(mPager); //only for this transformer

        Bundle bNina = new Bundle();
        bNina.putInt("image", R.drawable.background1);
        bNina.putString("name", "Nina");
        FragmentSignIn pfNina = new FragmentSignIn();
        pfNina.setArguments(bNina);

        Bundle bNiju = new Bundle();
        bNiju.putInt("image", R.drawable.background2);
        bNiju.putString("name", "Ninu Junior");
        FragmentRegister pfNiju = new FragmentRegister();
        pfNiju.setArguments(bNiju);


        mAdapter.add(pfNina);
        mAdapter.add(pfNiju);
        mPager.setAdapter(mAdapter);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().show();
        }

        tt = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (act){
                            act = !act;
                            changepager(mPager.getCurrentItem()+1);
                        }
                        else {
                            act = !act;
                            changepager(mPager.getCurrentItem()-1);

                        }
                        Log.d("viewpager", "run: changed");
                    }
                });
            }
        };
        t = new Timer();

        Log.d("viewpager", "onCreate: started");
        t.schedule(tt,0,5000);


        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (t != null) {
                    t.cancel();
                    t = null;
                }
                return false;
            }
        });
    }


    private void changepager(int i){
        mPager.setCurrentItem(i,true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}