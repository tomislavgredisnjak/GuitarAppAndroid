package hr.tvz.android.MVPgredisnjak;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuitarActivity extends ListActivity {

    private static final String API_URL = "http://10.0.2.2:8080/api/";
    List<Guitar> guitars = new ArrayList<>();
    List<Guitar> guitars1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("GuitarActivityCreated");

        InterfacePremaServisu client =
                ServiceGenerator.createService(InterfacePremaServisu.class, API_URL);

        Call<List<Guitar>> call = client.dohvatiGitare();

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);

        call.enqueue(new Callback<List<Guitar>>() {

            @Override
            public void onResponse(Call<List<Guitar>> call, Response<List<Guitar>> response) {

                if (response.isSuccessful()) {

                    guitars = response.body();
                    adapter.clear();
                    for (Guitar guitar : guitars) {
                        guitars1.add(guitar);
                        adapter.add(guitar.getName());
                    }
                    setListAdapter(adapter);
                    getListView().setTextFilterEnabled(true);

                }

            }



            @Override
            public void onFailure(Call<List<Guitar>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}