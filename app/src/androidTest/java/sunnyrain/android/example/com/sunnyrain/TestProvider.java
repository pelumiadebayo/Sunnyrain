package sunnyrain.android.example.com.sunnyrain;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import sunnyrain.android.example.com.sunnyrain.data.WeatherContract.LocationEntry;
import sunnyrain.android.example.com.sunnyrain.data.WeatherContract.WeatherEntry;
import sunnyrain.android.example.com.sunnyrain.data.WeatherDbHelper;


public class TestProvider extends AndroidTestCase{
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void  testDeleteDb() throws  Throwable{
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
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

    public void testGetType(){
        //content://sunnyrain.android.example.com.sunnyrain/weather/
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        //vnd.android.cursor.dir/sunnyrain.android.example.com.sunnyrain/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testLocation = "94074";
        //content://sunnyrain.android.example.com.sunnyrain/weather/94074
        type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocation(testLocation));
        //vnd.android.cursor.dir/sunnyrain.android.example.com.sunnyrain/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testDate = "20180907";
        //content://sunnyrain.android.example.com.sunnyrain/weather/94074/20180907
        type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
        //vnd.android.cursor.item/sunnyrain.android.example.com.sunnyrain/weather
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);

        //content://sunnyrain.android.example.com.sunnyrain/location/
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        //vnd.android.cursor.dir/sunnyrain.android.example.com.sunnyrain/location
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        //content://sunnyrain.android.example.com.sunnyrain/location/1
        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        //vnd.android.cursor.item/sunnyrain.android.example.com.sunnyrain/location
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testInsertReadProvider(){
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
        Cursor cursor = mContext.getContentResolver().query(LocationEntry.buildLocationUri(locationRowId),
                null, // leaving column null
                null, //col for where clause
                null, //value for where clause
                null //sort order
        );
        if(cursor.moveToFirst()){
           validateCursor(values, cursor);

            ContentValues weatherValues = getWeatherContentValues(locationRowId);
            long weatherRowId;
            weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
            //verify we got a row back
            assertTrue(weatherRowId != -1);
            Cursor weatherCursor = mContext.getContentResolver().query(WeatherEntry.CONTENT_URI,
                    null, // leaving column null
                    null, //col for where clause
                    null, //value for where clause
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
