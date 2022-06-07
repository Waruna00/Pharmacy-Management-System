package sample;

public class Checkout {
    String d_code,d_name,d_des;
    String d_price;
    double amount;
    int bill_item_no;
    int qty;
    public String getD_code() {
        return d_code;
    }
    public String getD_name() {
        return d_name;
    }
    public String getD_price() {
        return d_price;
    }
    public String getD_des() {
        return d_des;
    }
    public double getAmount() {
        return amount;
    }
    public int getBill_item_no() {
        return bill_item_no;
    }

    public int getQty() {
        return qty;
    }
    public Checkout(int bill_item_no, String d_code, String d_name, String d_price, String d_des, int qty, double amount){
        this.bill_item_no=bill_item_no;
        this.d_code=d_code;
        this.d_name=d_name;
        this.d_price=d_price;
        this.d_des=d_des;
        this.qty=qty;
        this.amount=amount;
    }
}
