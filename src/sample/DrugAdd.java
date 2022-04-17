package sample;

public class DrugAdd {
    String item_code;
    String name;
    String type;
    Integer quantity;

    public DrugAdd(String item_code, String name, String type, Integer quantity) {
        this.item_code = item_code;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
