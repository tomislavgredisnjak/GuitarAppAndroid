package hr.tvz.android.MVPgredisnjak;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import android.widget.Toast;

import androidx.annotation.RequiresApi;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GuitarWidgetProvider extends AppWidgetProvider {

    List<Guitar> guitars = new ArrayList<>();
    public static final String UPDATE_WIDGET = "GuitarWidgetProvider";
    private static final String API_URL = "http://10.0.2.2:8080/api/";
    public AppWidgetTarget appWidgetTarget;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final Integer[] idGitare = new Integer[1];
        final Guitar[] guitar = {new Guitar()};
        InterfacePremaServisu client =
                ServiceGenerator.createService(InterfacePremaServisu.class, API_URL);

        Call<List<Guitar>> callId = client.dohvatiGitare();

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_list_item_1);

        callId.enqueue(new Callback<List<Guitar>>() {

            @Override
            public void onResponse(Call<List<Guitar>> callId, Response<List<Guitar>> response) {

                if (response.isSuccessful()) {
                    guitars = response.body();
                    adapter.clear();
                    idGitare[0] = guitars.size() + 1;

                    Call<Guitar> call = client.dohvatiGitaru(idGitare[0]);

                    call.enqueue(new Callback<Guitar>() {

                        @Override
                        public void onResponse(Call<Guitar> call, Response<Guitar> response) {
                            Fresco.initialize(context);
                            if (response.isSuccessful()) {

                                guitar[0] = response.body();

                                for (int appWidgetId : appWidgetIds) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.guitar_widget);
                                    appWidgetTarget = new AppWidgetTarget(context, views, R.id.guitarImageWidget, appWidgetIds);
                                    views.setOnClickPendingIntent(R.id.buttonGuitar, pendingIntent);
                                    views.setTextViewText(R.id.textWidget, guitar[0].getName());
                                    views.setInt(R.id.guitarWidgetId, "setBackgroundColor", Color.WHITE);
                                    appWidgetManager.updateAppWidget(appWidgetId, views);
                                }
                                Glide.with(context.getApplicationContext()).load(guitar[0].getImageLink()).asBitmap().into(appWidgetTarget);
                            }
                        }

                        @Override
                        public void onFailure(Call<Guitar> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Guitar>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
}
