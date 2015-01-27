package marcomessini.mybookmarks;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by marcomessini on 27/01/15.
 */
public class UpToDateServiceIntent extends IntentService implements TaskCallback{

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    TaskCallback tc=this;
    WebSite WS;
    DataBaseManager db=new DataBaseManager(this);
    ArrayList<WebSite> valueWS;
    ArrayList<Group> valueG;

    private static final String TAG = "DownloadService";

    public UpToDateServiceIntent() {
        super(UpToDateServiceIntent.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                valueG=DataBaseManager.getGroup();
                for(int i=0;i<=valueG.size()-1;i++){
                    valueWS=DataBaseManager.getWebSite(i);
                    for(int j=0;j<=valueWS.size()-1;j++){
                        WS=valueWS.get(j);
                        DownloadWS.DownloadWebpageTask mt= new DownloadWS.DownloadWebpageTask(tc,WS);
                        mt.execute(WS.URL);
                    }
                }

                /* Sending result back to activity */
                /*if (null != results && results.length > 0) {
                    bundle.putStringArray("result", results);
                    receiver.send(STATUS_FINISHED, bundle);
                }*/
            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
    }

    @Override
    public void done(int hash, WebSite ws) {
        int hashNew = hash;
        int hashOld = WS.hash;
        if (hashNew == -1){
            hashNew = hashOld;
            Toast.makeText(this, "CONNESSION LOST WHIT " + WS.name, Toast.LENGTH_LONG).show();
        }
        //controllo hash
        if (hashNew != hashOld) {
            Log.e("HASH cambiato", " id_ws " + WS.id_WebSite);
            db.setCheckWS(WS.id_WebSite, 1);
            db.updateHASH(WS.id_WebSite, hashNew);//non funziona correttamente(corretta)?!-->da provare
            //Log.e("hash new inserito"," "+ins);
            Log.e("NEW HASH",""+hashNew);
            Log.e("OLD HASH",""+hashOld);
            WS.check=1;
            WS.hash=hashNew;
            //adapter.notifyDataSetChanged();
        }
        else {
            db.setCheckWS(WS.id_WebSite,0);
            WS.check=0;
            //adapter.notifyDataSetChanged();
            Log.e("HASH invariato"," id_ws " +WS.id_WebSite);
        }
    }
}