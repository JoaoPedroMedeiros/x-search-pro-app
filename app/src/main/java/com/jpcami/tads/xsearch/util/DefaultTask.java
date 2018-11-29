package com.jpcami.tads.xsearch.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

public abstract class DefaultTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Exception e;

    protected Context context;

    public DefaultTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (e != null) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        else {
            onFinish(result);
        }
    }

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return executeTask(params);
        }
        catch (IOException e) {
            this.e = e;
            return null;
        }
    }

    protected abstract Result executeTask(Params... params) throws IOException;

    protected void onFinish(Result result) {
    }
}
