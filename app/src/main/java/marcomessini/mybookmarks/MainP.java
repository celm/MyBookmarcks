package marcomessini.mybookmarks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;


public class MainP extends ActionBarActivity {

    ListView listView ;
    DataBaseManager db=new DataBaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);

        //Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        //Prova per visualizare nella ListView
       /* String[] values = new String[]{
                "Gruppo1",
                "Gruppo2" *//*,
                "Gruppo3" ,
                "Gruppo4"*//*
        };*/

        final ArrayList<Group> values1 = db.getGroup();
                //new Group(1,"social",3)

        final GroupsAdapter adapter = new GroupsAdapter(this, values1);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                values1.get(position);
                Intent newActivity = new Intent(MainP.this, ListWebSIte.class);
                newActivity.putExtra("id_gruppo",position);
                startActivity(newActivity);
                // ListView Clicked item index
                //int itemPosition = position;

                // ListView Clicked item value
                //String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                //Toast.makeText(getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();

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
                                db.delGroup(position);
                                db.delWsOfGroup(position);
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

        return super.onOptionsItemSelected(item);
    }
}
