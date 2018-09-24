package sunnyrain.android.example.com.sunnyrain.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

public class weatherProvider extends ContentProvider {
    private  static final  int WEATHER= 100;
    private  static final  int WEATHER_WITH_LOCATION= 101;
    private  static final  int WEATHER_WITH_LOCATION_AND_DATE= 102;
    private  static final  int LOCATION= 300;
    private  static final  int LOCATION_ID= 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private WeatherDbHelper mOpenHelper;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static {
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();
        sWeatherByLocationSettingQueryBuilder.setTables(
                WeatherContract.WeatherEntry.TABLE_NAME + "INNER JOIN " +
                        WeatherContract.LocationEntry.TABLE_NAME + " ON " +
                        WeatherContract.WeatherEntry.TABLE_NAME + "." +
                        WeatherContract.WeatherEntry.COLUMN_LOC_KEY + " = " +
                        WeatherContract.LocationEntry.TABLE_NAME + "." +
                        WeatherContract.LocationEntry._ID);
    }

    private static final String sLocationSettingSelection =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";
    private static final String sLocationSettingWithStartDateSelection =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND" +
            WeatherContract.WeatherEntry.COLUMN_DATETEXT + " >= ? ";
//
//    private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//    String locationSettings = WeatherContract.WeatherEntry.getWeatherLocationFromUri(uri);
//
//    }

    private static UriMatcher buildUriMatcher(){
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final  String authority = WeatherContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, WeatherContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER+ "/*", WEATHER_WITH_LOCATION);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER+ "/*", WEATHER_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, WeatherContract.PATH_LOCATION + "/#", LOCATION_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper= new WeatherDbHelper(getContext());
        return true;
    }

    //getType is used to return the mime type associated with data at a given uri
    @Override
    public String getType(Uri uri) {
        final  int match = sUriMatcher.match(uri);
        switch (match){
            case WEATHER_WITH_LOCATION_AND_DATE:
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case WEATHER:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case LOCATION:
                return WeatherContract.LocationEntry.CONTENT_TYPE;
            case LOCATION_ID:
                return WeatherContract.LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case WEATHER_WITH_LOCATION_AND_DATE:
            {
                retCursor = null;
                break;
            }
            case WEATHER_WITH_LOCATION:
            {
                retCursor = null;
                break;
            }
            case WEATHER:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LOCATION_ID:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.LocationEntry.TABLE_NAME,
                        projection,
                        WeatherContract.LocationEntry._ID + "= '" + ContentUris.parseId(uri),
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LOCATION:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //cursor register content resolver to watch for changes to uri and it's desendant
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 1;
    }
}
