package sunnyrain.android.example.com.sunnyrain;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.ActionProvider;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
private static final String LOG_TAG= DetailFragment.class.getSimpleName();
    private static final String SUNNYRAIN_SHARE_HASHTAG= "#sunnyrain";
    private String mForecastStr;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null && intent.hasExtra(intent.EXTRA_TEXT)){
            mForecastStr = intent.getStringExtra(intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
        }
        return  rootView;
    }
    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr+ SUNNYRAIN_SHARE_HASHTAG);
        return  shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        //provider to change the share intent
        ShareActionProvider mshareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(menuItem);
        //attaching an intent to the shareActionProvider
        if (mshareActionProvider != null){
            mshareActionProvider.setShareIntent(createShareForecastIntent());
        }else {
            Log.d(LOG_TAG, "Share Action Provider is null");
        }
    }
}
