package marcomessini.mybookmarks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class ListWebSIte extends ActionBarActivity implements TaskCallback {

    ListView listView;
    SwipeRefreshLayout swipeLayout;
    int idGruppo;
    int idListWS;
    int i;
    boolean exe = false;
    String nameGroup;
    DataBaseManager db = new DataBaseManager(this);
    WebSiteAdapter adapter;
    ArrayList<WebSite> valuesWS;
    int count=0;
    TaskCallback tc=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_web_site);

        Intent intent = getIntent();

        idGruppo = intent.getIntExtra("id_gruppo", -1);
        nameGroup = intent.getStringExtra("nome_gruppo");
        setTitle("Group " + nameGroup);

        Intent intentListWS = getIntent();
        idListWS = intentListWS.getIntExtra("id_list", -1);

        listView = (ListView) findViewById(R.id.listWS);

        //elementi di prova
        /*ArrayList<WebSite> valuesWS= new ArrayList<WebSite>();
        valuesWS.add(new WebSite(1,1,"La Gazzetta","http://www.lagazzetta.it","jkhgajyfjhvzf"));
        valuesWS.add(new WebSite(2,2,"Lercio","http://www.lercio.it","kjhgakjdgiu"));*/

        /*valuesWS = DataBaseManager.getWebSite(idGruppo);

        adapter = new WebSiteAdapter(this, valuesWS);

        // Assign adapter to ListView
        listView.setAdapter(adapter);*/

        //controllare se tutti i siti sono aggiornati
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                count=0;
                for (i = 0; i <= valuesWS.size() - 1; i++) {
                    Log.e("CICLO", "" + i);
                    String url = valuesWS.get(i).URL;
                    Log.e("url ciclo", i + " - " + url);
                    WebSite webSite;
                    webSite=valuesWS.get(i);
                    DownloadWS.DownloadWebpageTask mt = new DownloadWS.DownloadWebpageTask(tc,webSite);
                    mt.execute(url);
                    if (i == valuesWS.size() - 1) {
                        exe = true;
                    }
                }
            }
        });
        //colori scroll
        swipeLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        /*//controllo se vuoto
        if (valuesWS.isEmpty()) {
            Log.e("ArrayList Gruppo", "vuoto");
            new AlertDialog.Builder(ListWebSIte.this)
                    .setTitle("There aren't any WebSite")
                    .setPositiveButton("Add Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent ActivityAddWS = new Intent(ListWebSIte.this, AddWebSite.class);
                            ActivityAddWS.putExtra("id_list", idListWS);
                            ActivityAddWS.putExtra("id_gruppo", idGruppo);
                            //activity for result
                            startActivityForResult(ActivityAddWS, 1);
                        }
                    }).show();
        }*/

         // ListView Item Click Listener
        //webview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = valuesWS.get(position).URL;
                String nome = valuesWS.get(position).name;
                Intent newActivity = new Intent(ListWebSIte.this, WebViewA.class);
                newActivity.putExtra("URL", url);
                newActivity.putExtra("nomeSito", nome);
                db.setCheckWS(valuesWS.get(position).id_WebSite,0);
                valuesWS.get(position).check=0;
                adapter.notifyDataSetChanged();
                startActivity(newActivity);
            }

        });

        //per eliminare i siti
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ListWebSIte.this)
                        .setTitle("Delete BookMark")
                        .setMessage("Are you sure you want to delete this BookMark?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //funzione per eliminare il sito
                                int id_ws = valuesWS.get(position).id_WebSite;
                                db.delWebSite(id_ws);
                                //refresh dell'activity
                                adapter.remove(adapter.getItem(position));
                                adapter.notifyDataSetChanged();
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
    protected void onStart(){
        super.onStart();
        valuesWS = DataBaseManager.getWebSite(idGruppo);
        //controllo se vuoto
        if (valuesWS.isEmpty()) {
            Log.e("ArrayList Gruppo", "vuoto");
            new AlertDialog.Builder(ListWebSIte.this)
                    .setTitle("There aren't any WebSite")
                    .setPositiveButton("Add Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent ActivityAddWS = new Intent(ListWebSIte.this, AddWebSite.class);
                            ActivityAddWS.putExtra("id_list", idListWS);
                            ActivityAddWS.putExtra("id_gruppo", idGruppo);
                            //activity for result
                            startActivityForResult(ActivityAddWS, 1);
                        }
                    }).show();
        }
        adapter = new WebSiteAdapter(this, valuesWS);
        //assegno l'adapter alla listview
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //recupero i dati
                int gruppo = data.getIntExtra("id_g", -1);
                String url = data.getStringExtra("newURL");
                String nameWS = data.getStringExtra("newNameWS");
                int hashIns = data.getIntExtra("hashCode", -1);
                //eseguo la query di inserimento nel DB
                long idWS = DataBaseManager.addWebSite(gruppo, url, nameWS, hashIns, 0);
                //inserisci dentro il content value per la view
                int id_WS = (int) idWS;
                Log.e("hash inserito", "" + hashIns);
                adapter.add(new WebSite(id_WS, gruppo, nameWS, url, hashIns, 0));
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void done(int hash,WebSite WS) {
        int hashNew = hash;
        int hashOld = WS.hash;
        if (hashNew == -1){
            hashNew = hashOld;
            Toast.makeText(this,"CONNESSION LOST WHIT "+WS.name,Toast.LENGTH_LONG).show();
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
            adapter.notifyDataSetChanged();
        }
        else {
            db.setCheckWS(WS.id_WebSite,0);
            WS.check=0;
            adapter.notifyDataSetChanged();
            Log.e("HASH invariato"," id_ws " +WS.id_WebSite);
        }
        if(count==valuesWS.size() - 1){
            swipeLayout.setRefreshing(false);
        }
        //count per sapere quando terminare lo scrollView
        count++;
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_list_web_site, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_addWS) {
                Intent ActivityAddWS = new Intent(ListWebSIte.this, AddWebSite.class);
                ActivityAddWS.putExtra("id_list", idListWS);
                ActivityAddWS.putExtra("id_gruppo", idGruppo);
                //activity for result
                startActivityForResult(ActivityAddWS, 1);
            }
            if (id == R.id.action_modGroup) {
                final EditText input = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("Modify Group's Name")
                        .setView(input)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //per modificare il nome
                                String value = input.getText().toString();
                                int IDG=idGruppo;
                                db.modGroupName(IDG,value);
                                setTitle("Group " + value);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }

            return super.onOptionsItemSelected(item);
        }

    }