package marcomessini.mybookmarks;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddGroup extends ActionBarActivity {

    EditText nameGroup;
    Button add;
    public static String newNameGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        nameGroup = (EditText) findViewById(R.id.editTextAddG);

        add = (Button) findViewById(R.id.buttonAddG);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //per prendere il nome
                newNameGroup = nameGroup.getText().toString();
                /*Intent ActivityRet = new Intent(AddGroup.this, MainP.class);
                startActivity(ActivityRet);*/
                boolean nameG= newNameGroup.isEmpty();
                if(!nameG){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("newNameGroup",newNameGroup);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
                else{
                    //non fare niente
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_group, menu);
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
