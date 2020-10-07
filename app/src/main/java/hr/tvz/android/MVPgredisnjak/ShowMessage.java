package hr.tvz.android.MVPgredisnjak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMessage extends AppCompatActivity {

    @BindView(R.id.guitarNameMessage)
    TextView guitarName;
    @BindView(R.id.guitarTypeMessage)
    TextView guitarType;
    @BindView(R.id.numberOfFretsMessage)
    TextView numberOfFrets;
    @BindView(R.id.numberOfStringsMessage)
    TextView numberOfStrings;
    @BindView(R.id.priceMessage)
    TextView price;
    @BindView(R.id.guitarImageMessage)
    ImageView guitarImage;
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
    @BindString(R.string.samo)
    String samo;
    private static final String API_URL = "http://10.0.2.2:8080/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        ButterKnife.bind(this);


        InterfacePremaServisu client =
                ServiceGenerator.createService(InterfacePremaServisu.class, API_URL);

        Call<Guitar> call = client.dohvatiGitaru(3);
        final Guitar[] mGuitar = {new Guitar()};
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);

        call.enqueue(new Callback<Guitar>() {

            @Override
            public void onResponse(Call<Guitar> call, Response<Guitar> response) {

                if (response.isSuccessful()) {

                    mGuitar[0] = response.body();
                    adapter.clear();

                    Uri uri = Uri.parse(mGuitar[0].getImageLink());
                    guitarImage.setImageURI(uri);

                    guitarName.setText(mGuitar[0].getName().replaceAll("_", " "));
                    guitarType.setText(guitarTypeString + " " + mGuitar[0].getType());
                    numberOfFrets.setText(numberOfFretsString + " " + mGuitar[0].getNumberOfFrets());
                    numberOfStrings.setText(numberOfStringsString + " " + mGuitar[0].getNumberOfStrings());
                    price.setText(samo + " " + priceString + " " + mGuitar[0].getPrice() + " " + kuna);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacija);
                    guitarImage.startAnimation(animation);

                    Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacija2);
                    guitarName.startAnimation(animation2);
                    guitarType.startAnimation(animation2);
                    numberOfFrets.startAnimation(animation2);
                    numberOfStrings.startAnimation(animation2);
                    price.startAnimation(animation2);

                    guitarImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), GuitarImageActivity.class);
                            intent.putExtra("guitar", mGuitar);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Guitar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
