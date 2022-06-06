package sample;

public class DrugAdd {
    String item_code;
    String name;
    String type;
    String barcode;
    Integer quantity;
    String description;




    public DrugAdd(String item_code, String name, String type , String barcode, Integer quantity, String description) {
        this.item_code = item_code;
        this.name = name;
        this.type = type;
        this.barcode = barcode;
        this.quantity = quantity;
        this.description = description;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
