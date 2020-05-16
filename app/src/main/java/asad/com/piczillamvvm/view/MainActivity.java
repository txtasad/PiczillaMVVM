package asad.com.piczillamvvm.view;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import asad.com.piczillamvvm.R;


public class MainActivity extends AppCompatActivity {

    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        MainFragment mainFragment = (MainFragment) fm.findFragmentByTag("main_fragment");
        // create the fragment and data the first time
        if (mainFragment == null) {
            // add the fragment
            mainFragment = new MainFragment();
            fm.beginTransaction().add(R.id.activity_main, mainFragment, "main_fragment").commit();
        }
    }
}
