package com.ghroupdrive.app;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by pk on 5/19/15.
 */
public class UserFunctions {
    private static Context _mcontext;
    private static SharedPreferences mSharedPreferences;
    private int totalMark= 0;

    UserFunctions(Context _mcontext){
       this._mcontext=_mcontext;
        mSharedPreferences = this._mcontext.getSharedPreferences("MyPref", 0);
    }

    public void setPref(String title, boolean value){
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putBoolean(title, value);
        ed.commit();

    }


    public void setPref(String title, String value){
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putString(title, value);
        ed.commit();

    }

    public void setPref(String title, int value){
        SharedPreferences.Editor ed = mSharedPreferences.edit();
        ed.putInt(title, value);
        ed.commit();

    }

    public boolean getPref(String title, boolean def){
       return mSharedPreferences.getBoolean(title,def);
    }

    public Float getPref(String title, Float def){
       return mSharedPreferences.getFloat(title, def);
    }


    public String getPref(String title, String def){
        return mSharedPreferences.getString(title, def);
    }

    public int getPref(String title, int def){
        return mSharedPreferences.getInt(title, def);
    }




    public void showMessage(String message){
        Toast.makeText(_mcontext, message, Toast.LENGTH_LONG).show();
    }


    public String getText(EditText ettext){
        return  ettext.getText().toString();
    }


