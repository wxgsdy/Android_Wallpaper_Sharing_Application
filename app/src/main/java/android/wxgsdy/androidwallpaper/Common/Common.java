package android.wxgsdy.androidwallpaper.Common;

import android.wxgsdy.androidwallpaper.Model.WallpaperItem;
import android.wxgsdy.androidwallpaper.Remote.IComputerVision;
import android.wxgsdy.androidwallpaper.Remote.RetrofitClient;

/**
 * Created by wxgsdy on 9/17/2018.
 */

public class Common {

    public static final String STR_CATEGORY_BACKGROUD = "CategoryBackground";
    public static final String STR_WALLPAPER = "Backgrounds";
    public static final int SIGN_IN_REQUEST_CODE = 1001 ;
    public static final int PIC_IMAGE_REQUEST = 1002;
    public static String CATEGORY_SELECTED;
    public static String CATEGORY_ID_SELECTED;

    public static final int PERMISSION_REQUEST_CODE = 1000;

    public static WallpaperItem select_background = new WallpaperItem();

    public static String select_background_key;

    public static String BASE_URL = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0";
    public static IComputerVision getComputerVisionAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IComputerVision.class);
    }

    public static String getAPIAdultEndPoint()
    {
        return new StringBuilder(BASE_URL).append("analyze?visualFeatures=Adult&language=en").toString();
    }


}
