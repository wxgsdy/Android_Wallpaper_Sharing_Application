package android.wxgsdy.androidwallpaper.Database.DataSource;

import android.wxgsdy.androidwallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wxgsdy on 9/18/2018.
 */

public interface IRecentsDataSource {
    Flowable<List<Recents>> getAllRecents();
    void insertRecents(Recents... recents);
    void updateRecents(Recents... recents);
    void deleteRecents(Recents... recents);
    void deleteAllRecents();


}
