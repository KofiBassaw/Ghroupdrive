package com.ghroupdrive.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class Receiver3 extends BroadcastReceiver {

    Context con;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
   // Toast.makeText(context, "sms received", Toast.LENGTH_LONG).show();
        //android.provider.Telephony.SMS_RECEIVED
    String smsReceived="android.intent.action.DATA_SMS_RECEIVED";
        String smsReceived2="android.provider.Telephony.SMS_RECEIVED";
        System.out.println("........................................................................."+intent.getAction());

        con=context;

    if(smsReceived.equals(intent.getAction()) || smsReceived2.equals(intent.getAction())){
        System.out.println("......................................................................... in it now");
       // String uricontent = intent.getDataString();
//        String []str = uricontent.split(":");
       // String strPort = str[str.length-1];
        String strPort = "";


        Bundle bundle=intent.getExtras();
    	if(bundle !=null){
      Object[] pdus=(Object[])bundle.get("pdus");
    		
    		final SmsMessage[] messages=new SmsMessage[pdus.length];
    		for(int i=0;i<pdus.length;i++){
    			messages[i]= SmsMessage.createFromPdu((byte[]) pdus[i]);
    		}
    		
    		if(messages.length>-1){
                System.out.println(".........................................................................  more than one");

                try{

                   for(int i=0; i<messages.length; i++){
                       System.out.println(".........................................................................in loop");

                       String phoneNum=messages[i].getOriginatingAddress();
                       String message="";


                           message=messages[i].getMessageBody();
                       Intent it = new Intent(StaticVariables.SMSRECEIVED);
                       it.putExtra(StaticVariables.MESSAGE,message);
                       context.sendBroadcast(it);

                   }




    			}catch (Exception e) {
					// TODO: handle exception
                    System.out.println("......................................................................... error received noww "+ e.toString());

                    e.printStackTrace();
				}
    		}else{
                System.out.println("......................................................................... length is small now");

            }
    	}
    }else{
        System.out.println("......................................................................... out of the case ");

    }
    
	}



}
