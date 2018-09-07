package sunnyrain.android.example.com.sunnyrain;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import android.database.Cursor;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import sunnyrain.android.example.com.sunnyrain.data.WeatherContract;
import sunnyrain.android.example.com.sunnyrain.data.WeatherContract.LocationEntry;
import sunnyrain.android.example.com.sunnyrain.data.WeatherContract.WeatherEntry;
import sunnyrain.android.example.com.sunnyrain.data.WeatherDbHelper;



public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void  testCreateDb() throws  Throwable{
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
    static public String TEST_CITY_NAME = "North Pole";

    static public ContentValues getLocationContentValues(){
        ContentValues values = new ContentValues();
        String testLocationSettings = "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.335;
        values.put(LocationEntry.COLUMN_CITY_NAME, TEST_CITY_NAME);
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSettings);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);
        return values;
    };
    static public  ContentValues getWeatherContentValues(long locationRowId){
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20180906");
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DECS, "Asteroid");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);
        return weatherValues;
    }

    public static void validateCursor(ContentValues expectedValues, Cursor valueCursor){
        Set<Map.Entry<String, Object>> valueSet =expectedValues.valueSet();
        for (Map.Entry<String, Object> entry: valueSet){
            String columnName= entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(-1 == idx);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
    }

    public void testInsertReadDb(){
        //dummy data for testing
        String testName = "North Pole";
        String testLocationSettings = "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.335;

        //getting writable database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values =  getLocationContentValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);
        //verify we got a row back
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row Id: " + locationRowId);

        Cursor cursor = db.query(LocationEntry.TABLE_NAME, //table to query
                null, //
                null, //column for the "where" clause
                null, //values for the "where" clause
                null, //column to group by
                null, //column to filter by row group
                null //sort order
        );
        if(cursor.moveToFirst()){
           validateCursor(values, cursor);

            ContentValues weatherValues = getWeatherContentValues(locationRowId);

            long weatherRowId;
            weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
            //verify we got a row back
            assertTrue(weatherRowId != -1);

            Cursor weatherCursor = db.query(WeatherEntry.TABLE_NAME, //table to query
                    null, //leaving "columns" null, just return all the columns
                    null, //column for  "where" clause
                    null, //values for  "where" clause
                    null, //column to group by
                    null, //column to filter by row group
                    null //sort order
            );

            if(weatherCursor.moveToFirst()){
                validateCursor(weatherValues, weatherCursor);
            }else {
                fail("No weather data returned!");
            }
        }else {
            fail("No value returned :(");
        }
    }
}
