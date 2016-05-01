package uhk.cz.smartnakup.db;

/**
 * Created by Jaromir on 21.2.2016.
 */
public class ObjectCart {
    int id;
    int quantity;
    int product;
    int bought;

    public ObjectCart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }
}
