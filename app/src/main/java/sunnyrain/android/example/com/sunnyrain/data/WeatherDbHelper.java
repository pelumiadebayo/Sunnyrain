package sunnyrain.android.example.com.sunnyrain.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import sunnyrain.android.example.com.sunnyrain.data.WeatherContract.LocationEntry;
import sunnyrain.android.example.com.sunnyrain.data.WeatherContract.WeatherEntry;


public class WeatherDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION =1;
    public  static final String DATABASE_NAME = "weather.db";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE=
                "CREATE TABLE" + LocationEntry.TABLE_NAME + "(" +
                        LocationEntry._ID + " INTEGER PRIMARY KEY, " +
                        LocationEntry.COLUMN_LOCATION_SETTING + " TEXT UNIQUE NOT NULL, " +
                        LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                        LocationEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                        LocationEntry.COLUMN_COORD_LONG + " REAL NOT NULL, " +
                        //inserting in the database with the same key will not update
                        // the database and will not return an ID
                        "UNIQUE (" + LocationEntry.COLUMN_LOCATION_SETTING + ")" +
                        " ON CONFLICT IGNORE" + " );";

        final String SQL_CREATE_WEATHER_TABLE=
                "CREATE TABLE "+ WeatherEntry.TABLE_NAME + "(" +
                        WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                        WeatherEntry.COLUMN_DATETEXT + " TEXT NOT NULL, " +
                        WeatherEntry.COLUMN_SHORT_DECS + " TEXT NOT NULL, " +
                        WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +

                        WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +

                        WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +

                        //setting the loc_key column as a foreign key to location table
                        //this ensure SQL to enforce a relationship btw the two tables
                        " FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES" +
                        LocationEntry.TABLE_NAME + "(" + LocationEntry._ID +"),"+

                        //to assure that app has just one weather entry per day & location
                        " UNIQUE ("+ WeatherEntry.COLUMN_DATETEXT + ", "+
                        WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE );";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST" + WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
