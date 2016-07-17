package com.udacity.gradle.builditbiger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lan.samuel_dsldevice.jokedisplaylib.JokeDisplayActivity;
import com.udacity.gradle.builditbiger.backendLink.EndPointsAsyncTaskPaid;

public class MainActivityPaid extends ActionBarActivity {

    int spinnerStatus = 0;
    ProgressBar progressBar;
    private TextView txtView;
    private Handler spinnerHandler = new Handler();
    private boolean loadCompletionStatus = false;
    private Object loadCompletionStatusLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

        //spinner=(ProgressBar)findViewById(R.id.progressBar);
        //spinner.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
        txtView = (TextView)findViewById(R.id.txtProgress);
        txtView.setVisibility(View.GONE);

        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

        /*
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginLoadingJoke();
            }
        });

        requestNewInterstitial();
        */
    }

    @Override
    public void onStart(){
        super.onStart();
        //spinner.hide();
        progressBar.setVisibility(View.GONE);
        txtView.setVisibility(View.GONE);
    }


    /*
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view){

        //if (mInterstitialAd.isLoaded()) {
        //    mInterstitialAd.show();
        //} else {
            beginLoadingJoke();
        //}

    }

    private boolean getLoadCompletionStatus(){

        synchronized (loadCompletionStatusLock){
            return loadCompletionStatus;
        }
    }


    private void beginLoadingJoke(){
        new EndPointsAsyncTaskPaid(true).execute(new Pair<Context, String>(this, "fetch Joke"));

        //Start the progress animation and wait for the notification of the completion
        // to finalize the progress animation

        //Make progress spinner visible
        //spinner.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        txtView.setText("0%");
        txtView.setVisibility(View.VISIBLE);

        final int spinnerIncrement = 1;
        final int spinnerIncrementPostLoad = 10;
        spinnerStatus = 0;
        //spinner

        //Start the thread that will animate the spinner
        new Thread(new Runnable() {
            public void run() {
                while (spinnerStatus < 100) {

                    // process some tasks
                    //spinnerStatus = doSomeTasks();

                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(getLoadCompletionStatus()){
                        spinnerStatus += spinnerIncrementPostLoad;
                    }
                    else if(spinnerStatus >= 85){
                        spinnerStatus -= (int)((Math.random()*10) + 10);
                    }
                    else{
                        spinnerStatus += spinnerIncrement;
                    }


                    // Update the progress bar
                    spinnerHandler.post(new Runnable() {
                        public void run() {
                            if(spinnerStatus > 100)
                                spinnerStatus = 100;
                            //spinner.setProgress(spinnerStatus);
                            progressBar.setProgress(spinnerStatus);
                            txtView.setText(spinnerStatus + "%");
                        }
                    });

                }

                // ok, joke is downloaded,
                if (spinnerStatus >= 100) {

                    // sleep 1 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // close the progress bar dialog
                    //spinner.dismiss();
                }

                //notify the completion of the progress animation
                synchronized (loadCompletionStatusLock){
                    loadCompletionStatusLock.notify();
                }
            }
        }).start();

    }


    public void displayJoke(String joke){
        //This method should cooperate to end the progress animation
        //By notifying that the joke loading operation is completed
        //And waiting to be notified back when the progress animation is completed
        synchronized (loadCompletionStatusLock) {
            loadCompletionStatus = true;
            try {
                loadCompletionStatusLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        loadCompletionStatus = false;
        Intent intent = new Intent(this, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_EXTRA, joke);
        startActivity(intent);
    }



}
