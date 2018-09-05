package sunnyrain.android.example.com.sunnyrain.data;

import android.provider.BaseColumns;

public class WeatherContract {
    public static final class LocationEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "location";
        //location query to the API from settings
        public static final String COLUMN_LOCATION_SETTING = "location_settings";
        //readable location string(lagos onstaed of 100242)
        public static final String COLUMN_CITY_NAME = "city_name";
        //longitude and latitude from APi to pinpoint the exact location from the map
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
    }
    public static final class WeatherEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "weather";
        //column with foreign key into the location table
        public static final String COLUMN_LOC_KEY = "location_id";
        //date, stored as text with the format yyyy-mm-dd
        public static final String COLUMN_DATETEXT = "date";
        //weather id as returned by the API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DECS = "short_desc";
        //min and max temp for the day
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        //humidity is stored as float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";
        //pressure is stored as float representing pressure
        public static final String COLUMN_PRESSURE = "pressure";
        //windspeed is stored as float representing windspeed mph
        public static final String COLUMN_WIND_SPEED = "wind";
        //meteorological degrees stored as float
        public static final String COLUMN_DEGREES = "degrees";
    }

}
