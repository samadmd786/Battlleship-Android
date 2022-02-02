package edu.msu.leemyou1.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GridView extends View {

    /**
     * The actual Grid
     */
    private Grid grid;

    /**
     * First touch status
     */
    private Touch touch1 = new Touch();

    /**
     * Second touch status
     */
    private Touch touch2 = new Touch();

    public GridView(Context context) {
        super(context);
        init(null, 0);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        grid = new Grid(getContext(), this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int id = event.getPointerId(event.getActionIndex());

        grid.onTouchEvent(this, event);

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event);
                touch1.copyToLast();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event);
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
                touch1.id = -1;
                touch2.id = -1;
                invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                if(id == touch2.id) {
                    touch2.id = -1;
                } else if(id == touch1.id) {
                    // Make what was touch2 now be touch1 by
                    // swapping the objects.
                    Touch t = touch1;
                    touch1 = touch2;
                    touch2 = t;
                    touch2.id = -1;
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                getPositions(event);
                move();
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        grid.draw(canvas);
    }

    /**
     * Save the Grid to a bundle
     *
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        grid.saveInstanceState(bundle);
    }

    /**
     * Getter for the Grid
     * @return Grid the actual Grid
     */
    public Grid getGrid(){
        return grid;
    }

    /**
     * Load the Grid from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        grid.loadInstanceState(bundle);
    }

    private class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }
    }

    /**
     * Get the positions for the two touches and put them
     * into the appropriate touch objects.
     * @param event the motion event
     */
    private void getPositions(MotionEvent event) {
        for(int i=0;  i<event.getPointerCount();  i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            // Get coordinates
            float x = event.getX(i);
            float y = event.getY(i);

            if(id == touch1.id) {
                touch1.x = x;
                touch1.y = y;
            } else if(id == touch2.id) {
                touch2.x = x;
                touch2.y = y;
            }
        }

        invalidate();
    }

    private float length(float xOne, float yOne, float xTwo, float yTwo) {
        float dX = xTwo - xOne;
        float dY = yTwo - yOne;
        double calc = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

        return (float)calc;
    }

    public void hatScale(float dScale, float xOne, float yOne) {
        grid.scale(dScale, xOne, yOne);
    }

    private void move() {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if(touch1.id < 0) {
            return;
        }

        if(touch2.id >= 0) {

            float lengthOne = length(touch1.lastX, touch1.lastY, touch2.lastX, touch2.lastY);

            float lengthTwo = length(touch1.x, touch1.y, touch2.x, touch2.y);

            float da2 = lengthTwo / lengthOne;
            hatScale(da2, touch1.x, touch1.y);
        }
    }
}