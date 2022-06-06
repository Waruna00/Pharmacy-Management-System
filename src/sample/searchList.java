package sample;

import java.sql.Date;

public class searchList {
    String itemcode;
    Date EXP;
    Date MPD;
    Integer cost_per_unit;
    Integer sale_per_unit;
    Integer quantity;
    String batch_no;
    Integer Com_No;

    public searchList(String itemcode, Date EXP, Date MPD, Integer cost_per_unit, Integer sale_per_unit, Integer quantity, String batch_no, Integer com_No) {
        this.itemcode = itemcode;
        this.EXP = EXP;
        this.MPD = MPD;
        this.cost_per_unit = cost_per_unit;
        this.sale_per_unit = sale_per_unit;
        this.quantity = quantity;
        this.batch_no = batch_no;
        this.Com_No = com_No;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public Date getEXP() {
        return EXP;
    }

    public void setEXP(Date EXP) {
        this.EXP = EXP;
    }

    public Date getMPD() {
        return MPD;
    }

    public void setMPD(Date MPD) {
        this.MPD = MPD;
    }

    public Integer getCost_per_unit() {
        return cost_per_unit;
    }

    public void setCost_per_unit(Integer cost_per_unit) {
        this.cost_per_unit = cost_per_unit;
    }

    public Integer getSale_per_unit() {
        return sale_per_unit;
    }

    public void setSale_per_unit(Integer sale_per_unit) {
        this.sale_per_unit = sale_per_unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public Integer getCom_No() {
        return Com_No;
    }

    public void setCom_No(Integer com_No) {
        Com_No = com_No;
    }


}
