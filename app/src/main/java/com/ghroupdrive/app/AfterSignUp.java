package com.ghroupdrive.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matiyas on 8/9/16.
 */
public class AfterSignUp extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout rlIhave,rlIDont,rlVerify;
    RippleView rpIhave,rpIDont,rpVerify,rpUpload;
    TextView tvIhave,tvIdont;
    RadioButton rbIHave,rbIDont;
    boolean iHave = true;
    TextView tvName;
    String fullName = "", phone ="";
    int userCode;
    String profileUrl = "";
    ImageView ivProfile;
    String image_path2 ="";
     LinearLayout llUpload;
    private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 1;
    Uri selectedImageUri;
    UserFunctions functions;
    String sendtsurl = "";
    ProgressDialog pDIalogi;
    ProgressBar pbBar;
    Boolean loading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aftersignup);
        rlIhave = (RelativeLayout) findViewById(R.id.rlIhave);
        rlVerify = (RelativeLayout) findViewById(R.id.rlVerify);
        rlIDont = (RelativeLayout) findViewById(R.id.rlIDont);
        rpIhave = (RippleView) findViewById(R.id.rpIhave);
        rpUpload = (RippleView) findViewById(R.id.rpUpload);
        rpIDont = (RippleView) findViewById(R.id.rpIDont);
        rpVerify = (RippleView) findViewById(R.id.rpVerify);
        tvIhave = (TextView) findViewById(R.id.tvIhave);
        tvIdont = (TextView) findViewById(R.id.tvIdont);
        rbIHave = (RadioButton) findViewById(R.id.rbIHave);
        rbIDont = (RadioButton) findViewById(R.id.rbIDont);
        tvName = (TextView) findViewById(R.id.tvName);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        llUpload = (LinearLayout) findViewById(R.id.llUpload);
        pbBar = (ProgressBar) findViewById(R.id.pbBar);
        functions = new UserFunctions(this);



        rlIhave.setOnClickListener(this);
        rlIDont.setOnClickListener(this);
        rlVerify.setOnClickListener(this);
        llUpload.setOnClickListener(this);

        fullName = getIntent().getStringExtra(StaticVariables.FULLNAME);
        phone = getIntent().getStringExtra(StaticVariables.MOBILENUMBER);
        userCode = getIntent().getExtras().getInt(StaticVariables.CODE);
        tvName.setText(fullName);

        if(userCode == 302)
        {
            iHave = getIntent().getExtras().getBoolean(StaticVariables.OFFERRIDE);
            profileUrl = getIntent().getStringExtra(StaticVariables.PROFILE);;

            if(!iHave)
            {
                iHave = false;
                rbIHave.setChecked(false);
                rbIDont.setChecked(true);
            }


            if(!profileUrl.contentEquals(""))
            {

                sendtsurl = profileUrl;
                String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+profileUrl+".jpg";
                AQuery aq = new AQuery(this);
                ImageOptions op=new ImageOptions();
                op.fileCache = true;
                op.memCache=true;
                op.targetWidth = 0;
                op.fallback = R.drawable.uploadpicture;
                aq.id(ivProfile).image(url, op);

            }


        }


    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.rlIhave:
                rpIhave.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    if (!iHave) {
                        iHave = true;
                        rbIHave.setChecked(true);
                        rbIDont.setChecked(false);

                    }
                }
            });
                break;

            case R.id.rlIDont:
                rpIDont.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        if (iHave) {
                            iHave = false;
                            rbIHave.setChecked(false);
                            rbIDont.setChecked(true);

                        }
                    }
                });
                break;
            case R.id.rlVerify:
                rpVerify.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        register();
                    }
                });
                break;
            case R.id.llUpload:
                rpUpload.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        openImageIntent();
                    }
                });
                break;
        }
    }



    private void register()
    {
        if(!loading)
        {

            JsonObject json = new JsonObject();
            json.addProperty(StaticVariables.MOBILENUMBER,phone);
            json.addProperty(StaticVariables.FULLNAME,fullName);
            json.addProperty(StaticVariables.PROFILE,sendtsurl);
            json.addProperty(StaticVariables.OFFERRIDE,iHave);
            json.addProperty(StaticVariables.INVITECODE,getIntent().getStringExtra(StaticVariables.INVITECODE));
            json.addProperty(StaticVariables.IMEI,functions.getPhoneID());
            SendRegister(json);
        }else
        {
            functions.showMessage("Loading image please wait");
        }





    }





    private void SendRegister(JsonObject data)
    {

        System.out.println("bbbbbbbb: "+data.toString());
        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Registering ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + "Register")
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
                                    String message = functions.getJsonString(json, StaticVariables.MESSAGE);
                                    if (code == 200) {
                                        JSONObject data = functions.getJsonObject(json, StaticVariables.DATA);


                                        if (data != null) {
                                            functions.setPref(StaticVariables.ACCESSCODE, functions.getJsonString(data, StaticVariables.ACCESSCODE));
                                            functions.setPref(StaticVariables.HASLOGEDING, true);
                                            functions.setPref(StaticVariables.OFFERRIDE, iHave);
                                            functions.setPref(StaticVariables.MOBILENUMBER, phone);
                                            functions.setPref(StaticVariables.PROFILE, sendtsurl);
                                            functions.setPref(StaticVariables.FULLNAME, fullName);

                                            if (userCode == 302) {
                                                alreadyUser();
                                            } else {
                                                goToHome();

                                            }

                                        } else {
                                            functions.showMessage("User Registration failed");
                                        }

                                    } else {
                                        functions.showMessage(message);

                                    }


                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("User Registration failed");

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




    private void alreadyUser()
    {

        final Dialog credDialog=new Dialog(this);
        credDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        credDialog.setContentView(R.layout.double_signup);
        credDialog.setCancelable(false);
        RelativeLayout rlCancel = (RelativeLayout) credDialog.findViewById(R.id.rlCancel);
        final RippleView rpCancel = (RippleView) credDialog.findViewById(R.id.rpCancel);
        RelativeLayout rlProceed = (RelativeLayout) credDialog.findViewById(R.id.rlProceed);
        final RippleView rpProceed = (RippleView) credDialog.findViewById(R.id.rpProceed);


        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        credDialog.dismiss();
                        goToHome();
                    }
                });
            }
        });

        rlProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpProceed.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        credDialog.dismiss();
                        JsonObject json = new JsonObject();
                        json.addProperty(StaticVariables.MOBILENUMBER, phone);
                        json.addProperty(StaticVariables.IMEI, functions.getPhoneID());

                        migrateUser(json);
                    }
                });
            }
        });

        credDialog.show();
    }








    private void migrateUser(JsonObject data)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Migrating user data please wait ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + "MigrateDevice")
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
                                    String message = functions.getJsonString(json, StaticVariables.MESSAGE);
                                    if (code == 200) {

                                        goToHome();

                                    } else {
                                        functions.showMessage(message);
                                        alreadyUser();

                                    }


                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("Unable to migrate please try again later");

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
            alreadyUser();


        }


    }


    private void goToHome()
    {

        if(iHave)
        {
            //start the driver home
            startActivity(new Intent(this,DriverHome.class));
            finish();
        }else {
            //start the rider home
            startActivity(new Intent(this,RiderHome.class));
            finish();
        }
    }






    private void openImageIntent() {


        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //galleryIntent.setType("image/*");
        //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
               // ivProfile.setVisibility(View.VISIBLE);
                final boolean isCamera;
                if (data == null) {
                    System.out.println("--------------------------------- data equals null");
                    isCamera = true;
                }else if(data.getData() == null){
                    System.out.println("--------------------------------- getData is null here");
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    System.out.println("--------------------------------- action: " + action);
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.contentEquals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }


                if (isCamera) {
                    // System.out.println("--------------------------- camera here"+image_path2);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = functions.getImageUri(photo);
                    //photo=rotate3(currImageURI);

                    image_path2 = functions.getRealPathFromURI(selectedImageUri);

                    //setCapturedImage(image_path2);
                    new SendImage().execute();
                } else {


                    /*System.out.println("--------------------------- gallery "+image_path2);
                    selectedImageUri = data == null ? null : data.getData();
                    image_path2 = functions.getRealPathFromURI(selectedImageUri);
                    */

                    selectedImageUri=data.getData();
                    image_path2 = functions.getRealPathFromURI(selectedImageUri);
                   // ivProfile.setImageURI(selectedImageUri);
                    new SendImage().execute();
                }
                // Bitmap bitmap = functions.getImageFromUri(selectedImageUri);
                //ivProfile.setImageBitmap(bitmap);


            }
        }
    }














    class SendImage extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            pbBar.setVisibility(View.VISIBLE);

        }

        /**
         * Creating product
         * */
        @Override
        protected String doInBackground(String... args) {


            Map config = new HashMap();
            config.put("cloud_name", StaticVariables.CLOUNDNAME);
            config.put("api_key", StaticVariables.APIKEY);
            config.put("api_secret", StaticVariables.APPSECRET);
            Cloudinary cloudinary = new Cloudinary(config);
            try {
                InputStream is;
                is = new FileInputStream(image_path2);
                Map uploadResult = cloudinary.uploader().upload(is, ObjectUtils.emptyMap());

                if(uploadResult!= null)
                {
                    sendtsurl = "v"+uploadResult.get(StaticVariables.VERSION).toString()+"/"+uploadResult.get(StaticVariables.PUBLICID).toString();
                    System.out.println("bbbbbbbbbb: "+sendtsurl);
                    is.close();
                    return "1";
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }





            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            loading = false;
            pbBar.setVisibility(View.GONE);
            if(file_url != null)
            {

                functions.setCapturedImage(image_path2,ivProfile);
            }else {
                functions.showMessage("unable to upload image");
            }


        }





    }





}
