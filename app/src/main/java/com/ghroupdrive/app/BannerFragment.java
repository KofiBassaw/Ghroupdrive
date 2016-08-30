package com.ghroupdrive.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matiyas on 8/11/16.
 */
public class BannerFragment extends Fragment implements View.OnClickListener {


    String images[] = new String[2];
    ImageView ivBannerImage;
    int position =0;

UserFunctions functions;
    ProgressBar pbBar;
    private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 1;
    Uri selectedImageUri;
    String image_path2 ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.banner_image_item, container, false);
        ivBannerImage = (ImageView) theLayout.findViewById(R.id.ivBannerImage);
        pbBar = (ProgressBar) theLayout.findViewById(R.id.pbBar);
        functions = new UserFunctions(getActivity());

        images[0] = functions.getPref(StaticVariables.PROFILE,"");
        images[1] = functions.getPref(StaticVariables.PROFILEBANNER,"");


        position = getArguments().getInt("pos");

        String url = StaticVariables.CLOUDORDINARYURL+"w_300,h_300/"+images[position]+".jpg";
        AQuery aq = new AQuery(getActivity());
        ImageOptions op=new ImageOptions();
        op.fileCache = true;
        op.memCache=true;
        op.targetWidth = 0;
        op.fallback = R.drawable.uploadpicture;
        aq.id(ivBannerImage).image(url, op);


        ivBannerImage.setOnClickListener(this);
        return  theLayout;


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.ivBannerImage:

                if(position == 1)
                {
                    openImageIntent();
                }
                break;
        }
    }







    private void openImageIntent() {


        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
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


                pbBar.setVisibility(View.VISIBLE);
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
            /*
            loading = true;
            pbBar.setVisibility(View.VISIBLE);
            */
            functions.showMessage("Updating profile....");
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
                    String sendtsurl = "v"+uploadResult.get(StaticVariables.VERSION).toString()+"/"+uploadResult.get(StaticVariables.PUBLICID).toString();
                    System.out.println("bbbbbbbbbb: "+sendtsurl);
                    is.close();
                    return sendtsurl;
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
            //loading = false;
            // pbBar.setVisibility(View.GONE);
            if(file_url != null)
            {

                JsonObject json = new JsonObject();
                json.addProperty(StaticVariables.ACCESSCODE,functions.getPref(StaticVariables.ACCESSCODE,""));
                json.addProperty(StaticVariables.OFFERRIDE,functions.getPref(StaticVariables.OFFERRIDE,false));
                json.addProperty(StaticVariables.IMEI,functions.getPhoneID());


                    json.addProperty(StaticVariables.PROFILEBANNER,file_url);


                // functions.setCapturedImage(image_path2,ivProfile);
                uploadImage(json, file_url);
            }else {
                pbBar.setVisibility(View.GONE);
                functions.showMessage("unable to upload image");
            }


        }





    }






    private void uploadImage(JsonObject data, final String profile)
    {


        ConnectionDetector cd=new ConnectionDetector(getActivity());
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("PUT", StaticVariables.BASEURL + "Profile")
                    .setJsonObjectBody(data)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                pbBar.setVisibility(View.GONE);
                                if (e != null) {
                                    e.printStackTrace();
                                    //System.out.println("---------------------------------- error");
                                }
                                System.out.println("bbbbbb: " + result);
                                if (result != null) {
                                    JSONObject json = new JSONObject(result);
                                    int code = json.getInt(StaticVariables.CODE);
                                    String message = functions.getJsonString(json, StaticVariables.MESSAGE);
                                    if (code == 200) {


                                            functions.setCapturedImage(image_path2, ivBannerImage);
                                            functions.setPref(StaticVariables.PROFILEBANNER,profile);




                                    } else {
                                        functions.showMessage(message);


                                    }


                                } else {
                                    functions.showMessage("Unable to update profile");

                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });


        } else {
            Toast.makeText(getActivity(), "No internet Connection Please try again later", Toast.LENGTH_LONG).show();



        }


    }


}
