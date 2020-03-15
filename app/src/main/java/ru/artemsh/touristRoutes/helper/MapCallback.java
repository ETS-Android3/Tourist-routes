package ru.artemsh.touristRoutes.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;

import java.util.List;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.createShowplace.CreateShowplaceBottomFragment;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.map.Person;
import ru.artemsh.touristRoutes.model.Showplace;

import static android.content.Context.LOCATION_SERVICE;

public class MapCallback implements LocationListener, OnMapReadyCallback, ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>,
        ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person> {
    private GoogleMap mMap;
    private ClusterManager<Person> mClusterManager;
    private FragmentManager transaction;
    private Context context;
    private SharedPreferences sPref;

    private static final String[] CONSTANTS = {"last_lat","last_lon","last_zoom"};

    private LocationManager locationManager;

    private Polyline polyline;

    private IDatabase database;
    private String myApiKeyGoogle = null;
    List<Showplace> showplaces;


    public MapCallback(FragmentManager transaction, IDatabase database, Context context) {
        this.transaction = transaction;
        this.database = database;
        this.context = context;

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            myApiKeyGoogle = bundle.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MapCallback", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("MapCallback", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void pause(){
        if (mMap!=null){
            sPref = PreferenceManager.getDefaultSharedPreferences(context);

            SharedPreferences.Editor ed = sPref.edit();
            ed.putFloat(CONSTANTS[0], (float) mMap.getCameraPosition().target.latitude);
            ed.putFloat(CONSTANTS[1], (float) mMap.getCameraPosition().target.longitude);
            ed.putFloat(CONSTANTS[2],  mMap.getCameraPosition().zoom);

            ed.commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;

        sPref = PreferenceManager.getDefaultSharedPreferences(context);//context.getPreferences(MODE_PRIVATE);
        double lat = sPref.getFloat(CONSTANTS[0],0.0f);
        double lon = sPref.getFloat(CONSTANTS[1],0.0f);
        float zoomFloat = sPref.getFloat(CONSTANTS[2],30.0f);

        CameraUpdate center = CameraUpdateFactory.newLatLng(position());
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(30);

        if (lat!=0.0f&&lon!=0.0f){
            center = CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
            zoom = CameraUpdateFactory.zoomTo(zoomFloat);
        }

        mMap.getUiSettings().setAllGesturesEnabled(true);
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
                        bottomSheetDialog = CreateShowplaceBottomFragment.getInstance(place, database, callbackUpdateMap, callbackRoute);
                        break;
                    }
                }
                if (bottomSheetDialog == null){
                    bottomSheetDialog = CreateShowplaceBottomFragment.getInstance(new Showplace(marker.getPosition()), database, callbackUpdateMap, callbackRoute);
                }
                // https://habr.com/ru/post/341548/
                // Написание построения маршрута

                bottomSheetDialog.show(transaction, context.getString(R.string.title_create_showplace));
                return true;
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
            mMap.clear();
            showMarker();
        }
    };

    private ICallbackRoute callbackRoute = new ICallbackRoute(){
        @Override
        public void request(double lat, double lng) {
            LatLng latLng = position();
            destination(latLng.latitude, latLng.longitude, lat, lng);
        }
    };

    private void destination(double startLat, double startLong, double finishLat, double finishLong){
        GeoApiContext context2 = new GeoApiContext().setApiKey(myApiKeyGoogle);

        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context2);
        apiRequest.origin(new com.google.maps.model.LatLng(startLat, startLong));
        apiRequest.destination(new com.google.maps.model.LatLng(finishLat, finishLong));
        apiRequest.mode(TravelMode.DRIVING); //set travelling mode

        apiRequest.setCallback(new com.google.maps.PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                DirectionsRoute[] routes = result.routes;
                EncodedPolyline encodedPolyline = routes[0].overviewPolyline;
                // https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial
                // https://stackoverflow.com/questions/17425499/how-to-draw-interactive-polyline-on-route-google-maps-v2-android

                PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
                for (com.google.maps.model.LatLng latLng : encodedPolyline.decodePath()) {
                    options.add(new LatLng(latLng.lat, latLng.lng));
                }

                polyline = mMap.addPolyline(options);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    private void showMarker(){
        showplaces = database.getAll();
        for (Showplace place :
                showplaces) {
            if (place.getPlace()==Showplace.TypePlace.SHOWPLACE){
                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng()).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(place.getTitle()).zIndex(1.1f));
            }else{
                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng()).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(place.getTitle()).zIndex(1.1f));
            }
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 30, this);
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
