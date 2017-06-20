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

package com.google.engedu.puzzle8;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then
            for(int i=1;i<=NUM_SHUFFLE_STEPS;i++)
            {
                ArrayList<PuzzleBoard> boardStates;
                boardStates=puzzleBoard.neighbours();
                int size=boardStates.size();
                int index=random.nextInt(size);
                puzzleBoard=boardStates.get(index);
            }
            puzzleBoard.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {

    /*  1.We've to create a PriorityQueue.
        2.Add the current Puzzleboard to it
        3.Repeat:
            3.1 Deque the element with least priority from the PriorityQueue
            3.2 if the current puzzleboard is not the solution,then enqueue the neighbors of the current board to the PriorityQueue
            3.3 if solution is obtained. Create an arraylist of all the puzzleboards that lead to the solution and reverse the list
                3.3.1 Copy that ArrayList to PuzzleBoardView.animation to see the animated solution

        To Implement the priority queue, we need to make a comparator
    */
        //Comparator
        Comparator<PuzzleBoard> puzzleBoardComparator=new Comparator<PuzzleBoard>() {
            @Override
            public int compare(PuzzleBoard one, PuzzleBoard two) {
                int diff=one.priority()-two.priority();
                return diff;
            }
        };

        PriorityQueue<PuzzleBoard> priorityQueue=new PriorityQueue<>(1,puzzleBoardComparator);
        //Set current board steps to 0 and previousboard to null
        puzzleBoard.steps=0;
        puzzleBoard.previousBoard=null;
        //Add current puzzleboard to priorityQueue
        priorityQueue.add(puzzleBoard);
//        Log.d("Before Start",String.valueOf(priorityQueue.size()));
        while(!priorityQueue.isEmpty())
        {
//            Log.d("A start Algo Queue Size",String.valueOf(priorityQueue.size()));
            PuzzleBoard leastPriorityBoard=priorityQueue.remove();
            if(!leastPriorityBoard.resolved())
            {
                priorityQueue.addAll(leastPriorityBoard.neighbours());
            }
            else {
                ArrayList<PuzzleBoard> solvedBoardsList=new ArrayList<>();
                //Add previous boards of the solved board to the list
                while(leastPriorityBoard.previousBoard!=null)
                {
                    solvedBoardsList.add(leastPriorityBoard);
                    leastPriorityBoard=leastPriorityBoard.previousBoard;
                }
                //Reverse the arraylist
                Collections.reverse(solvedBoardsList);
                //To animate
                animation=solvedBoardsList;
                //To update the view
                invalidate();
                break;
            }
        }



    }
}
