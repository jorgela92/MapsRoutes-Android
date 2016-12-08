package es.fingercode.racehub.mapsroute.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.fingercode.racehub.mapsroute.R;
import es.fingercode.racehub.mapsroute.adapter.ListRoutesAdapter;
import es.fingercode.racehub.mapsroute.adapter.RecyclerViewOnItemClickListener;
import es.fingercode.racehub.mapsroute.api.Api;
import es.fingercode.racehub.mapsroute.manager.RouteManager;
import es.fingercode.racehub.mapsroute.manager.UserManager;
import es.fingercode.racehub.mapsroute.manager.WayPointManager;
import es.fingercode.racehub.mapsroute.model.Route;
import es.fingercode.racehub.mapsroute.model.WayPoint;
import es.fingercode.racehub.mapsroute.services.LocationProvider;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        BottomNavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{

    private SupportMapFragment mapFragment_location, mapFragment_point;
    private GoogleMap map_location, map_point;
    private static Activity activity;
    private Context context;
    private LinearLayout linearLayout_routes, linearLayout_points, linearLayout_location;
    private BottomNavigationView bottomNavigationView;
    private TextView text1, text2;
    private Button end_route;
    private Route route;
    private int positionRoutes = 1;
    private Intent msgIntent = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static Activity getActivity() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = this;
        createUi();
        msgIntent = new Intent(MainActivity.this, LocationProvider.class);
        startService(msgIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("speedExceeded"));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double currentLatitude = intent.getDoubleExtra("latitude", 0);
            Double currentLongitude = intent.getDoubleExtra("longitude", 0);
            String currentAdderess = intent.getStringExtra("address");
            handleLocation(currentLatitude, currentLongitude);
            String title = getFechaHora();
            if (route !=null) {
                onNewLocation(currentLatitude, currentLongitude, currentAdderess,title);
            }
        }
    };

    private void onNewLocation(Double lat, Double lon, String description, String title) {
        WayPointManager.getInstance(getApplicationContext()).addWaypoint(route.getId(), title, description, lat+"", lon+"", new Api.Listener<WayPoint>() {
            @Override
            public void onSucces(WayPoint data) {

            }

            @Override
            public void onError(String description) {

            }
        });
    }

    public void createUi() {
        linearLayout_routes = (LinearLayout) findViewById(R.id.layout_rutes);
        linearLayout_points = (LinearLayout) findViewById(R.id.layout_points);
        linearLayout_location = (LinearLayout) findViewById(R.id.layout_location);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(new ListRoutesAdapter(UserManager.getInstance(context).getUserLogged().getRoutes(),new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                positionRoutes = position;
            }
        }));
        mapFragment_location = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_location);
        mapFragment_location.getMapAsync(this);
        mapFragment_point = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_points);
        mapFragment_point.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map_point = googleMap;
                map_point.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map_point.setMyLocationEnabled(true);
                map_point.getUiSettings().setMyLocationButtonEnabled(true);
                map_point.getUiSettings().setZoomControlsEnabled(true);
                map_point.getUiSettings().setCompassEnabled(true);
                map_point.getUiSettings().setIndoorLevelPickerEnabled(true);
                map_point.getUiSettings().setZoomGesturesEnabled(true);
                map_point.getUiSettings().setRotateGesturesEnabled(true);
                map_point.getUiSettings().setScrollGesturesEnabled(true);
                map_point.getUiSettings().setTiltGesturesEnabled(true);
            }
        });
        end_route = (Button) findViewById(R.id.end_route);
        end_route.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map_location = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map_location.setMyLocationEnabled(true);
        map_location.getUiSettings().setMyLocationButtonEnabled(true);
        map_location.getUiSettings().setZoomControlsEnabled(true);
        map_location.getUiSettings().setCompassEnabled(true);
        map_location.getUiSettings().setIndoorLevelPickerEnabled(true);
        map_location.getUiSettings().setZoomGesturesEnabled(true);
        map_location.getUiSettings().setRotateGesturesEnabled(true);
        map_location.getUiSettings().setScrollGesturesEnabled(true);
        map_location.getUiSettings().setTiltGesturesEnabled(true);
    }

    public void handleLocation(Double latitude, Double longitude) {
        LatLng ubication = new LatLng(latitude, longitude);
        map_location.clear();
        map_location.addMarker(new MarkerOptions().position(ubication).title("Traking"));
    }
    public void painWayPoint(int idRoute){
        map_point.clear();
        PolylineOptions rectOptions = new PolylineOptions();
        for (int i = 0; i<UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().size();i++) {
            rectOptions
                    .add(new LatLng(UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().get(i).getLatitude(),
                            UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().get(i).getLongitude()));
        }
        map_point.addMarker(new MarkerOptions().position(new LatLng(UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().get(0).getLatitude(),
                UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().get(0).getLongitude())));
        map_point.addMarker(new MarkerOptions().position(new LatLng(UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().get(UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().size()-1).getLatitude(),
                UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().get(UserManager.getInstance(context).getUserLogged().getRoutes().get(idRoute).getWaypoints().size()-1).getLongitude())));
        map_point.addPolyline(rectOptions);
    }
    public String getFechaHora() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(calendar.getTime());
        String fechaHora;
        if (date.getMinutes() <= 9) {
            fechaHora = formattedDate + " a las " + date.getHours() + ": 0" + date.getMinutes();
        } else {
            fechaHora = formattedDate + " a las " + date.getHours() + ":" + date.getMinutes();
        }
        return fechaHora;
    }

    private void speedExceedMessageToActivity(boolean b) {
        Intent intent = new Intent("speedEneableDiseable");
        sendEnableDiseable(intent, b);
    }

    private void sendEnableDiseable(Intent intent, boolean location) {
        intent.putExtra("enableDiseable", location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rutes_item:
                linearLayout_routes.setVisibility(View.VISIBLE);
                linearLayout_points.setVisibility(View.INVISIBLE);
                linearLayout_location.setVisibility(View.INVISIBLE);
                break;
            case R.id.points_item:
                linearLayout_routes.setVisibility(View.INVISIBLE);
                linearLayout_points.setVisibility(View.VISIBLE);
                linearLayout_location.setVisibility(View.INVISIBLE);
                painWayPoint(positionRoutes);
                break;
            case R.id.location_item:
                linearLayout_routes.setVisibility(View.INVISIBLE);
                linearLayout_points.setVisibility(View.INVISIBLE);
                linearLayout_location.setVisibility(View.VISIBLE);
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                final View dialog_layout = getLayoutInflater().inflate(R.layout.dialog_route, null);
                text1 = (EditText) dialog_layout.findViewById(R.id.route_title);
                text2 = (EditText) dialog_layout.findViewById(R.id.route_description);
                Button button = (Button) dialog_layout.findViewById(R.id.button_ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RouteManager.getInstance(context).addRoute(UserManager.getInstance(context).getUserLogged().getId(), text1.getText().toString()+".", text2.getText().toString()+".", new Api.Listener<Route>() {
                            @Override
                            public void onSucces(Route data) {
                                route = data;
                            }

                            @Override
                            public void onError(String description) {

                            }
                        });
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialog_layout);
                alertDialog.show();
                break;
        }
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.end_route:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                alertDialogBuilder
                        .setMessage("¿Desea terminar la ruta?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                speedExceedMessageToActivity(false);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }
}
