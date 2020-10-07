package hr.tvz.android.MVPgredisnjak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "guitar";

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
    @BindString(R.string.drawableUrl)
    String drawableUrl;
    @BindString(R.string.kuna)
    String kuna;

    private Guitar mGuitar;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mGuitar = getArguments().getParcelable(ARG_ITEM_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (mGuitar != null) {


            DecimalFormat df = new DecimalFormat("#.##");

            Uri uri = Uri.parse(mGuitar.getImageLink());
            guitarImage.setImageURI(uri);
            guitarName.setText(mGuitar.getName().replaceAll("_", " "));
            guitarType.setText(guitarTypeString + " " + mGuitar.getType());
            numberOfFrets.setText(numberOfFretsString + " " + mGuitar.getNumberOfFrets());
            numberOfStrings.setText(numberOfStringsString + " " + mGuitar.getNumberOfStrings());
            price.setText(priceString + " " + df.format(mGuitar.getPrice()) + " " + kuna);
            Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.animacija);
            guitarImage.startAnimation(animation);

            Animation animation2 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.animacija2);
            guitarName.startAnimation(animation2);
            guitarType.startAnimation(animation2);
            numberOfFrets.startAnimation(animation2);
            numberOfStrings.startAnimation(animation2);
            price.startAnimation(animation2);

            guitarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), GuitarImageActivity.class);
                    intent.putExtra("guitar", mGuitar);
                    startActivity(intent);
                }

            });
        }
        return rootView;
    }


}
