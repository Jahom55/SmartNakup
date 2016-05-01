package uhk.cz.smartnakup.db;

/**
 * Created by Jaromir on 21.2.2016.
 */
public class ObjectProduct {
    int id;
    String name;
    int xcor;
    int ycor;

    public ObjectProduct(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXcor() {
        return xcor;
    }

    public void setXcor(int xcor) {
        this.xcor = xcor;
    }

    public int getYcor() {
        return ycor;
    }

    public void setYcor(int ycor) {
        this.ycor = ycor;
    }
}
