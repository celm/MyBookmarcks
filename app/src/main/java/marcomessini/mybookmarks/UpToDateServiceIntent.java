package marcomessini.mybookmarks;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
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
            callNotify(WS.name,WS.URL);
            WS.hash=hashNew;
        }
        else {
            db.setCheckWS(WS.id_WebSite,0);
            //WS.check=0;
            Log.e("[SERVICE]HASH invariato"," wsName " +WS.name);
        }
    }

    public void callNotify(String WSname, String url){
        Log.e("NOTIFICA PARTITA"," WS="+WSname);
        Intent intent=new Intent(this,WebViewA.class);
        intent.putExtra("URL", url);
        intent.putExtra("nomeSito",WSname);
        //zero al posto di PendingIntent.FLAG_CANCEL_CURRENT
        PendingIntent pi= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder n  = new NotificationCompat.Builder(this)
                .setContentTitle(" - "+WSname+" - ")
                .setContentText("IS CHANGED")
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentIntent(pi)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build());

    }
}