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

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };

    private ArrayList<PuzzleTile> tiles=new ArrayList<>();

    //Added for solving
    public int steps;
    public PuzzleBoard previousBoard;



    PuzzleBoard(Bitmap bitmap, int parentWidth) {

        //Scale the given image to make it a square
        Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,true);

        int tileWidth=parentWidth/NUM_TILES;
        int i,j,count=0;
        for(i=0;i<NUM_TILES;i++)
        {
            for(j=0;j<NUM_TILES;j++)
            {
                //X,Y,Height,Width are required. X-(j*tileWidth) Y-(i*tileWidth)

                Bitmap chunk=Bitmap.createBitmap(scaledBitmap,j*tileWidth,i*tileWidth,tileWidth,tileWidth);

                //Add this chunk to the tiles Arraylist--> 3*i+j will be the index of each tile

                PuzzleTile object=new PuzzleTile(chunk,count);
                count+=1;
                tiles.add(object);
            }
        }
        //Setting the 9th square to be null
        //tiles.set(8,null);
        tiles.set(NUM_TILES*NUM_TILES-1,null);
        steps=0;
        previousBoard=null;

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        steps+=1;
        previousBoard=otherBoard;
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.

    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        //Checks if the tiles are inplace
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {

        for(int count=0;count<tiles.size();count++)
        {
            PuzzleTile tile=tiles.get(count);
            //1. Located the empty tile
            if(tile==null)
            {
                ArrayList<PuzzleBoard> boardStates=new ArrayList<>();
                //Find out valid neighbors
                //2.Consider all the neighbours of the empty square
                for(int [] XY:NEIGHBOUR_COORDS)
                {
                    //Note that we mapped a 3X3 array into 0-8 arraylist
                    //Checking if neighbours are inside the board
                    int posX,posY,curX,curY;
                    posY=count/NUM_TILES;   //Use index 6 and 8 as examples to explain. Consider method XYtoindex()
                    posX=count%NUM_TILES;
                    curX=XY[0];
                    curY=XY[1];
                    //Checks if the Neighbour's X value is inside the board
                    if( posX+curX <NUM_TILES && posX+curX>=0)
                    {
                        //Checks if the Neighbour's Y value is inside the board
                        if(posY+curY<NUM_TILES && posY+curY>=0)
                        {
                            //3. Valid neighbour, create new board
                            //We cloned the current board to a new board
                            PuzzleBoard board=new PuzzleBoard(this);
                            //Now we will swap the null tile with the current neighbour in the cloned board
                            //count has the current null tile
                            //We need to find the index of the neighbour in the arraylist
                            //3 Rows...Going down by each row increases index by 3
                            board.swapTiles(count,count+curX+NUM_TILES*curY);
                            //Added the new board to the state list
                            boardStates.add(board);
                        }
                    }
                }
                return boardStates;
            }
        }
        return null;
    }

    public int priority() {
        //Need to find Manhattan distances
        //Sum of rowchange required + column change required
        int manhattanDistance=0;
        int rowShift,columnShift;
        for(int i=0;i<tiles.size();i++)
        {
            if(tiles.get(i)!=null)
            {
                //Get the position of the tile in the arraylist
                int tilePos=tiles.get(i).getNumber();
                //Imagine shifting Tile with number 1 from index 5 to index 0. We need to shift 2 col and 1 row
                columnShift=Math.abs(i%NUM_TILES-tilePos%NUM_TILES);
                rowShift=Math.abs(i/NUM_TILES-tilePos/NUM_TILES);
                manhattanDistance+=rowShift+columnShift;
            }
        }
        int totalPriority=manhattanDistance+steps;
        return totalPriority;
    }

}
