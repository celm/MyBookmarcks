package marcomessini.mybookmarks;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


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

        Intent intent = getIntent();
        idWS= intent.getIntExtra("IDWS",0);
        url= intent.getStringExtra("URL");
        nome= intent.getStringExtra("nomeSito");
        setTitle("WebSite "+nome);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(url);

    }


    //implementare metodo onStart, su onNewIntent devo mettere setIntent(intent)
    //fare metodo
    // onStop(){
    //  websiteA.destroy();
    // }
    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            idWS= intent.getIntExtra("IDWS",0);
            url= intent.getStringExtra("URL");
            nome= intent.getStringExtra("nomeSito");
            setTitle("WebSite "+nome);

            myWebView.setWebViewClient(new WebViewClient());
            myWebView.loadUrl(url);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
