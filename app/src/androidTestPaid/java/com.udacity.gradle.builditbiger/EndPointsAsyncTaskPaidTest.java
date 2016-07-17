package com.udacity.gradle.builditbiger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.AndroidTestCase;

import com.udacity.gradle.builditbiger.backendLink.EndPointsAsyncTaskPaid;

/**
 * Created by owner on 01/07/2016.
 */
public class EndPointsAsyncTaskPaidTest extends AndroidTestCase {

    public void testVerifyEndPointsAsyncTaskResponse(){

        EndPointsAsyncTaskPaid endPointsAsyncTask = new EndPointsAsyncTaskPaid(false);
        endPointsAsyncTask.execute(new Pair<Context, String>(getContext(), "fetch Joke"));
        String resultAfterExecution = endPointsAsyncTask.getResultAfterExecution();
        assertNotNull(resultAfterExecution);
    }
}
