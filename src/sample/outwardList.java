package sample;

import java.time.LocalDate;

public class outwardList {
    private Integer no;
    private String code;
    private String name;
    private String des;


    Integer qty;

    public outwardList(Integer no, String code, String name, String des, Integer qty) {
        this.no=no;
        this.code=code;
        this.name=name;
        this.des=des;
        this.qty=qty;
    }


    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
