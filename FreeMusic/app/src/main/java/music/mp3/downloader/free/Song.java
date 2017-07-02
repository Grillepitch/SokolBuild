package music.mp3.downloader.free;

/**
 * Created by Alex on 10.06.2017.
 */

public class Song extends Item{
    private String title;
    private String videoId;
    private String img_small;
    private String img_medium;
    private String img_big;
    private String mp3Url;
    public Song(String title, String videoId, String img_small, String img_medium, String img_big, String mp3Url) {
        this.title = title;
        this.videoId = videoId;
        this.img_small = img_small;
        this.img_medium = img_medium;
        this.img_big = img_big;
        this.mp3Url = mp3Url;
    }

    public Song() {
    }

    public String getTitle() {
        return title;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getImg_small() {
        return img_small;
    }

    public String getImg_medium() {
        return img_medium;
    }

    public String getImg_big() {
        return img_big;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setImg_small(String img_small) {
        this.img_small = img_small;
    }

    public void setImg_medium(String img_medium) {
        this.img_medium = img_medium;
    }

    public void setImg_big(String img_big) {
        this.img_big = img_big;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }
}
