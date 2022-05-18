package sample;

public class searchList {
    String d_code;
    String d_name;
    String d_type;
    String des;

    public String getD_code() {
        return d_code;
    }

    public String getD_name() {
        return d_name;
    }

    public String getD_type() {
        return d_type;
    }

    public String getDes() {
        return des;
    }

    public searchList(String d_code, String d_name, String d_type, String des){
        this.d_code=d_code;
        this.d_name=d_name;
        this.d_type=d_type;
        this.des=des;

    }
}
