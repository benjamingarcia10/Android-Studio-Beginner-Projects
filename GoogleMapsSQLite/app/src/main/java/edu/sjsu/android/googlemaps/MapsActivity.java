package edu.sjsu.android.googlemaps;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    private final LatLng LOCATION_UNIV = new LatLng(37.335371, -121.881050);
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                drawMarker(latLng);
                ContentValues contentValues = new ContentValues();
                contentValues.put(LocationsDB.FIELD_LAT, latLng.latitude);
                contentValues.put(LocationsDB.FIELD_LNG, latLng.longitude);
                contentValues.put(LocationsDB.FIELD_ZOOM, map.getCameraPosition().zoom);
                LocationInsertTask insertTask = new LocationInsertTask();
                insertTask.execute(contentValues);
                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                map.clear();
                LocationDeleteTask deleteTask = new LocationDeleteTask();
                deleteTask.execute();
                Toast.makeText(getBaseContext(), "All markers are removed", Toast.LENGTH_LONG).show();
            }
        });

        map.addMarker(new MarkerOptions().position(LOCATION_CS).title("Find Me Here!"));
    }

    private void drawMarker(LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        map.addMarker(markerOptions);
    }

    public void onClick_CS(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18);
        map.animateCamera(update);
    }

    public void onClick_Univ(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14);
        map.animateCamera(update);
    }

    public void onClick_City(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10);
        map.animateCamera(update);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = LocationsContentProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int locationCount = 0;
        double lat = 0;
        double lng = 0;
        float zoom = 0;

        locationCount = data.getCount();
        data.moveToFirst();

        for (int i = 0; i < locationCount; i++) {

            lat = data.getDouble(data.getColumnIndex(LocationsDB.FIELD_LAT));
            lng = data.getDouble(data.getColumnIndex(LocationsDB.FIELD_LNG));
            zoom = data.getFloat(data.getColumnIndex(LocationsDB.FIELD_ZOOM));
            LatLng location = new LatLng(lat, lng);
            drawMarker(location);
            data.moveToNext();
        }

        if (locationCount > 0) {
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }
}
