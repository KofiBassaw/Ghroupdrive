package com.ghroupdrive.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

/**
 * Created by matiyas on 8/8/16.
 */
public class WelcomeScreen extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private static final int NUM_PAGES = 4;
    public static NonSwippablePager mPager;
    private PagerAdapter mPagerAdapter;
    RelativeLayout rlLogin,rlFacebook;
    RippleView rpLogin,rpFacebook;
    int pos = 0;
    MySurfaceView surfaceViewFrame;

    int[] images = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4};

    MediaPlayer player;
    private SurfaceHolder holder;
    UserFunctions functions;
    ProgressDialog pDIalogi;

    private static final int REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        rlFacebook = (RelativeLayout) findViewById(R.id.rlFacebook);
        rpLogin = (RippleView) findViewById(R.id.rpLogin);
        rpFacebook = (RippleView) findViewById(R.id.rpFacebook);
        functions = new UserFunctions(this);
       // imageView = (ImageView) findViewById(R.id.ivAnimation);

        mPager = (NonSwippablePager) findViewById(R.id.pager);
        mPager.setScrollDurationFactor(4);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        rlLogin.setOnClickListener(this);
        rlFacebook.setOnClickListener(this);
        animat();





        try{
            surfaceViewFrame = (MySurfaceView) findViewById(R.id.vvSplashVideo);
            // setContentView(videoHolder);
            player = new MediaPlayer();
            holder = surfaceViewFrame.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            player = new MediaPlayer();
            player.setOnPreparedListener(this);
            // player.setOnCompletionListener(this);

/*
            DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoHolder.getLayoutParams();
            params.width =  metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoHolder.setLayoutParams(params);

            Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                    + R.raw.picvideo);
            videoHolder.setVideoURI(video);

            videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {

                }

            });
            videoHolder.start();


            */

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }




        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }

    }


    private void animat()
    {

        /*
        final Animation anim_out = AnimationUtils.loadAnimation(WelcomeScreen.this, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(WelcomeScreen.this, android.R.anim.fade_in);

        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ////////////////////////////////////////
                // HERE YOU CHANGE YOUR IMAGE CONTENT //
                ////////////////////////////////////////
                //ui_image.setImage...
                System.out.println("-------------------------- bbbbbbbbbbbbbbbbbbb");

                if (pos == 3)
                    pos = 0;
                else {
                    pos++;
                }

                imageView.setImageResource(images[pos]);

                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Thread td = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(1000 * 3);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    imageView.startAnimation(anim_out);
                                }
                            }
                        };


                    }
                });

                imageView.startAnimation(anim_in);
            }
        });

        imageView.startAnimation(anim_out);

        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlLogin:
                rpLogin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        showCodeDialog(1);
                    }
                });
                break;
            case R.id.rlFacebook:
                rpFacebook.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        showCodeDialog(2);
                    }
                });
                break;
        }
    }


    private void showCodeDialog(final int type)
    {



        final Dialog credDialog=new Dialog(this);
        credDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        credDialog.setContentView(R.layout.invite_code_dialog);

        RelativeLayout rlContinue = (RelativeLayout) credDialog.findViewById(R.id.rlContinue);
        final RippleView rpContine = (RippleView) credDialog.findViewById(R.id.rpContinue);
        final TextView tvContinue = (TextView) credDialog.findViewById(R.id.tvContinue);
        final EditText etInviteCode = (EditText) credDialog.findViewById(R.id.etInviteCode);


        if(type == 1)
        {
            //sign up clicke
           // rlContinue.setBackgroundColor(Color.parseColor("#1fade0"));
            //tvContinue.setText("Continue");
        }else
        {
            //facebook clicked
            //rlContinue.setBackgroundColor(Color.parseColor("#2753ad"));
           // tvContinue.setText("Continue with facebook");
        }

        rlContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpContine.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        if(type == 1)
                        {
                            /*
                            startActivity(new Intent(WelcomeScreen.this,SignUp.class));
                            finish();
                            */
                            String inviteCode = etInviteCode.getText().toString();

                            if(!inviteCode.contentEquals(""))
                            {
                                //call the verify function
                                credDialog.dismiss();
                                JsonObject json = new JsonObject();
                                json.addProperty(StaticVariables.CODE,inviteCode);
                                verifyCode(json,type);



                            }else
                            {
                                etInviteCode.setError("Please enter your invitation code");
                            }



                        }else {
                            //facebook
                            /*
                            startActivity(new Intent(WelcomeScreen.this,AfterSignUp.class));
                            finish();
                            */
                        }
                    }
                });
            }
        });


        credDialog.show();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new SliderOne();

                case 1:
                    return new SliderTwo();
                case 2:
                    return new SliderThree();
                case 3:
                    return new SliderFour();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }




    public static class SliderOne extends Fragment {


        TextView tvText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.slider_layout , container, false);

            return theLayout;

        }
    }

    public static class SliderTwo extends Fragment {


        TextView tvText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.slider_layout , container, false);

            return theLayout;

        }
    }


 public static class SliderThree extends Fragment {


        TextView tvText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.slider_layout , container, false);

            return theLayout;

        }
    }

 public static class SliderFour extends Fragment {


        TextView tvText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.slider_layout , container, false);

            return theLayout;

        }
    }




    private void playVideo() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                            + R.raw.loginvideo);
                    player.setDataSource(WelcomeScreen.this,video);
                    //player.setDataSource(video);
                    player.prepare();
                    // player.start();
                } catch (Exception e) { // I can split the exceptions to get which error i need.
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        //slide =false;
        player.pause();
        super.onPause();
    }



    @Override
    protected void onResume() {
       // slide = true;
        if(!player.isPlaying()){
            player.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
       // mainslide = false;
        player.stop();
        player.release();
        super.onDestroy();
    }




    @Override
    public void onPrepared(MediaPlayer mp) {
        System.out.println("------------------------ prepared");
        mp.setLooping(true);
        player.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);


        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        android.view.ViewGroup.LayoutParams lp = surfaceViewFrame.getLayoutParams();

        System.out.println("+++++++++++++++++++++ 1 Screen width: " + screenWidth + " and Height: " + screenHeight);
        if (videoProportion > screenProportion) {
            System.out.println("+++++++++++++++++++++ 1 Video width: "+videoWidth+" and Height: "+videoHeight);
            lp.width = screenWidth * 2;
            lp.height = videoHeight*2;

        } else {
            System.out.println("+++++++++++++++++++++ 2");
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        surfaceViewFrame.setLayoutParams(lp);




        if (!player.isPlaying()) {
            System.out.println("--------------------------------- start to play");
            player.start();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.setScreenOnWhilePlaying(true);

        playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


    }


    private void verifyCode(JsonObject data, final int type)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Verifying ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + "VerifyInvite")
                    .setJsonObjectBody(data)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    //System.out.println("---------------------------------- error");
                                }
                                System.out.println("bbbbbb: " + result);
                                pDIalogi.dismiss();
                                if (result != null) {
                                    JSONObject json = new JSONObject(result);
                                      int code = json.getInt(StaticVariables.CODE);
                                    String message = functions.getJsonString(json,StaticVariables.MESSAGE);
                                    if(code ==  200)
                                    {
                                        JSONObject data = functions.getJsonObject(json,StaticVariables.DATA);

                                        if(data != null)
                                        {
                                            boolean IsUsed = data.getBoolean(StaticVariables.ISSUED);
                                            if(!IsUsed)
                                            {
                                                //start activity here
                                                if(type == 1)
                                                {
                                                    Intent it = new Intent(WelcomeScreen.this,SignUp.class);
                                                    it.putExtra(StaticVariables.TYPE,type);
                                                    it.putExtra(StaticVariables.INVITECODE,functions.getJsonString(data,StaticVariables.INVITECODE));
                                                    functions.setPref(StaticVariables.HASINVITED,true);
                                                    startActivity(it);
                                                    finish();
                                                }else
                                                {
                                                    //call the facebook activity here
                                                }
                                            }else {
                                                functions.showMessage("Code has been already used");
                                            }


                                        }else
                                        {
                                            functions.showMessage("Unable to verify invite please tray again later");
                                        }

                                    }else {
                                        functions.showMessage(message);

                                    }




                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("Unable to verify invite please tray again later");

                                }

                            } catch (Exception ex) {
                                pDIalogi.dismiss();
                                ex.printStackTrace();
                            }
                        }
                    });


        } else {
            pDIalogi.dismiss();
            Toast.makeText(this, "No internet Connection Please try again later", Toast.LENGTH_LONG).show();


        }



    }







    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO

                }else {
                   // finish();
                }
                break;

            default:
                break;
        }
    }

}
