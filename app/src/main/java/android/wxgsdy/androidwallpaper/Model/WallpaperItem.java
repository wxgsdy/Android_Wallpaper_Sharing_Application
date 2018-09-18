package android.wxgsdy.androidwallpaper.Model;

/**
 * Created by wxgsdy on 9/17/2018.
 */

public class WallpaperItem {
    public String imageLink;
    public String categoryId;

    public WallpaperItem() {
    }

    public WallpaperItem(String imageLink, String categoryId) {
        this.imageLink = imageLink;
        this.categoryId = categoryId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
