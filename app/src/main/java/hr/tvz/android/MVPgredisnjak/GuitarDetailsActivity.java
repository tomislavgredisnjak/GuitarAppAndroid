package hr.tvz.android.MVPgredisnjak;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuitarDetailsActivity extends AppCompatActivity {

    @BindView(R.id.guitarName)
    TextView guitarName;
    @BindView(R.id.guitarType)
    TextView guitarType;
    @BindView(R.id.numberOfFrets)
    TextView numberOfFrets;
    @BindView(R.id.numberOfStrings)
    TextView numberOfStrings;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.guitarImage)
    SimpleDraweeView guitarImage;
    @BindString(R.string.guitarType)
    String guitarTypeString;
    @BindString(R.string.numberOfStrings)
    String numberOfStringsString;
    @BindString(R.string.numberOfFrets)
    String numberOfFretsString;
    @BindString(R.string.price)
    String priceString;
    @BindString(R.string.kuna)
    String kuna;
    @BindString(R.string.share)
    String share;
    @BindString(R.string.da)
    String da;
    @BindString(R.string.ne)
    String ne;
    @BindString(R.string.shared)
    String shared;
    @BindString(R.string.shareRejected)
    String shareRejected;
    String guitarFullName;

    private static final String API_URL = "http://10.0.2.2:8080/api/";
    MediaPlayer electricPlayer;
    MediaPlayer acousticPlayer;
    MediaPlayer classicalPlayer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.guitar_detail);

        Integer idGitare = Integer.parseInt(getIntent().getExtras().getString("guitar"));

        final Guitar[] guitar = {new Guitar()};
        InterfacePremaServisu client =
                ServiceGenerator.createService(InterfacePremaServisu.class, API_URL);

        Call<Guitar> call = client.dohvatiGitaru(idGitare);

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);

        call.enqueue(new Callback<Guitar>() {

            @Override
            public void onResponse(Call<Guitar> call, Response<Guitar> response) {

                if (response.isSuccessful()) {

                    guitar[0] = response.body();
                    adapter.clear();
                    adapter.add(guitar[0].getName());


                    if(guitar[0].type.equals("electric")){
                        electricPlayer = electricPlayer.create(GuitarDetailsActivity.this, R.raw.electric);
                        electricPlayer.start();

                    }else if(guitar[0].type.equals("acoustic")){
                        acousticPlayer = acousticPlayer.create(GuitarDetailsActivity.this, R.raw.acoustic);
                        acousticPlayer.start();

                    }else if(guitar[0].type.equals("classical")){
                        classicalPlayer = classicalPlayer.create(GuitarDetailsActivity.this, R.raw.classical);
                        classicalPlayer.start();

                    }

                    DecimalFormat df = new DecimalFormat("#.##");

                    guitarName.setText(guitar[0].getName().replaceAll("_", " "));
                    guitarType.setText(guitarTypeString + " " + guitar[0].getType());
                    numberOfFrets.setText(numberOfFretsString + " " + guitar[0].getNumberOfFrets());
                    numberOfStrings.setText(numberOfStringsString + " " + guitar[0].getNumberOfStrings());
                    price.setText(priceString + " " + df.format(guitar[0].getPrice()) + " " + kuna);
                    Uri uri = Uri.parse(guitar[0].getImageLink());
                    guitarImage.setImageURI(uri);

                    guitarFullName = (guitar[0].getName());

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacija);
                    guitarImage.startAnimation(animation);

                    Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacija2);
                    guitarName.startAnimation(animation2);
                    guitarType.startAnimation(animation2);
                    numberOfFrets.startAnimation(animation2);
                    numberOfStrings.startAnimation(animation2);
                    price.startAnimation(animation2);
                }
            }

            @Override
            public void onFailure(Call<Guitar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        ButterKnife.bind(this);

        guitarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuitarImageActivity.class);
                intent.putExtra("guitar", guitar[0]);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.share) {
            startAlert();
            return true;
        }else{
            return false;
        }

    }

    public void startAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(share + guitarFullName + "?")
                .setPositiveButton(da, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        {
                            //salje broadcast na testnu aplikaciju iz 4. predavanja
                            String uniqueActionString = "hr.android.broadcast.testbc";
                            Intent broadcastIntent = new Intent(uniqueActionString);
                            broadcastIntent.putExtra(guitarFullName,1);
                            sendBroadcast(broadcastIntent);
                            Toast.makeText(getApplicationContext(), shared, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(ne, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), shareRejected, Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .create()
                .show();
    }
}
