package android.wxgsdy.androidwallpaper.Remote;

import android.wxgsdy.androidwallpaper.Model.AnalyzeModel.ComputerVision;
import android.wxgsdy.androidwallpaper.Model.AnalyzeModel.URLUpload;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by wxgsdy on 9/23/2018.
 */

public interface IComputerVision {
    @Headers({
            "Content-Type:application/json",
            "Ocp-Apim-Subscription-Key:9f4357f82abd46db95bf407138ece0e8"
    })
    @POST
    Call<ComputerVision> analyzeImage(@Url String apiEndPoint, @Body URLUpload url);
}
