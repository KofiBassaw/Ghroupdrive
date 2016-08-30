package com.ghroupdrive.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by matiyas on 8/8/16.
 */
public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        Thread aa=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                    startActivity(new Intent(SplashScreen.this,WelcomeScreen.class));
                    finish();

                }
                super.run();
            }
        };

        aa.start();
    }
}
