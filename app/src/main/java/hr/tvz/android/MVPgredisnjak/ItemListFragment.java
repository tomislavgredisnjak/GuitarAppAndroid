package hr.tvz.android.MVPgredisnjak;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private static final String API_URL = "http://10.0.2.2:8080/api/";
    List<Guitar> guitars = new ArrayList<>();

    private Callbacks mCallbacks = sGuitarCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;


    interface Callbacks {

        void onItemSelected(String name);

    }


    private static Callbacks sGuitarCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String name) {
        }
    };

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InterfacePremaServisu client =
                ServiceGenerator.createService(InterfacePremaServisu.class, API_URL);

        Call<List<Guitar>> call = client.dohvatiGitare();

        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_list_item_1);

        call.enqueue(new Callback<List<Guitar>>() {

            @Override
            public void onResponse(Call<List<Guitar>> call, Response<List<Guitar>> response) {
                // Poziva se kada se dobije response od servera - bilo koji (čak i 404)

                if (response.isSuccessful()) {
                    // Napuni podacima
                    guitars = response.body();
                    adapter.clear();
                    for (Guitar guitar : guitars) {
                        adapter.add(guitar.getName());
                    }
                }
                setListAdapter(new ArrayAdapter<Guitar>(
                        getActivity(),
                        android.R.layout.simple_list_item_activated_1,
                        android.R.id.text1,
                        guitars));
            }

            @Override
            public void onFailure(Call<List<Guitar>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sGuitarCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Integer idGitare = position + 1;
        if(position > 8){
            idGitare++;
        }
        mCallbacks.onItemSelected(idGitare.toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {

            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }


    public void setActivateOnItemClick(boolean activateOnItemClick) {

        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
