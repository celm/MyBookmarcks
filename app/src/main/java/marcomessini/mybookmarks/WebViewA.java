package marcomessini.mybookmarks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


public class WebViewA extends ActionBarActivity {

    String url;
    String nome;
    int idWS;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        idWS= intent.getIntExtra("IDWS", 0);
        url= intent.getStringExtra("URL");
        nome= intent.getStringExtra("nomeSito");
        setTitle("WebSite "+nome);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(url);
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
/*

        if (id == R.id.action_modWS) {
            final EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Modify WebSite's Name")
                    .setView(input)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //per modificare il nome
                            String value = input.getText().toString();
                            DataBaseManager.modWSname(idWS,value);
                            setTitle("WebSite " + value);
                            Log.e("NOME WS MOD ","");
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).show();
        }
*/

        return super.onOptionsItemSelected(item);
    }
}
