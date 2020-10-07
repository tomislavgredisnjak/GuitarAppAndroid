package hr.tvz.android.MVPgredisnjak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GuitarImageActivity extends AppCompatActivity {

    @BindView(R.id.guitarPicture)
    SimpleDraweeView guitarPicture;

    @BindView(R.id.search)
    Button search;

    @BindString(R.string.google)
    String google;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.guitar_image);

        ButterKnife.bind(this);

        Guitar guitar = getIntent().getExtras().getParcelable("guitar");

        Uri uri = Uri.parse(guitar.getImageLink());
        guitarPicture.setImageURI(uri);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacija2);
        guitarPicture.startAnimation(animation);

        search.startAnimation(animation);

        guitarPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = google + guitar.getName().replace("_", " ").toLowerCase();
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = google + guitar.getName().replace("_", " ").toLowerCase();
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
    }
}

