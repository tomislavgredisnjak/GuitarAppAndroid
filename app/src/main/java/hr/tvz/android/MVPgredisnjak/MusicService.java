package hr.tvz.android.MVPgredisnjak;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    static MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.background);
        player.setLooping(true);
        player.start();
    }

    public static void stopMusic(){
            player.stop();
            player.release();
    }

}
