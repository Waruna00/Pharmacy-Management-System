package sample;

import java.time.LocalDate;

public class outwardList {
    Integer P_no;
    String itemcode;
    LocalDate EXP;
    LocalDate MPD;
    Integer quantity;
    String batch_no;
    String Com_No;

    public outwardList(Integer p_no, String itemcode, LocalDate EXP, LocalDate MPD, Integer quantity, String batch_no, String com_No) {
        this.P_no = p_no;
        this.itemcode = itemcode;
        this.EXP = EXP;
        this.MPD = MPD;
        this.quantity = quantity;
        this.batch_no = batch_no;
        this.Com_No = com_No;
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

    public Integer getQuantity() {
        return quantity;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public String getCom_No() {
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

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public void setCom_No(String com_No) {
        this.Com_No = com_No;
    }








}
