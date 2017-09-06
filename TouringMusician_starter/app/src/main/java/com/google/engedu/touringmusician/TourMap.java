/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        Paint linePaint=new Paint();
        pointPaint.setColor(Color.RED);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/

        /*We Need A pointer to keep track of the previous node( to draw line between prev and p)
         and one pointer to keep track of the starting node(to draw the last line)*/

        Point current=new Point(0,0);
        Point firstPoint=new Point(0,0);

        for (Point p : list) {
            /**
             **
             **  YOUR CODE GOES HERE
             **
             **/
            if(current.equals(0,0))
            {
                current=new Point(p);
                firstPoint=new Point(p);
            }
            else
                canvas.drawLine(current.x,current.y,p.x,p.y,linePaint);

            current=p;
            canvas.drawCircle(p.x, p.y, 20, pointPaint);

        }

        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/

        //Connecting First and Last Points
        linePaint.setColor(Color.RED);
        canvas.drawLine(current.x,current.y,firstPoint.x,firstPoint.y,linePaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
