package ca.nismit.simplerssreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ca.nismit.simplerssreader.fragment.AddFragment;
import ca.nismit.simplerssreader.fragment.FeedsListFragment;
import ca.nismit.simplerssreader.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            setUpViews();
        }
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

    /**
     * If user pressed back button, it will be shown previous fragment
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return;
        }

        super.onBackPressed();
    }

    /**
     * Making a main fragment which is activity_main
     *
     * @return void
     */
    private void setUpViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main, MainFragment.newInstance())
                .commit();
    }

    /**
     * Replace other fragment when the fragment is not same fragment
     * with animation
     *
     * @param fragment
     * @see res/anim/
     */
    private void replaceFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main);
        if (currentFragment.getClass() == fragment.getClass()) {
            // nothing to do
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_down, R.anim.slide_out_down,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.activity_main, fragment)
                .addToBackStack(null)
                .commit();
    }
}
