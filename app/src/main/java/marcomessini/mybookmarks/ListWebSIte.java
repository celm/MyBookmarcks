package marcomessini.mybookmarks;

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

import java.util.ArrayList;


public class ListWebSIte extends ActionBarActivity {

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_web_site);

        listView = (ListView) findViewById(R.id.listWS);

        //Prova per visualizare nella ListView
        String[] valuesWS = new String[]{
                "Sito1",
                "Sito2" ,
                "Sito3" ,
                "Sito4" ,
                "Sito5" ,
                "Sito6" ,
                "Sito7" ,
                "Sito8" ,
                "Sito9" ,
                "Sito10"
        };

        final ArrayList<String> listWS = new ArrayList<String>();
        for (int i = 0; i < valuesWS.length; ++i) {
            listWS.add(valuesWS[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listWS);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){


                switch( position )
                {
                    case 0:  Intent newActivity = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity);
                        break;
                    case 1:  Intent newActivity1 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity1);
                        break;
                    case 2:  Intent newActivity2 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity2);
                        break;
                    case 3:  Intent newActivity3 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity3);
                        break;
                    case 4:  Intent newActivity4 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity4);
                        break;
                    case 5:  Intent newActivity5 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity5);
                        break;
                    case 6:  Intent newActivity6 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity6);
                        break;
                    case 7:  Intent newActivity7 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity7);
                        break;
                    case 8:  Intent newActivity8 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity8);
                        break;
                    case 9:  Intent newActivity9 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity9);
                        break;
                    case 10:  Intent newActivity10 = new Intent(ListWebSIte.this, WebViewA.class);
                        startActivity(newActivity10);
                        break;
                }
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();

            }

        });
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
            startActivity(ActivityAddWS);
        }
        if (id == R.id.action_delWS) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
