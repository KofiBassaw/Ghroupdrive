package com.ghroupdrive.app;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
public class SignUp extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rlVerify;
    RippleView rpVerify;
    UserFunctions functions;
    EditText etFullName,etPhoneNumber;
    String name="",phone="";
    String receivedCode = "";
    String verifyCode = "";
    ProgressDialog pDIalogi;
    private int userCode = 0;
    String profileUrl = "";
    Boolean offerRide = false;
    Dialog credDialog;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    String inviteCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        rlVerify = (RelativeLayout) findViewById(R.id.rlVerify);
        rpVerify = (RippleView) findViewById(R.id.rpVerify);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        functions = new UserFunctions(this);
        inviteCode = getIntent().getStringExtra(StaticVariables.INVITECODE);




        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }


        rlVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rlVerify:
                rpVerify.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        /*
                        startActivity(new Intent(SignUp.this,AfterSignUp.class));
                        finish();
                        */
                        vaidateData();
                    }
                });
                break;
        }
    }


    private void vaidateData()
    {
        phone = functions.getText(etPhoneNumber);
        name = functions.getText(etFullName);

        if(name.contentEquals(""))
        {
            etFullName.setError("Please enter yoir phone number");
            etFullName.requestFocus();
        }else if (phone.contentEquals(""))
        {
            etPhoneNumber.setError("Please enetr your phone number");
            etPhoneNumber.requestFocus();
        }else
        {
            JsonObject json = new JsonObject();
            json.addProperty(StaticVariables.MOBILENUMBER,phone);
            json.addProperty(StaticVariables.IMEI,functions.getPhoneID());
            veryfyMobile(json);

        }

    }








    @Override
    protected void onResume() {
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                StaticVariables.SMSRECEIVED));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
            Log.e("rror", "> " + e.getMessage());
        }
        super.onDestroy();
    }




    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String message = intent.getStringExtra(StaticVariables.MESSAGE);
            receivedCode  = message;
            if(!verifyCode.contentEquals(""))
            {
                if(verifyCode.toLowerCase().contentEquals(message.toLowerCase()))
                {
                    if(credDialog!=null)
                    {
                        if(credDialog.isShowing())
                        {
                            credDialog.dismiss();
                        }
                    }
                    functions.setPref(StaticVariables.HASVERIFIED, true);
                    goToNextScreen();

                }
            }



        }
    };




    private void goToNextScreen()
    {
        Intent it = new Intent(SignUp.this, AfterSignUp.class);
        it.putExtra(StaticVariables.FULLNAME, name);
        it.putExtra(StaticVariables.MOBILENUMBER, phone);
        it.putExtra(StaticVariables.CODE, userCode);
        it.putExtra(StaticVariables.INVITECODE, inviteCode);
        if(userCode == 302)
        {
            it.putExtra(StaticVariables.OFFERRIDE,offerRide);
            it.putExtra(StaticVariables.PROFILE,profileUrl);
        }
        startActivity(it);
        finish();
    }


    private void veryfyMobile(JsonObject data)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Verifying ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + "VerifyMobile")
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
                                    userCode = code;
                                    if(code ==  200)
                                    {
                                        JSONObject data = functions.getJsonObject(json,StaticVariables.DATA);


                                        if(data != null)
                                        {
                                            verifyCode = functions.getJsonString(data,StaticVariables.VERIFICATIONCODE);

                                           if(!receivedCode.contentEquals("") && verifyCode.toLowerCase().contentEquals(receivedCode))
                                           {
                                               goToNextScreen();
                                           }else{
                                               showVerification();
                                           }

                                        }else
                                        {
                                            functions.showMessage("Unable to verify invite please tray again later");
                                        }

                                    }else if(code==302){


                                        JSONObject data = functions.getJsonObject(json,StaticVariables.DATA);


                                        if(data != null)
                                        {
                                            verifyCode = functions.getJsonString(data,StaticVariables.VERIFICATIONCODE);
                                            profileUrl = functions.getJsonString(data,StaticVariables.PROFILE);
                                            offerRide = data.getBoolean(StaticVariables.OFFERRIDE);

                                            if(!receivedCode.contentEquals("") && verifyCode.toLowerCase().contentEquals(receivedCode))
                                            {
                                                goToNextScreen();
                                            }else{
                                                showVerification();
                                            }

                                        }else
                                        {
                                            functions.showMessage("Unable to send data please try again");
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










    private void showVerification()
    {



          credDialog=new Dialog(this);
        credDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        credDialog.setContentView(R.layout.invite_code_dialog);
        credDialog.setCancelable(false);
        RelativeLayout rlContinue = (RelativeLayout) credDialog.findViewById(R.id.rlContinue);
        final RippleView rpContine = (RippleView) credDialog.findViewById(R.id.rpContinue);
        final TextView tvContinue = (TextView) credDialog.findViewById(R.id.tvContinue);
        final TextView tvInviteCode = (TextView) credDialog.findViewById(R.id.tvInviteCode);
        final TextView tvMessage = (TextView) credDialog.findViewById(R.id.tvMessage);
        final EditText etInviteCode = (EditText) credDialog.findViewById(R.id.etInviteCode);
         TextView tvPhone = (TextView) credDialog.findViewById(R.id.tvPhone);
         View vLine = (View) credDialog.findViewById(R.id.vLine);


        tvPhone.setVisibility(View.VISIBLE);
        vLine.setVisibility(View.VISIBLE);
        tvInviteCode.setText("Check Your Inbox");
        tvMessage.setText("We have just texted a 5 digit number to");
        etInviteCode.setHint("enter verification code");
        tvPhone.setText(phone);


        tvContinue.setText("VERIFY");
        rlContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpContine.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                        String inviteCode = etInviteCode.getText().toString();

                        if (!inviteCode.contentEquals("")) {
                            //call the verify function
                            if (inviteCode.toLowerCase().contentEquals(verifyCode.toLowerCase())) {
                                functions.setPref(StaticVariables.HASVERIFIED, true);
                                credDialog.dismiss();
                                goToNextScreen();

                            } else {
                                etInviteCode.setError("invalid verification code");
                            }


                        } else {
                            etInviteCode.setError("Please enter your invitation code");
                        }


                    }
                });
            }
        });


        credDialog.show();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }else {
                    finish();
                }
                break;

            default:
                break;
        }
    }
}
