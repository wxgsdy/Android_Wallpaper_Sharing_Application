package android.wxgsdy.androidwallpaper.Database.LocalDatabase;

import android.opengl.GLSurfaceView;
import android.wxgsdy.androidwallpaper.Database.DataSource.IRecentsDataSource;
import android.wxgsdy.androidwallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by wxgsdy on 9/18/2018.
 */

public class RecentsDataSource implements IRecentsDataSource{

    private RecentsDAO recentsDAO;
    private static RecentsDataSource instance;

    public RecentsDataSource(RecentsDAO recentsDAO) {
        this.recentsDAO = recentsDAO;
    }

    public static RecentsDataSource getInstance(RecentsDAO recentsDAO)
    {
        if(instance == null)
            instance = new RecentsDataSource(recentsDAO);
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return recentsDAO.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        recentsDAO.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        recentsDAO.updateRecents(recents);
    }

    @Override
    public void deleteRecents(Recents... recents) {
        recentsDAO.deleteRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        recentsDAO.deleteAllRecents();
    }
}
