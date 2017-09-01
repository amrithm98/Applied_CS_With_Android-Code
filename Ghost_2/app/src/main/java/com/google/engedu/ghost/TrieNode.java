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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {

        //Add each string to the trie from FastDictionary
        TrieNode trie=this;
        for(int i=0;i<s.length();i++)
        {
            String str=String.valueOf(s.charAt(i));
            //If there's no child 'str' for the current node--> Create a new node with str
            if(trie.children.containsKey(str)==false)
            {
                trie.children.put(str,new TrieNode());
            }
            //Update node to be the recent child node When the child exists
            trie=trie.children.get(str);
        }
        //Keep an identifier at the end of each word
        trie.isWord=true;
    }

    public boolean isWord(String s) {

        TrieNode trie=this;
        for(int i=0;i<s.length();i++)
        {
            String str=String.valueOf(s.charAt(i));
            //Suppose the string is help
            //First checks for node h, if it exists, update current node to h
            //Then checks for e from h, if it exists , update current node to e
            if(trie.children.containsKey(str)==false)
            {
                return false;
            }
            trie=trie.children.get(str);

        }
        //Instead of true we return this because the search may end at an incomplete word
        //Suppose the word given was 'hel', since nodes h->e->l are present, it doesn't return true
        //Without node 'p'
        //Hence we return trie.isWord
        return trie.isWord;
    }

    public String getAnyWordStartingWith(String s) {

        TrieNode trie=this;
        String word="";
        char alpha[]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        //Just to shuffle the array so that we dont get the same word every time given a starting letter
        alpha=shuffleArray(alpha);
        if(s.equalsIgnoreCase(""))
        {
            Random random=new Random();
            int index=random.nextInt(26);
            String startingWord=getAnyWordStartingWith(String.valueOf(alpha[index]));
            return startingWord;

        }

        for(int i=0;i<s.length();i++)
        {
            String str=String.valueOf(s.charAt(i));
            if(trie.children.containsKey(str)==false)
            {
                return "";
            }
            word+=str;
            trie=trie.children.get(str);
        }

        //A do-while loop is needed. Suppose the word that we get is "help"
        //We go through the inner loop alone and get 'e' which makes 'helpe'
        //When we check isWord, we find that this is not a word eventhough its present in the trie because of the word 'helper'
        //We repeat the search with the do while loop and findout 'helps'
        do {

            for(char ch: alpha)
            {
                String str=String.valueOf(ch);
                if(trie.children.containsKey(str))
                {
                    word+=str;
                    Log.d("wordmaking",word);
                    //We're updating this node to check if the identifier value is true
                    //If isWord is true at the updated point,then only it's a valid word

                    trie=trie.children.get(str);
                }
            }

        } while(trie.isWord==false);
        Log.d("wordfound",word);

        return word;
    }

    public static char[] shuffleArray(char[] array)
    {
        int index;
        char temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
            Log.d("Shuffling",String.valueOf(array[i]));
        }
        return array;
    }
    //Not Working Yet
    public void traversal(TrieNode t,String s,ArrayList<String> words)
    {
        if(t.isWord && t.children.isEmpty())
        {
            words.add(s);
        }
        else
        {
            if(t.isWord)
                words.add(s);
            else
            {
                for(String key: t.children.keySet())
                {
                    String tmp=s+key;
                    traversal(t.children.get(key),tmp,words);
                }
            }
        }

    }
    //Not Working Yet
    public String getGoodWordStartingWith(String s) {
        TrieNode trie=this;
        String word="";
        char alpha[]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        //Just to shuffle the array so that we dont get the same word every time given a starting letter
        alpha=shuffleArray(alpha);
        Random random=new Random();
        for(int i=0;i<s.length();i++)
        {
            String str=String.valueOf(s.charAt(i));
            if(trie.children.containsKey(str)==false)
            {
                return "";
            }
            word+=str;
            trie=trie.children.get(str);
        }

        ArrayList<String> notValidWords=new ArrayList<>();
        ArrayList<String> ValidWords=new ArrayList<>();
        for(String a: this.children.keySet())
        {
            if(!this.isWord)
                notValidWords.add(s);

        }
        if(notValidWords.isEmpty())
        {
            traversal(trie,s,ValidWords);
        }
        else
        {
            int index=random.nextInt(notValidWords.size());
            String key=notValidWords.get(index);
            s+=key;
            traversal(trie.children.get(key),s,ValidWords);
        }
        int index=random.nextInt(ValidWords.size());
        word=ValidWords.get(index);
        return word;
    }
}
