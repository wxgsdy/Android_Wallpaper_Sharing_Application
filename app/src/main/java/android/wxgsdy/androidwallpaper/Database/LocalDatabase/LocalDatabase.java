package android.wxgsdy.androidwallpaper.Database.LocalDatabase;

/**
 * Created by wxgsdy on 9/18/2018.
 */


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import android.content.Context;
import android.wxgsdy.androidwallpaper.Database.Recents;

import static android.wxgsdy.androidwallpaper.Database.LocalDatabase.LocalDatabase.DATABASE_VERSION;

@Database(entities= Recents.class, version = DATABASE_VERSION)
public abstract class LocalDatabase extends RoomDatabase{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="EDMTLiveWallpaper";

    public abstract RecentsDAO recentsDAO();

    private static LocalDatabase instance;
    public static LocalDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, LocalDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }



}
