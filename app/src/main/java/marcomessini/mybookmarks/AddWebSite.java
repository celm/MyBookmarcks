package marcomessini.mybookmarks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import java.net.URL;

import static marcomessini.mybookmarks.DownloadWS.giveHash;
import static marcomessini.mybookmarks.DownloadWS.DownloadWebpageTask;


public class AddWebSite extends ActionBarActivity implements TaskCallback{

    EditText nameWS;
    EditText URL;
    Button add;
    public static String newNameWS;
    public static String newURL;
    public static int hash;
    public static int id_g;
    DownloadWebpageTask mt = new DownloadWebpageTask(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_web_site);



        final Intent intent= getIntent();
        id_g=intent.getIntExtra("id_gruppo",-1);
        //edit text name WS
        nameWS = (EditText) findViewById(R.id.editTextAddWSName);
        //edit text url
        URL = (EditText) findViewById(R.id.editTextAddWSURL);

        add = (Button) findViewById(R.id.buttonAddWS);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //per prendere il nome
                newNameWS = nameWS.getText().toString();
                newURL=URL.getText().toString();

                //per tornare all'activity precedente
                /*Intent ActivityRet= new Intent(AddWebSite.this, ListWebSIte.class);
                ActivityRet.putExtra("id_gruppo",id_g);
                startActivity(ActivityRet);*/
                boolean name=newNameWS.isEmpty();
                boolean url=newURL.isEmpty();
                if (!name && !url){
                    if (!newURL.contains("http://"))
                        newURL = "http://" + newURL;

                    URL urlW;
                    try {
                        urlW = new URL(newURL);
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            //startAsyncTask(newURL);
                            mt.execute(newURL);

                        } else {
                            //alert if no connection
                            Toast.makeText(getApplicationContext(),"No network connection available",Toast.LENGTH_LONG).show();
                        }

                    } catch (MalformedURLException e) {
                        Toast.makeText(getApplicationContext(),"The value is not an URL",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    //stringhe nn inserite(non fare niente)
                }
            }
        });
    }

    //implemento done() per passare dati e terminare l'activity
    public void done() {
        hash=giveHash();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("id_g",id_g);
        returnIntent.putExtra("newURL",newURL);
        returnIntent.putExtra("newNameWS",newNameWS);
        returnIntent.putExtra("hashCode",hash);
        setResult(RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_web_site, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

}

