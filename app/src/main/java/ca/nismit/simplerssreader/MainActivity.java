package ca.nismit.simplerssreader;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ca.nismit.simplerssreader.fragment.DisplayTestFragment;
import ca.nismit.simplerssreader.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
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
            replaceFragment(DisplayTestFragment.newInstance());
            return true;
        }
        return super.onOptionsItemSelected(item);
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
