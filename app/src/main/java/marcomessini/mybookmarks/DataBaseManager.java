package marcomessini.mybookmarks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by marcomessini on 19/01/15.
 */
public class DataBaseManager {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = DataBaseManager.class.getSimpleName();

    private static Context context;
    private static SQLiteDatabase db;
    private static SQLiteHelperManager dbHelper;

    //nome tabelle

    //tab gruppi
    public static final String TABLE_GROUPS = "groups";
    //tab website
    public static final String TABLE_WEBSITE = "website";

    //nomi colonne

    //tab groups
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    //tab website
    public static final String KEY_IDWS ="id_ws";
    public static final String KEY_IDG = "id_group";
    public static final String KEY_NAMEWS = "nameWS";
    public static final String KEY_URL = "url";
    public static final String KEY_HASH = "hash";
    public static final String KEY_CHECK = "check_ws";

    private static final String[] COLUMNS_GROUP = {KEY_ID,KEY_NAME};

    private static final String[] COLUMNS_WEBSITE = {KEY_IDWS,KEY_IDG,KEY_NAMEWS,KEY_URL,KEY_HASH,KEY_CHECK};

    public DataBaseManager(Context context1) {
        context = context1;
    }

    //aprire connesione DB
    private static void open() throws SQLException {
        dbHelper = new SQLiteHelperManager(context);
        db = dbHelper.getWritableDatabase();
    }
    //chiudere connesione DB
    private static void close() {
        dbHelper.close();
    }

    //per tab gruppi
    private static ContentValues createContentValuesG(String name) {
        ContentValues valuesG = new ContentValues();
        valuesG.put( KEY_NAME, name );

        return valuesG;
    }

    //per tab website
    private static ContentValues createContentValuesWS(int idG, String url, String nameWS, int hash, int check_ws) {
        ContentValues valuesWS = new ContentValues();
        valuesWS.put( KEY_IDG, idG );
        valuesWS.put( KEY_NAMEWS, nameWS );
        valuesWS.put( KEY_URL, url );
        valuesWS.put( KEY_HASH, hash );
        valuesWS.put( KEY_CHECK, check_ws );

        return valuesWS;
    }

    //METODI

    //add gruppo
    public static long addGroup(String name) {
        ContentValues initialValues = createContentValuesG(name);
        open();
        long res=db.insertOrThrow(TABLE_GROUPS, null, initialValues);
        close();
        return res;
    }

    //delete group
    public boolean delGroup(int id) {
        String id_g=Integer.toString(id);
        String[] id_ag={id_g};
        open();
        boolean res = db.delete(TABLE_GROUPS, KEY_ID + "=?", id_ag) > 0;
        close();
        return res;
    }

    //elimina i siti con gruppo id = id
    public boolean delWebSite(int id) {
        String id_str=Integer.toString(id);
        String[] id_s= {id_str};
        open();
        boolean res= db.delete(TABLE_WEBSITE, KEY_IDWS +"=?", id_s) > 0;
        close();
        return res;
    }

    //show all groups async
    public static void getGroupAsync(ListView lv){
        new AsyncTask<Void,Void,ArrayList<Group>>(){
            @Override
            protected ArrayList<Group> doInBackground(Void... params) {
                open();
                Cursor cursor = db.query(TABLE_GROUPS, new String[]{KEY_ID, KEY_NAME}, null, null, null, null, null);
                ArrayList<Group> ris= new ArrayList<Group>();
                while (cursor.moveToNext()) {
                    int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    String name= cursor.getString(cursor.getColumnIndex(KEY_NAME));
                    Cursor cursorWS= db.rawQuery("select * from website where id_group = ?", new String[] { Integer.toString(ID) });
                    int risCount=cursorWS.getCount();
                    ris.add(new Group(ID,name,risCount));
                }
                close();
                return ris;
            }
            @Override
            protected void onPostExecute(ArrayList<Group> al){
                ArrayList<Group> value = al;
                return;
            }
        };
    }

    //show all group
    public static ArrayList<Group> getGroup() {
        open();
        Cursor cursor = db.query(TABLE_GROUPS, new String[]{KEY_ID, KEY_NAME}, null, null, null, null, null);
        ArrayList<Group> ris= new ArrayList<Group>();
        while (cursor.moveToNext()) {
            int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name= cursor.getString(cursor.getColumnIndex(KEY_NAME));
            Cursor cursorWS= db.rawQuery("select * from website where id_group = ?", new String[] { Integer.toString(ID) });
            int risCount=cursorWS.getCount();
            ris.add(new Group(ID,name,risCount));
        }
        close();
        return ris;
    }

    //funzioni tabella WebSite
    public static long addWebSite(int idG, String url, String nameWS, int hash, int check_ws){
        ContentValues initialValuesWS = createContentValuesWS(idG, url, nameWS, hash, check_ws);
        open();
        long resW=db.insertOrThrow(TABLE_WEBSITE, null, initialValuesWS);
        close();
        return resW;
    }

    //show all WS Async

    //show all WS
    public static ArrayList<WebSite> getWebSite(int IDG) {
        open();
        Cursor cursor= db.rawQuery("select * from website where id_group = ?", new String[] { Integer.toString(IDG) });
        ArrayList<WebSite> ris= new ArrayList<WebSite>();
        while (cursor.moveToNext()) {
            int IDWS = cursor.getInt(cursor.getColumnIndex(KEY_IDWS));
            String name= cursor.getString(cursor.getColumnIndex(KEY_NAMEWS));
            String url= cursor.getString(cursor.getColumnIndex(KEY_URL));
            int hash= cursor.getInt(cursor.getColumnIndex(KEY_HASH));
            int check= cursor.getInt(cursor.getColumnIndex(KEY_CHECK));
            ris.add(new WebSite(IDWS,IDG,name,url,hash,check));
        }
        close();
        return ris;
    }

    //setta la variabile check
    //set prende 1 o 0
    public static boolean setCheckWS(int id_ws,int set){
        ContentValues cv= new ContentValues();
        cv.put(KEY_CHECK,set);
        String id_WS=Integer.toString(id_ws);
        String[] id_WebPage= {id_WS};
        open();
        int ris=db.update(TABLE_WEBSITE,cv,KEY_IDWS+"=?",id_WebPage);
        close();
        if(ris>0){
            return true;
        }
        return false;
    }

    //aggiorna hash
    public static int updateHASH(int id_ws,int newHash){
        ContentValues cv = new ContentValues();
        int NEWHASH=newHash;
        cv.put(KEY_HASH,NEWHASH);
        String id_WS=Integer.toString(id_ws);
        String[] id_WebPage = {id_WS};
        open();
        int ris=db.update(TABLE_WEBSITE,cv,KEY_IDWS+"=?",id_WebPage);
        close();
        return ris;
    }

    //modifica nome gruppo
    public boolean modGroupName(int id_g, String newName){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,newName);
        String [] id_group={Integer.toString(id_g)};
        open();
        int ris=db.update(TABLE_GROUPS,cv,KEY_ID+"=?",id_group);
        close();
        if(ris>0){
            return true;
        }
        return false;
    }

    //modifica nome sito
    static boolean modWSname(int id_ws, String newNameWS){
        ContentValues cvW = new ContentValues();
        cvW.put(KEY_NAMEWS,newNameWS);
        String [] id_WS={Integer.toString(id_ws)};
        open();
        int ris=db.update(TABLE_WEBSITE,cvW,KEY_IDWS+"=?",id_WS);
        close();
        if(ris>0){
            return true;
        }
        return false;
    }
}