    public ProgressDialog prepareDialog(String message, boolean cancel){
        ProgressDialog pDialog=new ProgressDialog(_mcontext);
        pDialog.setMessage(message);
        pDialog.setCancelable(cancel);
        pDialog.setIndeterminate(true);
        return  pDialog;
    }

public String getPhoneID(){
    TelephonyManager tm;
    tm=(TelephonyManager) _mcontext.getSystemService(Context.TELEPHONY_SERVICE);
   String phoneIME=tm.getDeviceId();
    if(phoneIME==null){
        phoneIME=   Settings.Secure.getString(_mcontext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    return phoneIME;
}





    public JSONObject getJsonObject(JSONObject json, String title){
        try
        {
            return  json.getJSONObject(title);
        }catch (Exception e){
            e.printStackTrace();
            return  null;

        }
    }
    public JSONArray getJsonArray(JSONObject json, String title){
        try
        {
            return  json.getJSONArray(title);
        }catch (Exception e){
            e.printStackTrace();
            return  null;

        }
    }

    public String getJsonStringWithNull(JSONObject json, String title){
        try
        {
            return  json.getString(title);
        }catch (Exception e){
            e.printStackTrace();
            return  null;

        }
    }







public String getJsonString(JSONObject json, String title){
    try {
        return json.getString(title);
    }catch (Exception e){
        e.printStackTrace();
        return "";

    }
}


    public int getInt(JSONObject json, String title){
        try {
            return json.getInt(title);
        }catch (Exception e){
            e.printStackTrace();
            return 0;

        }
    }

    public JSONArray getArray(JSONObject json, String title){
        try{
            return json.getJSONArray(title);
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }
    }







    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }










    public static boolean isEmpty(String data)
    {
        if(data.contentEquals(""))
            return  true;
            else
            return  false;
    }



    public int getSelectPos(String title, String[] titleArray )
    {
        int pos =0;
        if(title.contentEquals(""))
        {
            return  pos;
        }else
        {

            for(int i=0; i<titleArray.length; i++)
            {
                System.out.println("region here: "+title+"=> ch: "+titleArray[i]);
                if(title.toLowerCase().contentEquals(titleArray[i].toLowerCase())) {
                    return i;
                }
            }
        }


        return  pos;
    }






    public void showUserProfileDialog()
    {
        final Dialog credDialog=new Dialog(_mcontext);
        credDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        credDialog.setContentView(R.layout.image_details_dialog);

        FloatingActionButton  message = (FloatingActionButton) credDialog.findViewById(R.id.fbMessage);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credDialog.dismiss();

                Intent it = new Intent(_mcontext, Chat.class);

                    it.putExtra("type","message");

                _mcontext.startActivity(it);

            }
        });

        FloatingActionButton  fbProfile = (FloatingActionButton) credDialog.findViewById(R.id.fbProfile);

        fbProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credDialog.dismiss();
                Intent it = new Intent(_mcontext, UserProfile.class);

                //it.putExtra("type","message");

                _mcontext.startActivity(it);

            }
        });

        FloatingActionButton  fbCall = (FloatingActionButton) credDialog.findViewById(R.id.fbCall);

        fbCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +"0266502723"));
                _mcontext.startActivity(intent);

            }
        });



        credDialog.show();

    }



    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(_mcontext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    public String getRealPathFromURI(Uri contentURI) {
        // can post image
        String[] proj={MediaStore.Images.Media.DATA};
        Cursor cursor =_mcontext.getContentResolver().query( contentURI,proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }








    public void setCapturedImage(final String imagePath, final ImageView ivProfile){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                try {
                    System.out.println("--------------------------------------------- starting ");

                    if(getDeviceName().contains("SAMSUNG"))
                    {
                        return imagePath;
                    }else
                        return getRightAngleImage(imagePath);
                }catch (Throwable e){
                    e.printStackTrace();
                }
                return imagePath;
            }

            @Override
            protected void onPostExecute(String imagePath) {
                super.onPostExecute(imagePath);
                System.out.println("--------------------------------------------- decoding file here ");
                ivProfile.setImageBitmap(decodeFile(imagePath));
            }
        }.execute();
    }



    private String getRightAngleImage(String photoPath) {

        try {
            System.out.println("--------------------------------------------- Right angle image");
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree,photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }



    private String rotateImage(int degree, String imagePath){

        if(degree<=0){
            return imagePath;
        }
        System.out.println("--------------------------------------------- rotate image ");
        String data ="degree: "+degree+"\n";
        try{
            Bitmap b= BitmapFactory.decodeFile(imagePath);
            Matrix matrix = new Matrix();
            if(b.getWidth()>b.getHeight()){
                data+="WIDTH GREATE: w: "+b.getWidth()+" H: "+b.getHeight();
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }else if(b.getWidth()>b.getHeight() && degree<=0){
                data+="degree 0: w: "+b.getWidth()+" H: "+b.getHeight();
                matrix.setRotate(90);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }
            final String copy =data;
            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);


            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            }else if (imageType.equalsIgnoreCase("jpeg")|| imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return imagePath;
    }


    public Bitmap decodeFile(String path) {
        try {
            // Decode deal_image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap  map = BitmapFactory.decodeFile(path, o2);
//            int imageHeigt = map.getHeight();
            //  int imageWidth = map.getWidth();

            return map;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }





    public ArrayList<GettersAndSetters> formatRide(JSONArray json)
    {
        ArrayList   details = new ArrayList<>();
        GettersAndSetters Details;


        if(!getPref(StaticVariables.HASGOTITSEARCH,false))
        {
            Details = new GettersAndSetters();
            Details.setType(StaticVariables.GOTIT);
            details.add(Details);
        }



        try
        {
            if(!StaticVariables.MYLOC.contentEquals(""))
            {

                 JSONArray myRides = new JSONArray(StaticVariables.MYLOC);
                if(myRides.length()>0)
                {
                    String rideJsonString = myRides.getJSONObject(0).toString();
                    RideObject rb = new RideObject(rideJsonString,this);
                    Details = new GettersAndSetters();
                    Details.setType(StaticVariables.BOOKTYPE);


                    if(getPref(rb.RideId,0) != 0)
                    {
                        Details.setSeatNo("SEAT "+getPref(rb.RideId,0));
                    }else
                    {
                        JSONArray passengers = rb.Passengers;

                        for(int i=0; i<passengers.length(); i++)
                        {
                            JSONObject onePass = passengers.getJSONObject(i);
                            ProfileObject po = new ProfileObject(onePass.toString(),this);

                            if(po.AccessCode == getPref(StaticVariables.ACCESSCODE,""))
                            {
                                Details.setSeatNo("SEAT "+po.Seat);

                                break;
                            }else
                            {
                                continue;
                            }

                        }
                    }





                    ProfileObject driver = new ProfileObject(rb.Driver.toString(),this);
                    Details.setDriversProfileUrl(driver.ProfileUrl);
                    Details.setName(driver.Fullname);





                    if(rb.RideStart !=null)
                    {
                        Details.setStartingPoint(getJsonString(rb.RideStart, StaticVariables.NAME));
                    }

                    if(rb.RideEnd != null)
                    {
                        Details.setEndingPoint(getJsonString(rb.RideEnd, StaticVariables.NAME));
                    }



                    if(rb.Stop1 != null)
                    {
                        Details.setPoint1(getJsonString(rb.Stop1, StaticVariables.NAME));
                    }else{
                        Details.setPoint1("");
                    }


                    if(rb.Stop2 != null)
                    {
                        Details.setPoint2(getJsonString(rb.Stop2, StaticVariables.NAME));
                    }else{
                        Details.setPoint2("");
                    }

                    Details.setJsonString(rideJsonString);
                    Details.setStatus(rb.Status);
                    details.add(Details);
                }
            }




            if(json.length()>0)
            {

                Details = new GettersAndSetters();
                Details.setType(StaticVariables.TOPTYPE);
                Details.setTitle("PEOPLE DRIVING FROM YOUR CURRENT LOCATION");
                details.add(Details);


                for(int i=0; i<json.length(); i++)
                {
                    JSONObject c = json.getJSONObject(i);

                    RideObject rdo = new RideObject(c.toString(),this);
                    Details = new GettersAndSetters();
                    Details.setType(StaticVariables.CONTENT);
                    JSONObject driver = getJsonObject(c,StaticVariables.DRIVER);
                    if(driver!= null)
                    {
                        Details.setDriversName(getJsonString(driver, StaticVariables.FULLNAME));
                        Details.setDriversProfileUrl(getJsonString(driver, StaticVariables.PROFILE));
                    }



                        JSONObject start = getJsonObject(c, StaticVariables.STARTING);

                        if(start !=null)
                        {
                            Details.setStartingPoint(getJsonString(start, StaticVariables.NAME));
                        }



                        JSONObject end = getJsonObject(c, StaticVariables.ENDING);
                        if(end != null)
                        {
                            Details.setEndingPoint(getJsonString(end, StaticVariables.NAME));
                        }



                        JSONObject mid1 = getJsonObject(c, StaticVariables.MID1);

                        if(mid1 != null)
                        {
                            Details.setPoint1(getJsonString(mid1, StaticVariables.NAME));
                        }else{
                            Details.setPoint1("");
                        }


                        JSONObject mid2 = getJsonObject(c,StaticVariables.MID2);
                        if(mid2 != null)
                        {
                            Details.setPoint2(getJsonString(mid2, StaticVariables.NAME));
                        }else{
                            Details.setPoint2("");
                        }

                    Details.setAvailableSeat(getJsonString(c, StaticVariables.AVAILABLESEAT));
                    Details.setCarType(getJsonString(c, StaticVariables.CARTYPE));
                    Details.setPrice(getJsonString(c, StaticVariables.PRICE));
                    Details.setJsonString(c.toString());
                    details.add(Details);



                }

            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        return  details;
    }


}
