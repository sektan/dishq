package version1.dishq.dishq.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

import version1.dishq.dishq.customViews.ScrollerCustomDuration;

/**
 * Created by dishq on 5/26/16.
 * Package name version1.dishq.dishq.
 */
public class CustomViewPager extends ViewPager {

    private ScrollerCustomDuration mScroller = null;
    private float initialYValue;
    private SwipeDirection direction;


    public CustomViewPager(Context context) {
        super(context);
        init();
        postInitViewPager();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        postInitViewPager();
        this.direction = SwipeDirection.all;
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * Override the Scroller instance with our own class so we can change the duration
     */

    private void postInitViewPager() {
        try {
            Class<?> viewPager = ViewPager.class;
            Field scroller = viewPager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewPager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
            Log.e("Error:", "Error is:" +e);
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.IsSwipeAllowed(swapXY(event)) && super.onTouchEvent(swapXY(event));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(swapXY(event))) {
            event = swapXY(event);
            boolean intercepted = super.onInterceptTouchEvent(swapXY(event));
            swapXY(event); // return touch coordinates to original reference frame for any child views
            return intercepted;
        }
        return false;
    }

    public void setPagingEnabled(SwipeDirection direction) {
        this.direction = direction;
    }


    private boolean IsSwipeAllowed(MotionEvent event) {
        event = swapXY(event);
        if(this.direction == SwipeDirection.all) return true;

        if(direction == SwipeDirection.none )//disable any swipe
            return false;

        float width = getWidth();
        float height = getHeight();

        float newX = (event.getY() / height) * width;

        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            initialYValue = newX;
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE) {
            try {
                float diffY = newX - initialYValue;
                if (diffY > 0 && direction == SwipeDirection.bottom ) {
                    return false;
                }else if (diffY < 0 && direction == SwipeDirection.top ) {
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public enum SwipeDirection {
        all, top, bottom, none
    }
}
