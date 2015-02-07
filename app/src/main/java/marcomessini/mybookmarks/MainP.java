package marcomessini.mybookmarks;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;


import static marcomessini.mybookmarks.DownloadWS.DownloadWebpageTask;


public class MainP extends ActionBarActivity{

    ListView listView ;
    DataBaseManager db=new DataBaseManager(this);
    GroupsAdapter adapter;
    ArrayList<Group> values1 = null;
    public String url;
    long timer;
    //TaskCallback tc=this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);

        //alarm & Service
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this,OnAlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);




        //set timer
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        long timer=pref.getLong("TIMER",60*15*1000);
        Log.e("Time set"," "+timer);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,timer,timer, pending);

        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        //        SystemClock.elapsedRealtime() +
        //                10 * 1000, pending);
                                //-------------//
        setTitle("MyBookmarks - GROUP LIST -");
        //Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

         values1= db.getGroup();
                //new Group(1,"social",3)
        if (values1.isEmpty()){
            Log.e("ArrayList Gruppo","vuoto");
            new AlertDialog.Builder(MainP.this)
                    .setTitle("There aren't any Groups")
                    .setPositiveButton("Add Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent ActivityAddGroup = new Intent(MainP.this, AddGroup.class);
                            startActivityForResult(ActivityAddGroup, 1);
                        }
                    })
                    /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })*/
                    .show();
        };


        adapter = new GroupsAdapter(this, values1);
        //adapter.notifyDataSetChanged();

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                int idG=values1.get(position).id_group;
                String nomeGroup=values1.get(position).name;
                Intent newActivity = new Intent(MainP.this, ListWebSIte.class);
                newActivity.putExtra("id_gruppo",idG);
                newActivity.putExtra("nome_gruppo",nomeGroup);


                //per controllare se il sito è aggiornato
                /*posizione=position;
                int gruppo=values1.get(i).id_group;
                ArrayList<WebSite> webSiteAr=DataBaseManager.getWebSite(gruppo);
                if(webSiteAr.size()>0) {
                    for (int i = 0; i <= webSiteAr.size(); i++) {
                        WebSite ws = webSiteAr.get(i);
                        DownloadWebpageTask mt = new DownloadWebpageTask(tc, ws);
                        mt.execute(ws.URL);
                    }
                }*/
                //parte l'actuvity --> da controllare
                startActivity(newActivity);
            }

        });

        //per eliminare il gruppo pressione prolungata
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainP.this)
                        .setTitle("Delete Group")
                        .setMessage("Are you sure you want to delete this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //funzione per eliminare il gruppo
                                int idGroup=values1.get(position).id_group;
                                boolean valG=db.delGroup(idGroup);
                                Log.i("valore query delG",Boolean.toString(valG));
                                adapter.remove(adapter.getItem(position));
                                //adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                //recupero i dati
                String name=data.getStringExtra("newNameGroup");
                //eseguo la query di inserimento
                long idWS=DataBaseManager.addGroup(name);
                //inserisci dentro il content value
                int id_G=(int)idWS;
                adapter.add(new Group(id_G,name,0));
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    void setTimer(long time, SharedPreferences pref){
        SharedPreferences.Editor edit=pref.edit();
        edit.putLong("TIMER",60*15*1000);
        edit.commit();
    }

    /*@Override
    public void done(int hash, WebSite webSite){
        int newHash=hash;
        int oldHash=webSite.hash;
        if(newHash!=oldHash){
            Log.e("HASH MODIFICATO "+webSite.id_WebSite," new H "+newHash+" old H"+oldHash);
            db.setCheckWS(webSite.id_WebSite,1);
            db.updateHash(webSite.id_WebSite,newHash);
            webSite.check=1;
            adapter.notifyDataSetChanged();
        }
        else{
            Log.e("HASH INVARIATO "+webSite.id_WebSite," old H"+oldHash);
            db.setCheckWS(webSite.id_WebSite,0);
            webSite.check=0;
            adapter.notifyDataSetChanged();
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            //Put the code for an action menu from the top here
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addGroup) {
            Intent ActivityAddGroup = new Intent(MainP.this, AddGroup.class);
            startActivityForResult(ActivityAddGroup, 1);
        }
        if (id == R.id.action_setTimer) {
            final CharSequence[] items = {"2Min","15Min", "30Min", "Hour","2Hour"};
            SharedPreferences pref = getPreferences(MODE_PRIVATE);
            final SharedPreferences.Editor edit= pref.edit();
            //timer= pref.getLong("TIMER",60*15*1000);
            new AlertDialog.Builder(this)
            .setTitle("Set Timer for UpDate")
                    //timer=60*15*1000;
                    //timer=60*30*1000;
                    //timer=60*60*1000;
                    //timer=60*120*1000;
            .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    //
                    switch (item) {
                        case (0):
                            edit.putLong("TIMER",60*2*1000);
                            edit.commit();
                            Log.e("SetTemp",""+items[0]);
                            break;
                        case (1):
                            edit.putLong("TIMER",60*15*1000);
                            edit.commit();
                            Log.e("SetTemp",""+items[0]);
                            break;
                        case (2):
                            edit.putLong("TIMER",60*30*1000);
                            edit.commit();
                            Log.e("SetTemp",""+items[1]);
                            break;
                        case(3):
                            edit.putLong("TIMER",60*60*1000);
                            edit.commit();
                            Log.e("SetTemp",""+items[2]);
                            break;
                        case(4):
                            edit.putLong("TIMER",60*120*1000);
                            edit.commit();
                            Log.e("SetTemp",""+items[3]);
                            break;
                    }
                }
            }).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
