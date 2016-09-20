package com.example.ovmaglana.messapp;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ovmaglana on 13/09/2016.
 */
public class Receiver extends BroadcastReceiver  {


    private Camera mCamera;
    private String message = "";
    private String number = "";
    private TakePhoto tk;
    private Context cont;
    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    Ringtone r;
    @Override
    public void onReceive(Context context, Intent intent) {


        cont = context;
        r = RingtoneManager.getRingtone(cont, notification);
        Bundle intentExtras = intent.getExtras();
        Log.d("RISIBING", "Invoked");
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get("pdus");
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
                message = smsBody;
                number = address;

            }
            //Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
            Log.d("RISIBING", smsMessageStr);
            SQLiteDatabase database = context.openOrCreateDatabase("messages", context.MODE_PRIVATE, null);
            message = message.replace("'", "''");
            String finalmessage = "Number: " + number + " | Body: " + message;
            database.execSQL("CREATE TABLE IF NOT EXISTS incoming (id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR); ");
            database.execSQL("INSERT INTO incoming VALUES(null, '"+finalmessage+"');");
            doSomthing();
            //this will update the UI with message

        }
    }




    private void doSomthing()
    {

        if(message.contains("[camera]"))
        {
            Toast.makeText(cont," camera ",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(cont, AutoCapture.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cont.startActivity(i);
        }

        else if(message.contains("ring")) {

            AudioManager am = (AudioManager)cont.getSystemService(cont.AUDIO_SERVICE);
            switch (am.getRingerMode())
            {
                case AudioManager.RINGER_MODE_SILENT:
                {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                }
                case AudioManager.RINGER_MODE_VIBRATE:
                {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                }
                default:
                {
                    break;
                }
            }
            am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
            r.play();
            Intent i = new Intent(cont, MainActivity.class);//"pating.pat.pappaasa.helloworld.SMSsender");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cont.startActivity(i);
        }

        else if(message.contains("stop")) {
            r.stop();
            Intent i = new Intent(cont, MainActivity.class);//"pating.pat.pappaasa.helloworld.SMSsender");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cont.startActivity(i);
        }
    }



}
