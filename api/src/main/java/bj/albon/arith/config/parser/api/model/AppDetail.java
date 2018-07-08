package bj.albon.arith.config.parser.api.model;

/**
 * @author albon
 *         Date : 17-1-22
 *         Time: 下午5:23
 */
public class AppDetail {

    private int status;
    private String message;
    private AppData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AppData getData() {
        return data;
    }

    public void setData(AppData data) {
        this.data = data;
    }
}
