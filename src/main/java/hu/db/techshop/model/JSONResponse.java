package hu.db.techshop.model;


import java.util.Map;

public class JSONResponse {

    String status;
    String message;
    Map<String, String> data;

    public JSONResponse(String status) {
        this.status = status;
    }

    public JSONResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public JSONResponse(String status, String message, Map<String, String> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
