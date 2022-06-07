package sample;

public class RemittanceList {
    int no;
    String acc;
    String des;
    double amount;
    RemittanceList(int no,String acc,String des,double amount){
        this.no=no;
        this.acc=acc;
        this.des=des;
        this.amount=amount;
    }
    public int getNo() {
        return no;
    }
    public String getAcc() {
        return acc;
    }
    public String getDes() {
        return des;
    }

    public double getAmount() {
        return amount;
    }
}
