package sample;

import java.time.LocalDate;

public class outwardList {
    Integer P_no;
    String itemcode;
    LocalDate EXP;
    LocalDate MPD;
    Integer cost_per_unit;
    Integer sale_per_unit;
    Integer quantity;
    String batch_no;
    Integer Com_No;

    public outwardList(Integer p_no, String itemcode, LocalDate EXP, LocalDate MPD, Integer cost_per_unit, Integer sale_per_unit, Integer quantity, String batch_no, Integer com_No) {
        this.P_no = p_no;
        this.itemcode = itemcode;
        this.EXP = EXP;
        this.MPD = MPD;
        this.cost_per_unit = cost_per_unit;
        this.sale_per_unit = sale_per_unit;
        this.quantity = quantity;
        this.batch_no = batch_no;
        Com_No = com_No;
    }


    public Integer getP_no() {
        return P_no;
    }

    public String getItemcode() {
        return itemcode;
    }

    public LocalDate getEXP() {
        return EXP;
    }

    public LocalDate getMPD() {
        return MPD;
    }

    public Integer getCost_per_unit() {
        return cost_per_unit;
    }

    public Integer getSale_per_unit() {
        return sale_per_unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public Integer getCom_No() {
        return Com_No;
    }

    public void setP_no(Integer p_no) {
        P_no = p_no;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public void setEXP(LocalDate EXP) {
        this.EXP = EXP;
    }

    public void setMPD(LocalDate MPD) {
        this.MPD = MPD;
    }

    public void setCost_per_unit(Integer cost_per_unit) {
        this.cost_per_unit = cost_per_unit;
    }

    public void setSale_per_unit(Integer sale_per_unit) {
        this.sale_per_unit = sale_per_unit;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public void setCom_No(Integer com_No) {
        Com_No = com_No;
    }








}
