package com.udacity.gradle.builditbiger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.AndroidTestCase;

import com.udacity.gradle.builditbiger.backendLink.EndPointsAsyncTaskFree;

/**
 * Created by owner on 01/07/2016.
 */
public class EndPointsAsyncTaskFreeTest extends AndroidTestCase {

    public void testVerifyEndPointsAsyncTaskResponse(){

        EndPointsAsyncTaskFree endPointsAsyncTask = new EndPointsAsyncTaskFree(false);
        endPointsAsyncTask.execute(new Pair<Context, String>(getContext(), "fetch Joke"));
        String resultAfterExecution = endPointsAsyncTask.getResultAfterExecution();
        assertNotNull(resultAfterExecution);
    }
}
