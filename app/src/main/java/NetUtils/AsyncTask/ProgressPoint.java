package NetUtils.AsyncTask;

public class ProgressPoint{
    String message;
    Integer progress;

    public ProgressPoint(String message, Integer progress) {
        this.message = message;
        this.progress = progress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}