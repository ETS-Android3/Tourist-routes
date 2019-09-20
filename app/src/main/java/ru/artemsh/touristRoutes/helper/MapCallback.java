package ru.artemsh.touristRoutes.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.security.auth.callback.Callback;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.createShowplace.CreateShowplaceBottomFragment;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.map.CustomMarkerClusteringDemoActivity;
import ru.artemsh.touristRoutes.map.Person;
import ru.artemsh.touristRoutes.model.Showplace;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class MapCallback implements LocationListener, OnMapReadyCallback, ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person> {
    private GoogleMap mMap;
    private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);
    private FragmentManager transaction;
    private Context context;

    private LocationManager locationManager;

    private IDatabase database;
    List<Showplace> showplaces;


    public MapCallback(FragmentManager transaction, IDatabase database, Context context) {
        this.transaction = transaction;
        this.database = database;
        this.context = context;
    }

    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        CameraUpdate center = CameraUpdateFactory.newLatLng(position());
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.setMyLocationEnabled(true);

        MarkerOptions markerOptions = new MarkerOptions();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CreateShowplaceBottomFragment bottomSheetDialog = null;
                for (Showplace place :
                        showplaces) {
                    if (place.getLatLng().equals(marker.getPosition())){
                        bottomSheetDialog = CreateShowplaceBottomFragment.getInstance(place, database, callbackUpdateMap);
                        break;
                    }
                }
                if (bottomSheetDialog == null){
                    bottomSheetDialog = CreateShowplaceBottomFragment.getInstance(new Showplace(marker.getPosition()), database, callbackUpdateMap);
                }

                bottomSheetDialog.show(transaction, context.getString(R.string.title_create_showplace));
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                showMarker();

                markerOptions.position(point);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                mMap.addMarker(markerOptions);
            }
        });


        mMap.clear();
        showMarker();
    }

    private ICallback callbackUpdateMap = new ICallback() {
        @Override
        public void request() {
            System.out.println("request");
            mMap.clear();
            showMarker();
        }
    };
    private void showMarker(){
        showplaces = database.getShowplaceAll();
        for (Showplace place :
                showplaces) {
            mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng()).title(place.getTitle()).zIndex(1.1f));
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {
        System.out.println("onClusterInfoWindowClick");
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Person item) {
        System.out.println("onClusterItemClick");
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person item) {
        System.out.println("onClusterItemInfoWindowClick");
        // Does nothing, but you could go into the user's profile page, for example.
    }


    @SuppressLint("MissingPermission")
    private LatLng position() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Location location= getLastBestLocation(locationManager);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        Toast.makeText(context, context.getResources().getText(R.string.wait_determined_location), Toast.LENGTH_SHORT).show();
        return new LatLng(location.getLongitude(), location.getLatitude());
    }

    private Location getLastBestLocation(LocationManager mLocationManager) {
        @SuppressLint("MissingPermission") Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        @SuppressLint("MissingPermission") Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("onLocationChanged");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        System.out.println("onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        System.out.println("onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        System.out.println("onProviderDisabled");
    }
}
