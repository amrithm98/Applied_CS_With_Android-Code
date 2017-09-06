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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

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
    private CircularLinkedList beginning_list=new CircularLinkedList();
    private CircularLinkedList closest_list=new CircularLinkedList();;
    private CircularLinkedList smallest_list=new CircularLinkedList();;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);

        Paint linePaint=new Paint();
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

        //EXTENSION:

        ArrayList<Paint> linePaints=new ArrayList<>();
        ArrayList<CircularLinkedList> lists=new ArrayList<>();
        lists.add(beginning_list);
        lists.add(closest_list);
        lists.add(smallest_list);

        Paint bluePaint=new Paint();
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(5);
        bluePaint.setStyle(Paint.Style.STROKE);

        Paint greenPaint=new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStrokeWidth(7);
        greenPaint.setStyle(Paint.Style.STROKE);

        Paint blackPaint=new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(9);
        blackPaint.setStyle(Paint.Style.STROKE);

        linePaints.add(bluePaint);
        linePaints.add(greenPaint);
        linePaints.add(blackPaint);


        if(beginning_list!=null && closest_list!=null && smallest_list!=null)
        {
            for(int i=2;i>=0;i--)
            {
                Log.d("","");
                current=new Point(0,0);
                firstPoint=new Point(0,0);

                for (Point p : lists.get(i)) {
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
                        canvas.drawLine(current.x,current.y,p.x,p.y,linePaints.get(i));

                    current=p;
                    canvas.drawCircle(p.x, p.y, 20,linePaints.get(i));
                }

                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/

                //Connecting First and Last Points
                canvas.drawLine(current.x,current.y,firstPoint.x,firstPoint.y,linePaints.get(i));
            }
        }

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
                } else if(insertMode.equals("Beginning")){
                    list.insertBeginning(p);
                }
                else if(insertMode.equals("Compare"))
                {
                    beginning_list.insertBeginning(p);
                    closest_list.insertNearest(p);
                    smallest_list.insertSmallest(p);
                    list=smallest_list;
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    if(insertMode.equals("Compare"))
                    {
                        message.setText(String.format("Beg,Close,Small Dist %.2f,%.2f,%.2f", beginning_list.totalDistance(),closest_list.totalDistance(),smallest_list.totalDistance()));
                    }
                    else
                    {
                        message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                    }
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        beginning_list.reset();
        closest_list.reset();
        smallest_list.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
