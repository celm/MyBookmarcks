package marcomessini.mybookmarks;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class MainP extends ActionBarActivity {

    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);

        //Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        //Prova per visualizare nella ListView
        String[] values = new String[]{
                "Gruppo1",
                "Gruppo2" /*,
                "Gruppo3" ,
                "Gruppo4"*/
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){


                switch( position )
                {
                    case 0:  Intent newActivity = new Intent(MainP.this, ListWebSIte.class);
                        startActivity(newActivity);
                        break;
                    case 1:  Intent newActivity1 = new Intent(MainP.this, ListWebSIte.class);
                        startActivity(newActivity1);
                        break;
                    case 2:  Intent newActivity2 = new Intent(MainP.this, ListWebSIte.class);
                        startActivity(newActivity2);
                        break;
                    case 3:  Intent newActivity3 = new Intent(MainP.this, ListWebSIte.class);
                        startActivity(newActivity3);
                        break;
                    case 4:  Intent newActivity4 = new Intent(MainP.this, ListWebSIte.class);
                        startActivity(newActivity4);
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
            startActivity(ActivityAddGroup);
        }

        if (id == R.id.action_delGroup) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
