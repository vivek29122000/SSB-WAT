package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static LinkedList<String> cur, old, tmp;
    static TextView tv;
    int pr = 0;
    BackgroundTask task;
    AudioManager am;
    MediaPlayer audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("word_list", MODE_PRIVATE);
        int temp = prefs.getInt("first_use", 1);
        SharedPreferences.Editor edit = prefs.edit();

        Log.i("----------------prefs----------", temp+"");
        if(temp == 1) {
            edit.putInt("first_use", 0);
            edit.commit();
            try {
                createFile(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        try {
//            createFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    getData();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SECURE);

        tv = findViewById(R.id.wordtv);

//        String[] s  = Data.data.split("\n");
//        cur.addAll(Arrays.asList(s));


        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/3)<<1, AudioManager.FLAG_PLAY_SOUND);
        audio = MediaPlayer.create(this, R.raw.chime);
        audio.start();

        task = new BackgroundTask(this, new Update() {
            @Override
            public void update(int i) {
                audio.start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(cur.get(i).split(":")[0]);
                        old.add(cur.get(i));
                        tmp.add(cur.get(i));
                        cur.remove(i);
                    }
                });
            }

            @Override
            public void up1() throws IOException {
                saveFile();
                Data.tmp = tmp;
                audio.release();

                startActivity(new Intent(MainActivity.this, AfterTestActivity.class));
                finish();
                finishAffinity();

            }

            @Override
            public void up2() throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audio.start();
                        tv.setText(" ");
                    }
                });
            }
        });

        task.execute();

    }

    public void createFile(boolean first) throws IOException {
        File root = this.getFilesDir();
        File f1 = new File(root, "f1.txt");
        if(first) f1.createNewFile();

        File f2 = new File(root, "f2.txt");
        if(first) f2.createNewFile();

        FileWriter writer = new FileWriter(f1);
        writer.write(Data.data);
        writer.flush();
        writer.close();
        Log.i("-----root------", root.getAbsolutePath());
    }

    public void getData() throws IOException {
        File f1 = new File(this.getFilesDir(),"f1.txt");
        BufferedReader br = new BufferedReader(new FileReader(f1));
        StringBuilder sb = new StringBuilder();

        cur = new LinkedList<>();
        old = new LinkedList<>();
        tmp = new LinkedList<>();

        String s = "";
        while((s = br.readLine()) != null) {
            sb.append(s + "\n");
        }
        cur.addAll(Arrays.asList(sb.toString().split("\n")));
        if(cur.size() < 60) {
            createFile(false);
            getData();
            return;
        }

        File f2 = new File(this.getFilesDir(),"f2.txt");
        br = new BufferedReader(new FileReader(f2));
        StringBuilder sb2 = new StringBuilder();

        while((s = br.readLine()) != null) {
            sb2.append(s + "\n");
        }
        old.addAll(Arrays.asList(sb2.toString().split("\n")));
    }

    public void saveFile() throws IOException {
        File f1 = new File(this.getFilesDir(),"f1.txt");
        File f2 = new File(this.getFilesDir(),"f2.txt");

        FileWriter f1w = new FileWriter(f1);
        for(String k: cur) f1w.write(k+"\n");
        f1w.flush();

        FileWriter f2w = new FileWriter(f2);
        for(String k: old) f2w.write(k+"\n");
        f2w.flush();

    }

    @Override
    protected void onPause() {
        super.onPause();
        pr = 5;
        onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(pr < 1) Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
        else {
            task.cancel(true);
            finish();
            finishAffinity();
            audio.release();
        }
        pr++;
    }
}