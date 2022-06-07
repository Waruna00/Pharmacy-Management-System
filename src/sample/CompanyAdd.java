package sample;

public class CompanyAdd {
    String com_no;
    String name;
    String address;
    Integer phone;

    public CompanyAdd(String com_no, String name, String address, Integer phone){
        this.com_no= com_no;
        this.name = name;
        this.address = address;
        this.phone =phone;
    }

    public String getCom_no() {
        return com_no;
    }
    public void setCom_no(String com_no) {
        this.com_no = com_no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getPhone() {
        return phone;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }
}
