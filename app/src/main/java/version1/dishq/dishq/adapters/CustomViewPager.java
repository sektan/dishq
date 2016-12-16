package version1.dishq.dishq.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
        enabledDirection = SwipeDirection.NONE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
    public void setPagingEnabled(SwipeDirection direction) {
        this.enabledDirection = direction;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if(this.enabledDirection == SwipeDirection.BOTH) return true;

        if(enabledDirection == SwipeDirection.NONE )//disable any swipe
            return false;

        if(event.getAction()== MotionEvent.ACTION_DOWN) {
            initialYValue = event.getX();
            return true;
        }

        if(event.getAction()== MotionEvent.ACTION_MOVE) {
            try {
                float diffY = event.getY() - initialYValue;
                if (diffY > 0 && enabledDirection == SwipeDirection.RIGHT ) {
                    return false;
                }else if (diffY < 0 && enabledDirection == SwipeDirection.LEFT ) {
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public enum SwipeDirection{
        LEFT,
        RIGHT,
        BOTH,
        NONE
    }

}
