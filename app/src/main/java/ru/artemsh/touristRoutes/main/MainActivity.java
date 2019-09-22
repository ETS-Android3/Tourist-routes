package ru.artemsh.touristRoutes.main;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.createShowplace.CreateShowplaceBottomFragment;
import ru.artemsh.touristRoutes.database.DBHelper;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.database.LocalDatabase;
import ru.artemsh.touristRoutes.map.CustomMarkerClusteringDemoActivity;
import ru.artemsh.touristRoutes.showplace.ShowplaceFragment;
import ru.artemsh.touristRoutes.wasPlace.WasPlaceFramgent;


public class MainActivity extends AppCompatActivity {

    private Fragment showplace;
    private Fragment wasPlace;
    private Fragment map;

    private BottomNavigationView navView;
    private FloatingActionButton actionButton;

    private IDatabase database;

    /*
            android:configChanges="orientation"
            android:screenOrientation="portrait"
            */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        database = DBHelper.initialization(getBaseContext());

        showplace = new ShowplaceFragment();
        wasPlace = new WasPlaceFramgent();
        map = new CustomMarkerClusteringDemoActivity();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_showplace);

        actionButton = findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateShowplaceBottomFragment bottomSheetDialog = CreateShowplaceBottomFragment.getInstance(null, database, null);
                bottomSheetDialog.show(getSupportFragmentManager(), getResources().getString(R.string.title_create_showplace));
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {//getString(R.string.title_showplace)
                case R.id.navigation_showplace:
                    replaceFragment(showplace);
                    break;
                case R.id.navigation_was_place:
                    replaceFragment(wasPlace);
                    break;
                case R.id.navigation_map:
                    replaceFragment(map);
                    break;
                    default:
                        return false;
            }
            return true;
        }
    };

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
        fragmentTransaction.commit();
    }
}
