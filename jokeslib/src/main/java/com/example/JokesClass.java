package com.example;

public class JokesClass {

    String[] jokes;
    public JokesClass(){

        jokes = new String[10];

        for(int i=0;i<10; i++){
            jokes[i] = "joke-"+(i+1);
        }
    }

    public String getJoke(){

        if(jokes!= null && jokes.length>0)
            return jokes[(int)(Math.random()*10)];
        else
            return "";
    }

}
