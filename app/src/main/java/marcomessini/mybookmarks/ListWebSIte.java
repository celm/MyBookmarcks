package marcomessini.mybookmarks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;


public class ListWebSIte extends ActionBarActivity {

    ListView listView ;
    int idGruppo;
    int idListWS;
    DataBaseManager db= new DataBaseManager(this);
    WebSiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_web_site);

        Intent intent = getIntent();

        idGruppo=intent.getIntExtra("id_gruppo", -1);

        Intent intentListWS = getIntent();
        idListWS=intentListWS.getIntExtra("id_list",-1);

        Toast.makeText(getApplicationContext(), ""+idGruppo,Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.listWS);

        //elementi di prova
        /*ArrayList<WebSite> valuesWS= new ArrayList<WebSite>();
        valuesWS.add(new WebSite(1,1,"La Gazzetta","http://www.lagazzetta.it","jkhgajyfjhvzf"));
        valuesWS.add(new WebSite(2,2,"Lercio","http://www.lercio.it","kjhgakjdgiu"));*/

        final ArrayList<WebSite> valuesWS = DataBaseManager.getWebSite(idGruppo);

        adapter = new WebSiteAdapter(this, valuesWS);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String url=valuesWS.get(position).URL;
                Intent newActivity = new Intent(ListWebSIte.this, WebViewA.class);
                newActivity.putExtra("URL",url);
                startActivity(newActivity);
            }

        });

        //per eliminare i siti
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ListWebSIte.this)
                        .setTitle("Delete Web Site")
                        .setMessage("Are you sure you want to delete this web site?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //funzione per eliminare il sito
                                int id_ws=valuesWS.get(position).id_WebSite;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                //recupero i dati
                int gruppo=data.getIntExtra("id_g",-1);
                String url=data.getStringExtra("newURL");
                String nameWS=data.getStringExtra("newNameWS");
                //eseguo la query di inserimento
                long idWS=DataBaseManager.addWebSite(gruppo,url,nameWS,"");
                //inserisci dentro il content value
                int id_WS=(int)idWS;
                adapter.add(new WebSite(id_WS,gruppo,nameWS,url,""));
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_web_site, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addWS) {
            Intent ActivityAddWS = new Intent(ListWebSIte.this, AddWebSite.class);
            ActivityAddWS.putExtra("id_list",idListWS);
            ActivityAddWS.putExtra("id_gruppo",idGruppo);
            //activity for result
            startActivityForResult(ActivityAddWS, 1);
        }

        return super.onOptionsItemSelected(item);
    }
}
