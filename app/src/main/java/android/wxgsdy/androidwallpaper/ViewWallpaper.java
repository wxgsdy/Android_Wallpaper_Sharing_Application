package android.wxgsdy.androidwallpaper;

import android.*;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.wxgsdy.androidwallpaper.Common.Common;
import android.wxgsdy.androidwallpaper.Database.DataSource.RecentRepository;
import android.wxgsdy.androidwallpaper.Database.LocalDatabase.LocalDatabase;
import android.wxgsdy.androidwallpaper.Database.LocalDatabase.RecentsDataSource;
import android.wxgsdy.androidwallpaper.Database.Recents;
import android.wxgsdy.androidwallpaper.Helper.SaveImageHelper;
import android.wxgsdy.androidwallpaper.Model.WallpaperItem;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewWallpaper extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton, fabDownload;
    ImageView imageView;
    CoordinatorLayout rootLayout;

    FloatingActionMenu mainFloating;
    com.github.clans.fab.FloatingActionButton fbShare;



    //Room Database
    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    // Facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case Common.PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    AlertDialog dialog = new SpotsDialog(ViewWallpaper.this);
                    dialog.show();
                    dialog.setMessage("Please waiting....");

                    String fileName = UUID.randomUUID().toString()+".png";
                    Picasso.with(getBaseContext())
                            .load(Common.select_background.getImageLink())
                            .into(new SaveImageHelper(getBaseContext(),
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName,
                                    "EDMTDev Live Wallpaper image"));
                }
                else
                    Toast.makeText(this, "You need accept this permission to download images!", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private Target target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try{
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(rootLayout, "Wallpaper was set!", Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Target facebookConvertBitmap = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();

            if(ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        // init Facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        // Init RoomDatabase
        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(this);
        recentRepository = RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));



        // Init
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        collapsingToolbarLayout.setTitle(Common.CATEGORY_SELECTED);

        imageView = (ImageView) findViewById(R.id.imageThumb);
        Picasso.with(this)
                .load(Common.select_background.getImageLink())
                .into(imageView);

        mainFloating = (FloatingActionMenu)findViewById(R.id.menu);
        fbShare = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fb_share);
        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create callback
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallpaper.this, "Share suceed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ViewWallpaper.this, "Share canceled!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ViewWallpaper.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // we will fetech photo from link and convert to bitmap
                Picasso.with(getBaseContext())
                        .load(Common.select_background.getImageLink())
                        .into(facebookConvertBitmap);

            }
        });

        //add to recents
        addToRecents();

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fabWallpaper);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.select_background.getImageLink())
                        .into(target);
            }
        });


        fabDownload = (FloatingActionButton)findViewById(R.id.fabDownload);
        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(ViewWallpaper.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
                }
                else
                {
                    AlertDialog dialog = new SpotsDialog(ViewWallpaper.this);
                    dialog.show();
                    dialog.setMessage("Please waiting....");

                    String fileName = UUID.randomUUID().toString()+".png";
                    Picasso.with(getBaseContext())
                            .load(Common.select_background.getImageLink())
                            .into(new SaveImageHelper(getBaseContext(),
                                    dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName,
                                    "EDMTDev Live Wallpaper image"));
                }
            }
        });


        // View count
        increaseViewCount();

    }

    private void increaseViewCount() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(Common.select_background_key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("viewCount"))
                        {
                            WallpaperItem wallpaperItem = dataSnapshot.getValue(WallpaperItem.class);
                            long count = wallpaperItem.getViewCount() + 1;
                            // update
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", count);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpaper.this, "Cannot update view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else // if view count is not set(default 1)
                        {
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", Long.valueOf(1));

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ViewWallpaper.this, "Cannot set default view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addToRecents() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Recents recents = new Recents(
                        Common.select_background.getImageLink(),
                        Common.select_background.getCategoryId(),
                        String.valueOf(System.currentTimeMillis()),
                        Common.select_background_key);
                recentRepository.insertRecents(recents);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR", throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });

        compositeDisposable.add(disposable);

    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(target);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
