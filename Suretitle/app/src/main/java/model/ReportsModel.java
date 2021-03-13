package model;

public class ReportsModel
{
    String report_id;
    String token;
    String customer_phone;
    String report_file_path;
    String uploaded_time;
    String country,state,property;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getReport_file_path() {
        return report_file_path;
    }

    public void setReport_file_path(String report_file_path) {
        this.report_file_path = report_file_path;
    }

    public String getUploaded_time() {
        return uploaded_time;
    }

    public void setUploaded_time(String uploaded_time) {
        this.uploaded_time = uploaded_time;
    }
}
