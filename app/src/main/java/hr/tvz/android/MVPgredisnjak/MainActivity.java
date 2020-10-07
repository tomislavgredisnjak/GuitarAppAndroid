package hr.tvz.android.MVPgredisnjak;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleObserver;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements ItemListFragment.Callbacks, LifecycleObserver {

    private static final String API_URL = "http://10.0.2.2:8080/api/";

    MediaPlayer electricPlayer;
    MediaPlayer acousticPlayer;
    MediaPlayer classicalPlayer;

    public static String MOJ_KANAL = "mojKanal";
    @BindString(R.string.electric)
    public String electric;
    @BindString(R.string.acoustic)
    public String acoustic;
    @BindString(R.string.classical)
    public String classical;
    @BindString(R.string.battery)
    public String battery;
    @BindString(R.string.nightMode)
    public String nightMode;
    @BindString(R.string.isNightMode)
    public String isNightMode1;
    @BindString(R.string.channelDesc)
    public String channelDesc;

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;

    public MainActivity() {
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }

    SharedPreferences sharedPreferences;

    private boolean mTwoPane;

    private BroadcastReceiver bcr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, battery, Toast.LENGTH_LONG).show();
            String nightmode= nightMode;
            sharedPreferences = getSharedPreferences(nightmode, Context.MODE_PRIVATE);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            saveNightModeState(true);
            recreate();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout
                ,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (findViewById(R.id.item_detail_container) != null) {

            mTwoPane = true;

            ((ItemListFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

        registerReceiver(bcr, new IntentFilter(Intent.ACTION_BATTERY_LOW));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(MOJ_KANAL, "Guitar channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDesc);
            NotificationManager man = (NotificationManager)(getSystemService(Context.NOTIFICATION_SERVICE));
            man.createNotificationChannel(channel);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Main activity", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();

                        Log.d("Main activity", token);
                    }
                });
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {

            Bundle arguments = new Bundle();

            final Guitar[] guitar = {new Guitar()};
            InterfacePremaServisu client =
                    ServiceGenerator.createService(InterfacePremaServisu.class, API_URL);

            Call<Guitar> call = client.dohvatiGitaru(Integer.parseInt(id));

            final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);

            call.enqueue(new Callback<Guitar>() {

                @Override
                public void onResponse(Call<Guitar> call, Response<Guitar> response) {

                    if (response.isSuccessful()) {

                        guitar[0] = response.body();
                        adapter.clear();
                        adapter.add(guitar[0].getName());

                        if(guitar[0].type.equals("electric")){
                            electricPlayer = electricPlayer.create(MainActivity.this, R.raw.electric);
                            electricPlayer.start();

                        }else if(guitar[0].type.equals("acoustic")){
                            acousticPlayer = acousticPlayer.create(MainActivity.this, R.raw.acoustic);
                            acousticPlayer.start();

                        }else if(guitar[0].type.equals("classical")){
                            classicalPlayer = classicalPlayer.create(MainActivity.this, R.raw.classical);
                            classicalPlayer.start();

                        }

                        arguments.putParcelable(ItemDetailFragment.ARG_ITEM_ID, guitar[0]);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    }
                }

                @Override
                public void onFailure(Call<Guitar> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        } else {
            Intent detailIntent = new Intent(this, GuitarDetailsActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }



    private void saveNightModeState(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String isNightMode = isNightMode1;
        editor.putBoolean(isNightMode,b);
        editor.apply();
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.item_list);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
        unregisterReceiver(bcr);

        }catch (IllegalArgumentException e){
            System.out.println("IllegalArgumentException");
        }
    }
}
