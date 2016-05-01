package uhk.cz.smartnakup.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uhk.cz.smartnakup.db.DatabaseHandler;
import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;

/**
 * Created by Jaromir on 6.2.2016.
 */
public class TableControllerProductCart extends DatabaseHandler {

    public TableControllerProductCart(Context context) {
        super(context);
    }

    public boolean create(ObjectCart objectCart) {
        ContentValues values = new ContentValues();
        values.put("product_id", objectCart.getProduct());
        values.put("quantity", objectCart.getQuantity());
        values.put("bought", objectCart.getBought());
        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("cart", null, values) > 0;
        db.close();
        return createSuccessful;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM cart";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public List<ObjectCart> read() {

        List<ObjectCart> recordsList = new ArrayList<ObjectCart>();

        String sql = "SELECT * FROM cart ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("product_id")));
                int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantity")));
                int bought = Integer.parseInt(cursor.getString(cursor.getColumnIndex("bought")));

                ObjectCart objectCart = new ObjectCart();
                objectCart.setId(id);
                objectCart.setProduct(productId);
                objectCart.setQuantity(quantity);
                objectCart.setBought(bought);
                recordsList.add(objectCart);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public ObjectCart readSingleRecord(int cartId) {

        ObjectCart objectCart = null;

        String sql = "SELECT * FROM cart WHERE id = " + cartId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("product_id")));
            int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantity")));
            int bought = Integer.parseInt(cursor.getString(cursor.getColumnIndex("bought")));

            objectCart = new ObjectCart();
            objectCart.setId(id);
            objectCart.setProduct(productId);
            objectCart.setQuantity(quantity);
            objectCart.setBought(bought);
        }

        cursor.close();
        db.close();

        return objectCart;

    }



    public boolean update(ObjectCart objectCart) {

        ContentValues values = new ContentValues();

        values.put("product_id", objectCart.getProduct());
        values.put("quantity", objectCart.getQuantity());
        values.put("bought", objectCart.getBought());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectCart.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("cart", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("cart", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;
    }

    public ObjectCart readSingleRecordByProductId(int product_Id) {

        ObjectCart objectCart = null;

        String sql = "SELECT * FROM cart WHERE product_id = " + product_Id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("product_id")));
            int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantity")));
            int bought = Integer.parseInt(cursor.getString(cursor.getColumnIndex("bought")));

            objectCart = new ObjectCart();
            objectCart.setId(id);
            objectCart.setProduct(productId);
            objectCart.setQuantity(quantity);
            objectCart.setBought(bought);

        }

        cursor.close();
        db.close();

        return objectCart;

    }

    public boolean deleteTable() {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("cart",null,null);
            deleteSuccessful = true;
        } catch (Exception e) {
        }

        db.close();

        return deleteSuccessful;
    }

}
