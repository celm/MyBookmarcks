package marcomessini.mybookmarks;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
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
            } catch (Exception e) {
                e.printStackTrace();
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
            WS.hash=hashNew;
            callNotify(WS);

        }
        else {
            db.setCheckWS(WS.id_WebSite,0);
            Log.e("[SERVICE]HASH invariato"," wsName " +WS.name);
        }
    }

    public void callNotify(WebSite WS){
        //notifica
        Log.e("NOTIFICA PARTITA"," WS="+WS.name);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.bookmark_notify, " - "+WS.name+" - "+"HAS CHANGED", System.currentTimeMillis());
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
        notification.setLatestEventInfo(this, " - "+WS.name+" - "+"HAS CHANGED", WS.URL, pendingIntent);
        notificationManager.notify(WS.id_WebSite, notification);

    }
}