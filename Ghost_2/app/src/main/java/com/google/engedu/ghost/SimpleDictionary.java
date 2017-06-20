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

package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {

    private ArrayList<String> words;
    int start,stop;
    Random random=new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        getGoodWordStartingWith(prefix);
        if(prefix=="")
        {
            int index=random.nextInt(words.size());
            return words.get(index);
        }
        else {
            String answer=binarySearch(prefix);
            return answer;
        }
    }

    public String binarySearch(String word){
        String result="";
        int beg=0,end=words.size(),mid=0;
        start=beg;stop=end;
        mid=(beg+end)/2;
        while(beg<end)
        {
            Log.d("Mid",String.valueOf(mid));
            mid=(beg+end)/2;
            result=words.get(mid);
            Log.d("Result",String.valueOf(result));
            if(result.startsWith(word))
            {
                Log.d("Word",result);
                start=mid;
                stop=end;
                return result;
            }
            else if(result.compareToIgnoreCase(word)>0){
                Log.d("lower",String.valueOf(result));
                //result lower than word
                end=mid;
            }
            else if(result.compareToIgnoreCase(word)<0){
                Log.d("greater",String.valueOf(result));
                //result greater than word
                beg=mid+1;
            }
        }
        return "";
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = "";
        String bs=binarySearch(prefix);
        Log.d("goodword",String.valueOf(start)+" stop "+String.valueOf(stop));
        List<String> prefList=words.subList(start,stop);
        Log.d("goodwordfound",prefList.get(0));
        return prefList.get(0);
    }
}
