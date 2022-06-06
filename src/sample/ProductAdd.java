package sample;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

public class ProductAdd {
    String itemcode;
    String itemname;
    String description;
    LocalDate EXP;
    LocalDate MPD;
    Integer cost_per_unit;
    Integer sale_per_unit;
    Integer quantity;
    String batch_no;
    Integer Com_No;


    public ProductAdd(String itemcode, String itemname, String description, LocalDate EXP, LocalDate MPD, Integer cost_per_unit, Integer sale_per_unit, Integer quantity, String batch_no, Integer com_No) {
        this.itemcode = itemcode;
        this.itemname = itemname;
        this.description = description;
        this.EXP = EXP;
        this.MPD = MPD;
        this.cost_per_unit = cost_per_unit;
        this.sale_per_unit = sale_per_unit;
        this.quantity = quantity;
        this.batch_no = batch_no;
        this.Com_No = com_No;

    }
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEXP() {
        return EXP;
    }

    public void setEXP(LocalDate EXP) {
        this.EXP = EXP;
    }

    public LocalDate getMPD() {
        return MPD;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setMPD(LocalDate MPD) {
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
