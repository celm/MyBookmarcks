package marcomessini.mybookmarks;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by marcomessini on 27/01/15.
 */
public class UpToDateServiceIntent extends IntentService implements TaskCallback{

    DataBaseManager db=new DataBaseManager(this);
    private static final String TAG = "DownloadService";

    public UpToDateServiceIntent() {
        super(UpToDateServiceIntent.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

            try {
                ArrayList<Group> valueG=DataBaseManager.getGroup();
                for(int i=0;i<=valueG.size()-1;i++){
                    ArrayList<WebSite> valueWS=DataBaseManager.getWebSite(valueG.get(i).id_group);
                    for(int j=0;j<=valueWS.size()-1;j++){
                        WebSite WS=valueWS.get(j);
                        DownloadWS.DownloadWebpageTask mt= new DownloadWS.DownloadWebpageTask(this,WS);
                        mt.execute(WS.URL);
                    }
                }

                /* Sending result back to activity */
                /*if (null != results && results.length > 0) {
                    bundle.putStringArray("result", results);
                    receiver.send(STATUS_FINISHED, bundle);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
                /* Sending error message back to activity */
                //bundle.putString(Intent.EXTRA_TEXT, e.toString());
                //receiver.send(STATUS_ERROR, bundle);
            }
        }


    @Override
    public void done(int hash, WebSite WS) {
        int hashNew = hash;
        int hashOld = WS.hash;
        if (hashNew == -1){
            hashNew = hashOld;
            Toast.makeText(this, "CONNESSION LOST WHIT " + WS.name, Toast.LENGTH_LONG).show();
        }
        //controllo hash
        if (hashNew != hashOld) {
            Log.e("[SERVICE]HASH cambiato", " wsName " + WS.name);
            db.setCheckWS(WS.id_WebSite, 1);
            db.updateHASH(WS.id_WebSite, hashNew);
            Log.e("NEW HASH",""+hashNew);
            Log.e("OLD HASH",""+hashOld);
            //WS.check=1;
            WS.hash=hashNew;
            callNotify(WS);

        }
        else {
            db.setCheckWS(WS.id_WebSite,0);
            //WS.check=0;
            Log.e("[SERVICE]HASH invariato"," wsName " +WS.name);
        }
    }

    public void callNotify(WebSite WS){
        //notifica
        Log.e("NOTIFICA PARTITA"," WS="+WS.name);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.bookmark_notify, "-"+WS.name+"-"+"IS CHANGED", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        Intent intent = new Intent(this, WebViewA.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("URL", WS.URL);
        intent.putExtra("nomeSito", WS.name);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,WS.id_WebSite,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "-"+WS.name+"-"+"IS CHANGED", WS.name, pendingIntent);
        notificationManager.notify(WS.id_WebSite, notification);

        /*//Android dev
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)

                        .setContentTitle(" - "+WS.name+" - "+" IS CHANGED")
                        .setContentText("MyBookmarks")
                        .setSmallIcon(android.R.drawable.ic_dialog_email)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent intent=new Intent(this,WebViewA.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("IDWS",WS.id_WebSite);
        intent.putExtra("URL", WS.URL);
        intent.putExtra("nomeSito",WS.name);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(WebViewA.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        *//*PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);*//*
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(WS.id_WebSite, mBuilder.build());*/


        //zero al posto di PendingIntent.FLAG_CANCEL_CURRENT opp WS.id_WebSite
        /*PendingIntent pi= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder n  = new NotificationCompat.Builder(this)
                .setContentTitle(" - "+WS.name+" - "+" IS CHANGED")
                .setContentText("MyBookmarks")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentIntent(pi)
                .setAutoCancel(true);


                Intent intent=new Intent(this,WebViewA.class);
                intent.putExtra("IDWS",WS.id_WebSite);
                intent.putExtra("URL", WS.URL);
                intent.putExtra("nomeSito",WS.name);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(WS.id_WebSite, n.build());*/

    }
}