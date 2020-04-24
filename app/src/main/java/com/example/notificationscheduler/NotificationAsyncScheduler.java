package com.example.notificationscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

public class NotificationAsyncScheduler extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        ServiceAsyncTask serviceAsyncTask = new ServiceAsyncTask();

        serviceAsyncTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        jobFinished(params, false);
        return true;
    }

    static class ServiceAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
