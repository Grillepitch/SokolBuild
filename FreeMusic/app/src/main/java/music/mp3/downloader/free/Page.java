package music.mp3.downloader.free;


import java.util.ArrayList;

public class Page {

    private String nextPageToken;
    private ArrayList<Song> videos;


    public Page(String nextPageToken, ArrayList<Song> videos) {
        this.nextPageToken = nextPageToken;
        this.videos = videos;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public ArrayList<Song> getVideos() {
        return videos;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public void setVideos(ArrayList<Song> videos) {
        this.videos = videos;
    }

    public Page() {
    }

    @Override
    public String toString() {
        return "Page{" +
                "nextPageToken='" + nextPageToken + '\'' +
                ", videos=" + videos +
                '}';
    }
}
