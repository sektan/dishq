package version1.dishq.dishq.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dishq on 5/26/16.
 * Package name version1.dishq.dishq.
 */
public class CustomViewPager extends ViewPager {

    private boolean enabled;
    private SwipeDirection enabledDirection;
    private float initialYValue;

    public CustomViewPager(Context context) {
        super(context);
        init();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
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
        event = swapXY(event);
        //if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(swapXY(event));
//        }
//        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        event = swapXY(event);
        //if (this.IsSwipeAllowed(event)) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(event));
            swapXY(event); // return touch coordinates to original reference frame for any child views
            return intercepted;
//        }
//        return false;
    }


    public void setPagingEnabled(SwipeDirection direction) {
        this.enabledDirection = direction;
    }

//    private boolean IsSwipeAllowed(MotionEvent event) {
//        if(this.enabledDirection == SwipeDirection.BOTH) return true;
//
//        if(enabledDirection == SwipeDirection.NONE )//disable any swipe
//            return false;
//
//        if(event.getAction()== MotionEvent.ACTION_DOWN) {
//            initialYValue = event.getX();
//            return true;
//        }
//
//        if(event.getAction()== MotionEvent.ACTION_MOVE) {
//            try {
//                float diffY = event.getX() - initialYValue;
//                if (diffY > 0 && enabledDirection == SwipeDirection.DOWN) {
//                    return false;
//                }else if (diffY < 0 && enabledDirection == SwipeDirection.UP) {
//                    return false;
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//
//        return true;
//    }
//
    public enum SwipeDirection{
        UP,
        DOWN,
        BOTH,
        NONE
    }

}
