package hr.tvz.android.MVPgredisnjak;

import com.raizlabs.android.dbflow.structure.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfacePremaServisu {

    @GET("Guitars")
    Call<List<Guitar>> dohvatiGitare();

    @GET("Guitars/{id}")
    Call<Guitar> dohvatiGitaru(@Path("id") int guitarId);

}
