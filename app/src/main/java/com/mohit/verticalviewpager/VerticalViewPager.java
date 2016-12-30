package com.mohit.verticalviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mohit on 29/12/16.
 */

public class VerticalViewPager extends ViewPager {

    public float oldX,oldY;

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public void init(){
        setPageTransformer(true,new VerticalViewPagerTransform());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public VerticalViewPager(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    private MotionEvent swapXY(MotionEvent event) {
        float x = getWidth();
        float y = getHeight();

        float newX = (event.getY()/y)*x;
        float newY = (event.getX()/x)*y;
        //log("oldX: " + String.valueOf(event.getX()) + " newX: " + String.valueOf(newX));
        //log("oldY: " + String.valueOf(event.getY()) + " newY: " + String.valueOf(newY));
        event.setLocation(newX,newY);
       // log(String.valueOf(event.getX()) + " " + String.valueOf(event.getY()));
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        return intercept;
    }

    public void log(String msg){
        Log.e("mohit",msg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(swapXY(ev));
       // return false;
    }

    private class VerticalViewPagerTransform implements ViewPager.PageTransformer{

        private static final float Min_Scale = 0.75f;
        @Override
        public void transformPage(View page, float position) {

            if(position < -1) {
                page.setAlpha(0);
            }else if(position <= 0){
                page.setAlpha(1

                );
                page.setTranslationX(page.getWidth()* -position);
                page.setTranslationY(page.getHeight()*position);
                page.setScaleX(1);
                page.setScaleY(1);
            }else if(position <= 1){
                page.setAlpha(1-position);
                page.setTranslationX(page.getWidth()* -position);
                page.setTranslationY(0);
                float scaleFactor = Min_Scale + (1 - Min_Scale) * (1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            }else if(position > 1){
                page.setAlpha(0);
            }
        }
    }
}
