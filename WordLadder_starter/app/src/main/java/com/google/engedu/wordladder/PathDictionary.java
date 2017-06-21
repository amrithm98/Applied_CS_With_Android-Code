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

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static final int MAX_DEPTH=7;
    private static HashSet<String> words = new HashSet<>();

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
            words.add(word);
        }
    }

    public boolean isWord(String word) {

        return words.contains(word.toLowerCase());
    }

    public ArrayList<String> neighbours(String word) {
        char alpha[]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        //Here we change each letter of the word to get a valid word and add them to a list
        ArrayList<String> neighbourWords=new ArrayList<>();
        for(int i=0;i<word.length();i++)
        {
            for(char ch : alpha)
            {
                //String is immutable ... StringBuilder is mutable
                StringBuilder str=new StringBuilder(word);
                str.setCharAt(i,ch);
                if(isWord(str.toString()))
                {
                    if(!str.equals(word))
                        neighbourWords.add(str.toString());
                }

            }
        }
        return neighbourWords;
    }


    public String[] findPath(String start, String end) {
//        Log.d("Words",neighbours(start).get(0));
//        Log.d("Words Length",String.valueOf(neighbours(start).size()));
        //We need to BFS over the word graph by adding neighbors of a word to a queue
        ArrayDeque<ArrayList<String >> arrayListArrayDeque=new ArrayDeque<>();
        HashSet<String> visited = new HashSet<>();
        visited.add(start);
        ArrayList<String> startElement=new ArrayList<>();
        startElement.add(start);
        arrayListArrayDeque.add(startElement);
        while(!arrayListArrayDeque.isEmpty()) {
            ArrayList<String> currentPath = arrayListArrayDeque.poll();

            if (currentPath.size()>MAX_DEPTH) {
                break;
            }
            //We need to examine each neighbor. If neighbor is the end..add it to currnt path and return the path
            //Otherwise add the neighbour to the current path
            String lastWord = currentPath.get(currentPath.size() - 1);
            for (String s : neighbours(lastWord)) {
                if (s.equals(end)) {
                    currentPath.add(end);
                    return currentPath.toArray(new String[currentPath.size()]);
                } else {
                    if(!visited.contains(s))
                    {
                        visited.add(s);
                        ArrayList<String> nextPath = new ArrayList<>(currentPath);
                        nextPath.add(s);
                        arrayListArrayDeque.add(nextPath);
                    }
                }
            }
        }
        return null;
    }
}
