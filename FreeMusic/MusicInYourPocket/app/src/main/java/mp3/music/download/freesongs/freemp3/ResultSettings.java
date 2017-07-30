package mp3.music.download.freesongs.freemp3;

/**
 * Created by Alex on 11.07.2017.
 */

public class ResultSettings {
    public String result;
    public  DataSettings data;

    public ResultSettings() {
    }

    public ResultSettings(String result, DataSettings data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public DataSettings getData() {
        return data;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(DataSettings data) {
        this.data = data;
    }
}
