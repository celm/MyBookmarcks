package marcomessini.mybookmarks;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddWebSite extends ActionBarActivity {

    EditText nameWS;
    EditText URL;
    Button add;
    public static String newNameWS;
    public static String newURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_web_site);

        Intent intent= getIntent();
        final int id_g=intent.getIntExtra("id_gruppo",-1);
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
                DataBaseManager.addWebSite(id_g,newURL,newNameWS,"");

                Intent ActivityRet = new Intent(AddWebSite.this, ListWebSIte.class);
                startActivity(ActivityRet);
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
