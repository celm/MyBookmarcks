package marcomessini.mybookmarks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import static marcomessini.mybookmarks.DownloadWS.giveHash;
import static marcomessini.mybookmarks.DownloadWS.DownloadWebpageTask;


public class MainP extends ActionBarActivity{

    ListView listView ;
    DataBaseManager db=new DataBaseManager(this);
    GroupsAdapter adapter;
    ArrayList<Group> values1 = null;
    public int posizione;
    public static int hash;
    public int i=0;
    public String url;
    //DownloadWebpageTask mt = new DownloadWebpageTask(this);
    public boolean exe=true;
    public boolean start=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);

        setTitle("MyBookmarks -GROUP LIST-");
        //Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

         values1= db.getGroup();
                //new Group(1,"social",3)
        if (values1.isEmpty()){
            Log.e("ArrayList Gruppo","vuoto");
            new AlertDialog.Builder(MainP.this)
                    .setTitle("There aren't Grups")
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
                startActivity(newActivity);

                //per controllare se il sito è aggiornato
                /*posizione=position;
                int gruppo=values1.get(i).id_group;
                ArrayList<WebSite> webSiteAr=DataBaseManager.getWebSite(gruppo);
                while(webSiteAr.size()>i){
                    Log.e("Ciclo",""+i);
                    url=webSiteAr.get(i).URL;
                    if(exe){
                     exe=false;
                     mt.execute(url);
                    }
                    i++;
                }
                start=true;*/
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

    /*public void done(){
        int newHash=giveHash();
        int oldHash=db.takeHash(i);
        if (newHash!=oldHash){
            Log.e("cambio avvenuto"," nuovo"+newHash+"vecchio"+oldHash);
            db.setCheckWS(i,1);
            db.updateHash(i,newHash);
        }
        exe=true;
        int idG=values1.get(posizione).id_group;
        String nomeGroup=values1.get(posizione).name;
        Intent newActivity = new Intent(MainP.this, ListWebSIte.class);
        newActivity.putExtra("id_gruppo",idG);
        newActivity.putExtra("nome_gruppo",nomeGroup);
        if(!start){
            startActivity(newActivity);
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addGroup) {
            Intent ActivityAddGroup = new Intent(MainP.this, AddGroup.class);
            startActivityForResult(ActivityAddGroup, 1);
        }

        return super.onOptionsItemSelected(item);
    }
}
