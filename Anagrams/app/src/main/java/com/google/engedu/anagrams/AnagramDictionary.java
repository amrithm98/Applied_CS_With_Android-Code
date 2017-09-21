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

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;

    private static final int DEFAULT_WORD_LENGTH = 3;

    private static final int MAX_WORD_LENGTH = 7;

    public int wordLength=DEFAULT_WORD_LENGTH;

    private Random random = new Random();

    public ArrayList<String> wordList=new ArrayList<>();

    public HashMap<String,ArrayList<String>> lettersToWord=new HashMap<>();

    public HashSet<String> wordSet=new HashSet<>();

    public HashMap<Integer,ArrayList<String>> sizeToWords=new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            //Adding to wordlist
            wordList.add(word);
            //Adding to hash set
            wordSet.add(word);
            //Adding to hash map
            String key=sortLetters(word);
            if(lettersToWord.containsKey(key)){
                lettersToWord.get(key).add(word);
            }
            else
            {
                ArrayList<String> newWords=new ArrayList<>();
                newWords.add(word);
                lettersToWord.put(key,newWords);
            }
            //Adding to sizeToWords.. to get words with 'key' number of letters
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                sizeToWords.put(word.length(),temp);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {

        boolean status;
        status=(sortLetters(word).contains(sortLetters(base)) && wordSet.contains(word) && !word.contains(base));
        return status;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(String e:wordList){
            if((sortLetters(e).equals(sortLetters(targetWord)))&&(targetWord.length()==e.length())){
                result.add(e);
            }
        }
//        String key=sortLetters(targetWord);
//        result=lettersToWord.get(key);
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String alphabets="abcdefghijklmnopqrstuvwxyz";
        for(int i=0;i<alphabets.length();i++)
        {
            String withOneMoreLetter=word+alphabets.charAt(i);
            String key=sortLetters(withOneMoreLetter);
            if(lettersToWord.containsKey(key)){
                ArrayList<String> temp=new ArrayList<>();
                ArrayList<String> removeList=new ArrayList<>();
                temp=lettersToWord.get(key);
                for(String s:temp)
                {
                    //Remove the not Good words
                    if(!isGoodWord(s,word))
                    {
                        removeList.add(s);
                    }
                }
                temp.removeAll(removeList);//otherwise crash
                result.addAll(temp);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        while (true) {

            //Get all words with given wordLength
            ArrayList<String> tempList = sizeToWords.get(wordLength);

            //Get a random number between 0 and arraylist size
            int num = random.nextInt(tempList.size());

            //pick random word from the arraylist
            //String randomWord = wordList.get(num);
            String randomWord = tempList.get(num);

            //get all the anagrams with one more letter for that random word
            ArrayList<String> arrayList = (ArrayList<String>) getAnagramsWithOneMoreLetter(randomWord);

            //validate the conditions given

            //OPTMISATION : Returning words with more than min anagrams
            if ((randomWord.length() == wordLength) && arrayList.size() > MIN_NUM_ANAGRAMS) {

                //increment the wordLength for next stage
                if (wordLength < MAX_WORD_LENGTH) wordLength++;
                return randomWord;
            }
        }
    }

    public String sortLetters(String word)
    {
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        String sortedString = new String(charArray);
        return sortedString;
    }

}
