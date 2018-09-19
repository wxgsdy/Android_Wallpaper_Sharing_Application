package android.wxgsdy.androidwallpaper.Database.LocalDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.wxgsdy.androidwallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wxgsdy on 9/18/2018.
 */

@Dao
public interface RecentsDAO {
    @Query("SELECT * FROM recents ORDER BY saveTime DESC LIMIT 10")
    Flowable<List<Recents>> getAllRecents();

    @Insert
    void insertRecents(Recents... recents);

    @Update
    void updateRecents(Recents... recents);

    @Delete
    void deleteRecents(Recents... recents);

    @Query("DELETE FROM recents")
    void deleteAllRecents();


}
