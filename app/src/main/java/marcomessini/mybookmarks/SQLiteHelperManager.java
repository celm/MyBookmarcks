package marcomessini.mybookmarks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcomessini on 19/01/15.
 */
public class SQLiteHelperManager extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "WebSiteGroups";

    public SQLiteHelperManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create groups & website tables
        String CREATE_GROUPS_TABLE = "CREATE TABLE groups ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT )";

        String CREATE_WEBSITE_TABLE = "CREATE TABLE website (" +
                "id_ws INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_group INTEGER REFERENCES groups(id) ON DELETE CASCADE, " +
                "nameWS TEXT, " +
                "url TEXT, " +
                "hash INTEGER," +
                "check_ws INTEGER )";

        // create groups & website tables
        db.execSQL(CREATE_GROUPS_TABLE);
        db.execSQL(CREATE_WEBSITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older groups & website tables if existed
        db.execSQL("DROP TABLE IF EXISTS groups");
        db.execSQL("DROP TABLE IF EXISTS website");

        // create fresh groups & website tables
        this.onCreate(db);
    }
}
