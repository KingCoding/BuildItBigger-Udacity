package com.udacity.gradle.builditbiger.backendLink;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.owner.jokesapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbiger.MainActivityFree;

import java.io.IOException;

/**
 * Created by owner on 28/06/2016.
 */
public class EndPointsAsyncTaskFree extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private Object lock;
    private boolean executionCompleted;
    String executionResult;
    boolean executionModeNotTest;

    public EndPointsAsyncTaskFree(boolean execMode){
        lock = new Object();
        executionCompleted = false;
        executionResult = null;
        executionModeNotTest = execMode;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            /*
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
           */

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://jokesbackend-1360.appspot.com/_ah/api/");
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String jokeRequest = params[0].second;

        try {
            return myApiService.getJoke(jokeRequest).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        synchronized (lock){

            executionCompleted = true;
            executionResult = joke;
            lock.notify();
        }

        if(executionModeNotTest)
            ((MainActivityFree)context).displayJoke(joke);
    }

    public String getResultAfterExecution(){
        String result = null;

        synchronized (lock){

            while(!executionCompleted){

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            result = executionResult;
        }

        return result;
    }
}