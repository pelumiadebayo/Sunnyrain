package sunnyrain.android.example.com.sunnyrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
//import android.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    public boolean onCreateOptionMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map){
            openPreferenceLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void  openPreferenceLocationInMap(){
        //reading from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        //using the uri builder for showing location found on the intent
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", location).build();
        //intent to lauch the map app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        //start the intent activity if it resolve successfully
        if (intent.resolveActivity(getPackageManager()) !=null){
            startActivity(intent);
        }else {
            Log.d("MainActivity", "couldn't call" + location + "no such location");
        }
    }
}
