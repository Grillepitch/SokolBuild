package music.mp3.downloader.free;



public class Data {
    public int net_type ;
    public int popup ;
    public String popup_url;
    public int is_burst;
    public String burst_url;

    public Data(int net_type, int popup, String popup_url, int is_burst, String burst_url) {
        this.net_type = net_type;
        this.popup = popup;
        this.popup_url = popup_url;
        this.is_burst = is_burst;
        this.burst_url = burst_url;
    }

    public int getNet_type() {
        return net_type;
    }

    public int getPopup() {
        return popup;
    }

    public String getPopup_url() {
        return popup_url;
    }

    public int getIs_burst() {
        return is_burst;
    }

    public String getBurst_url() {
        return burst_url;
    }

    public void setNet_type(int net_type) {
        this.net_type = net_type;
    }

    public void setPopup(int popup) {
        this.popup = popup;
    }

    public void setPopup_url(String popup_url) {
        this.popup_url = popup_url;
    }

    public void setIs_burst(int is_burst) {
        this.is_burst = is_burst;
    }

    public void setBurst_url(String burst_url) {
        this.burst_url = burst_url;
    }
}
