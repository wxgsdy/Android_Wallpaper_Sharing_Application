package android.wxgsdy.androidwallpaper.Database.DataSource;

import android.wxgsdy.androidwallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wxgsdy on 9/18/2018.
 */

public class RecentRepository implements IRecentsDataSource{

    private IRecentsDataSource mLocalDataSource;
    private static RecentRepository instance;

    public RecentRepository(IRecentsDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static RecentRepository getInstance(IRecentsDataSource mLocalDataSource){
        if(instance == null)
            instance = new RecentRepository(mLocalDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDataSource.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDataSource.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        mLocalDataSource.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
        mLocalDataSource.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        mLocalDataSource.deleteAllRecents();
    }
}
