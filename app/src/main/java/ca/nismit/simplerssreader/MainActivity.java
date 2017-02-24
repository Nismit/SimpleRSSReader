package ca.nismit.simplerssreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ca.nismit.simplerssreader.fragment.AddFragment;
import ca.nismit.simplerssreader.fragment.FeedsListFragment;
import ca.nismit.simplerssreader.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        setUpViews();
    }

    @Override
    protected void onStart() {
        //Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        //Log.d(TAG, "onStop");
        super.onStop();
    }

    void setUpViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main, MainFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_rss_feed) {
            replaceFragment(AddFragment.newInstance());
            return true;
        } else if(id == R.id.edit_rss_database) {
            replaceFragment(FeedsListFragment.newInstance());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Log.d(TAG, "onBackPressed: Stack " + fm.getBackStackEntryCount());
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return;
        }

        super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main);
        if (currentFragment.getClass() == fragment.getClass()) {
            // nothing to do
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.activity_main, fragment)
                .addToBackStack(null)
                .commit();
    }
}
