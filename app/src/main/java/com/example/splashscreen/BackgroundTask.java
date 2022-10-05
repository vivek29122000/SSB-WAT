package com.example.splashscreen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class BackgroundTask extends AsyncTask<Void, Void, Void> {

    Context c;
    Update u;

    BackgroundTask(Context c, Update u) {
        this.c = c;
        this.u = u;
    }

    @Override
    protected Void doInBackground(Void... voids) {

//        for(int i=0; )

        Random r = new Random();

        for(int i=1; i<=62; i++) {
            try {
                if(i == 21 || i == 42) {
                    u.up2();
                    Thread.sleep(15000);
                    continue;
                }

                int ran = r.nextInt(MainActivity.cur.size()-1);
                u.update(ran);

                Thread.sleep(15000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Toast.makeText(c, "test finished", Toast.LENGTH_SHORT).show();
        try {
            u.up1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
