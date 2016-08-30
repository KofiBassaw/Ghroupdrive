package com.ghroupdrive.app;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matiyas on 8/9/16.
 */
public class RiderProfileFragement extends Fragment implements View.OnClickListener {

ViewPager vpPager;
ViewPager vpPager2;
    private static final int NUM_PAGES = 4;
    private PagerAdapter mPagerAdapter;
    private BannerAdapter mPagerAdapter2;
    ImageView iview[] = new ImageView[2];
    Uri selectedImageUri;


    private static final int BANNERNUM = 2;
    String images[];

    ImageView ivProfile;
    boolean change = true;
    private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 1;
    private static final int GALLERY = 2;


    RelativeLayout rlInvite;
    RippleView rpInvite;
    int selectedPos = 0;
    Spinner spUniversities;
    String[]universities;
    LinearLayout llUploadContainer;
    RelativeLayout llPagerContainer;
    UserFunctions functions;
    String profileBanner ="";
    String image_path2 ="";
    ProgressBar pbBarImage;
    ProgressBar rlPbarGallery;
    RelativeLayout rlGallery;
    RippleView rpGallery;
    TextView tvName,tvInviteRem;
    ProgressDialog pDIalogi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.rider_profile, container, false);
        vpPager = (ViewPager) theLayout.findViewById(R.id.vpPager);
        vpPager2 = (ViewPager) theLayout.findViewById(R.id.vpPager2);
        ivProfile = (ImageView) theLayout.findViewById(R.id.ivProfile);
        tvName = (TextView) theLayout.findViewById(R.id.tvName);
        tvInviteRem = (TextView) theLayout.findViewById(R.id.tvInviteRem);
        iview[0] = (ImageView) theLayout.findViewById(R.id.ivOne);
        iview[1] = (ImageView) theLayout.findViewById(R.id.ivTwo);
        rlInvite = (RelativeLayout) theLayout.findViewById(R.id.rlInvite);
        rpInvite = (RippleView) theLayout.findViewById(R.id.rpInvite);
        spUniversities = (Spinner) theLayout.findViewById(R.id.spUniversities);
        llUploadContainer = (LinearLayout) theLayout.findViewById(R.id.llUploadContainer);
        llPagerContainer = (RelativeLayout) theLayout.findViewById(R.id.llPagerContainer);
        rlGallery = (RelativeLayout) theLayout.findViewById(R.id.rlGallery);
        pbBarImage = (ProgressBar) theLayout.findViewById(R.id.pbBarImage);
        rlPbarGallery = (ProgressBar) theLayout.findViewById(R.id.rlPbarGallery);
        rpGallery = (RippleView) theLayout.findViewById(R.id.rpGallery);
        vpPager.setPageMargin(25);
        vpPager.setPageMarginDrawable(R.color.mainback);

        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(mPagerAdapter);




        vpPager.setClipToPadding(false);
        vpPager.setPadding(70, 0, 70, 0);
        functions = new UserFunctions(getActivity());
        String profile = functions.getPref(StaticVariables.PROFILE,"");



        vpPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int pos = vpPager2.getCurrentItem();
                if (selectedPos != pos) {
                    selectedPos = pos;
                    change = false;
                    images = new String[2];
                    images[0] = functions.getPref(StaticVariables.PROFILE,"");
                    images[1] = functions.getPref(StaticVariables.PROFILEBANNER,"");
                    if (pos == 0) {

                        if(!images[1].contentEquals(""))
                        {
                            String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+images[1]+".jpg";
                            AQuery aq = new AQuery(getActivity());
                            ImageOptions op=new ImageOptions();
                            op.fileCache = true;
                            op.memCache=true;
                            op.targetWidth = 0;
                            op.fallback = R.drawable.uploadpicture;
                            aq.id(ivProfile).image(url, op);
                        }




                    } else if (pos == 1) {
                        if(!images[0].contentEquals(""))
                        {
                            String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+images[0]+".jpg";
                            AQuery aq = new AQuery(getActivity());
                            ImageOptions op=new ImageOptions();
                            op.fileCache = true;
                            op.memCache=true;
                            op.targetWidth = 0;
                            op.fallback = R.drawable.uploadpicture;
                            aq.id(ivProfile).image(url, op);
                        }


                    }
                    alternateImage(pos);
                    change = true;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


        universities = getResources().getStringArray(R.array.universities);
        ArrayAdapter<String> aa=new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,universities);
        spUniversities.setAdapter(aa);

        rlInvite.setOnClickListener(this);
        rlGallery.setOnClickListener(this);



        tvInviteRem.setText(functions.getPref(StaticVariables.INVITENUM,3) + " Remaining");



        if(!profile.contentEquals(""))
        {
            String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+profile+".jpg";
            AQuery aq = new AQuery(getActivity());
            ImageOptions op=new ImageOptions();
            op.fileCache = true;
            op.memCache=true;
            op.targetWidth = 0;
            op.fallback = R.drawable.uploadpicture;
            aq.id(ivProfile).image(url, op);
        }
        profileBanner =  functions.getPref(StaticVariables.PROFILEBANNER,"");
        profile =  functions.getPref(StaticVariables.PROFILE,"");
        if(profileBanner.contentEquals(""))
        {
            llUploadContainer.setVisibility(View.VISIBLE);
            llPagerContainer.setVisibility(View.GONE);
        }else {
            llUploadContainer.setVisibility(View.GONE);
            llPagerContainer.setVisibility(View.VISIBLE);
            mPagerAdapter2 = new BannerAdapter(getChildFragmentManager());
            vpPager2.setAdapter(mPagerAdapter2);
            vpPager2.setCurrentItem(1);
        }

        tvName.setText(functions.getPref(StaticVariables.FULLNAME,""));


        ivProfile.setOnClickListener(this);
        return  theLayout;
    }


    private void alternateImage(int pos)
    {
        for(int i=0; i<iview.length; i++)
        {
            if(i == pos)
            {
                iview[i].setImageResource(R.drawable.myovalwhite);
            }else
            {
                iview[i].setImageResource(R.drawable.myovalgrey);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlInvite:
                rpInvite.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        showInvite();
                    }
                });
                break;
            case R.id.ivProfile:

                String samp = functions.getPref(StaticVariables.PROFILEBANNER, "");

                if(!samp.contentEquals(""))
                {
                    int pos = vpPager2.getCurrentItem();
                    if(pos == 1)
                    {
                        openImageIntent();
                    }
                }else
                {
                    openImageIntent();
                }





                break;
            case R.id.rlGallery:
                rpGallery.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);


                        /*
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLERY);
                        */
                    }
                });
                break;
        }

    }





    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Images hold = new Images();
            Bundle bundle=new Bundle();
            bundle.putInt("pos",position);
            hold.setArguments(bundle);
            return hold;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }








    private class BannerAdapter extends FragmentStatePagerAdapter {
        public BannerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BannerFragment hold = new BannerFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("pos",position);
            hold.setArguments(bundle);
            return hold;
        }

        @Override
        public int getCount() {
            return BANNERNUM;
        }
    }






    private void showInvite()
    {

        final Dialog credDialog=new Dialog(getActivity());
        credDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        credDialog.setContentView(R.layout.invitedialog);


        RelativeLayout rlProceed = (RelativeLayout) credDialog.findViewById(R.id.rlInvite);
        final RippleView rpProceed = (RippleView) credDialog.findViewById(R.id.rpInvite);
        final EditText etName = (EditText) credDialog.findViewById(R.id.etName);
        final EditText etPhone = (EditText) credDialog.findViewById(R.id.etPhone);




        rlProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpProceed.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                        String name = etName.getText().toString();
                        String phone = etPhone.getText().toString();

                        if(name.contentEquals(""))
                        {
                            etName.setError("Enter name");
                            etName.requestFocus();
                        }else if(phone.contentEquals(""))
                        {
                            etPhone.setError("Enter phone number");
                            etPhone.requestFocus();
                        }else
                        {
                            credDialog.dismiss();
                            JsonObject json = new JsonObject();
                            json.addProperty(StaticVariables.ACCESSCODE, functions.getPref(StaticVariables.ACCESSCODE,""));
                            json.addProperty(StaticVariables.MOBILENUMBER, phone);
                            json.addProperty(StaticVariables.NAME, name);
                            sendInvite(json);
                        }


                    }
                });
            }
        });

        credDialog.show();
    }







    private void sendInvite(JsonObject data)
    {

        System.out.println("bbbbbbbb: " + data.toString());
        pDIalogi = new ProgressDialog(getActivity());
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Sending invite ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(getActivity());
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + "SendInvite")
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
                                        int data = functions.getInt(json, StaticVariables.DATA);
                                        functions.setPref(StaticVariables.INVITENUM,data);
                                        tvInviteRem.setText(data+" Remaining");
                                        inviteFinish();



                                    } else {
                                        functions.showMessage(message);

                                    }


                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("Invitation failed");

                                }

                            } catch (Exception ex) {
                                pDIalogi.dismiss();
                                ex.printStackTrace();
                            }
                        }
                    });


        } else {
            pDIalogi.dismiss();
            Toast.makeText(getActivity(), "No internet Connection Please try again later", Toast.LENGTH_LONG).show();


        }



    }





    private void inviteFinish()
    {

        final Dialog credDialog=new Dialog(getActivity());
        credDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        credDialog.setContentView(R.layout.invitefinishdialog);


        RelativeLayout rlProceed = (RelativeLayout) credDialog.findViewById(R.id.rlInvite);
        final RippleView rpProceed = (RippleView) credDialog.findViewById(R.id.rpInvite);




        rlProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpProceed.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        credDialog.dismiss();

                    }
                });
            }
        });

        credDialog.show();
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


                pbBarImage.setVisibility(View.VISIBLE);
                if (isCamera) {
                    // System.out.println("--------------------------- camera here"+image_path2);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = functions.getImageUri(photo);
                    //photo=rotate3(currImageURI);

                    image_path2 = functions.getRealPathFromURI(selectedImageUri);

                    //setCapturedImage(image_path2);
                    new SendImage().execute("profile");
                } else {


                    /*System.out.println("--------------------------- gallery "+image_path2);
                    selectedImageUri = data == null ? null : data.getData();
                    image_path2 = functions.getRealPathFromURI(selectedImageUri);
                    */

                    selectedImageUri=data.getData();
                    image_path2 = functions.getRealPathFromURI(selectedImageUri);
                    // ivProfile.setImageURI(selectedImageUri);
                    new SendImage().execute("profile");
                }
                // Bitmap bitmap = functions.getImageFromUri(selectedImageUri);
                //ivProfile.setImageBitmap(bitmap);


            }else if(requestCode == GALLERY)
            {
                rlPbarGallery.setVisibility(View.VISIBLE);
                selectedImageUri = data.getData();
                image_path2 = functions.getRealPathFromURI(selectedImageUri);
                new SendImage().execute("banner");

            }
        }
    }









    class SendImage extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        String type = "";

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

            type = args[0];
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

                if(type.contentEquals("profile"))
                json.addProperty(StaticVariables.PROFILE,file_url);
                else if(type.contentEquals("banner"))
                {
                    json.addProperty(StaticVariables.PROFILEBANNER,file_url);
                }

                // functions.setCapturedImage(image_path2,ivProfile);
                uploadImage(json, file_url, type);
            }else {
                pbBarImage.setVisibility(View.GONE);
                rlPbarGallery.setVisibility(View.GONE);
                functions.showMessage("unable to upload image");
            }


        }





    }







    private void uploadImage(JsonObject data, final String profile, final String type)
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
                                pbBarImage.setVisibility(View.GONE);
                                rlPbarGallery.setVisibility(View.GONE);
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

                                        if(type.contentEquals("profile"))
                                        {
                                            functions.setCapturedImage(image_path2, ivProfile);
                                            functions.setPref(StaticVariables.PROFILE,profile);
                                        }else if(type.contentEquals("banner"))
                                        {
                                            profileBanner = profile;
                                            functions.setPref(StaticVariables.PROFILEBANNER,profile);
                                            llUploadContainer.setVisibility(View.GONE);
                                            llPagerContainer.setVisibility(View.VISIBLE);
                                            mPagerAdapter2 = new BannerAdapter(getChildFragmentManager());
                                            vpPager2.setAdapter(mPagerAdapter2);
                                        }



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
