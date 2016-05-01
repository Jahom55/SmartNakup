package uhk.cz.smartnakup.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uhk.cz.smartnakup.db.DatabaseHandler;
import uhk.cz.smartnakup.db.ObjectProduct;

/**
 * Created by Jaromir on 6.2.2016.
 */
public class TableControllerProductDB extends DatabaseHandler {

    public TableControllerProductDB(Context context) {
        super(context);
    }

    public boolean create(ObjectProduct objectProduct) {
        ContentValues values = new ContentValues();
        values.put("name", objectProduct.getName());
        values.put("xcor", objectProduct.getXcor());
        values.put("ycor", objectProduct.getYcor());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("products", null, values) > 0;
        db.close();
        return createSuccessful;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM products";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public List<ObjectProduct> read() {

        List<ObjectProduct> recordsList = new ArrayList<ObjectProduct>();

        String sql = "SELECT * FROM products ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String productName = cursor.getString(cursor.getColumnIndex("name"));
                int productXcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("xcor")));
                int productYcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ycor")));

                ObjectProduct objectProduct = new ObjectProduct();
                objectProduct.setId(id);
                objectProduct.setName(productName);
                objectProduct.setXcor(productXcor);
                objectProduct.setYcor(productYcor);
                recordsList.add(objectProduct);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public ObjectProduct readSingleRecord(int productId) {

        ObjectProduct objectProduct = null;

        String sql = "SELECT * FROM products WHERE id = " + productId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String productName = cursor.getString(cursor.getColumnIndex("name"));
            int productXcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("xcor")));
            int productYcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ycor")));

            objectProduct = new ObjectProduct();
            objectProduct.setId(id);
            objectProduct.setName(productName);
            objectProduct.setXcor(productXcor);
            objectProduct.setYcor(productYcor);

        }

        cursor.close();
        db.close();

        return objectProduct;

    }

    public ObjectProduct readSingleRecordByName(String name) {

        ObjectProduct objectProduct = null;

        String sql = "SELECT * FROM products WHERE name = '" + name + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String productName = cursor.getString(cursor.getColumnIndex("name"));
            int productXcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("xcor")));
            int productYcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ycor")));

            objectProduct = new ObjectProduct();
            objectProduct.setId(id);
            objectProduct.setName(productName);
            objectProduct.setXcor(productXcor);
            objectProduct.setYcor(productYcor);

        }

        cursor.close();
        db.close();

        return objectProduct;

    }

    public boolean update(ObjectProduct objectProduct) {

        ContentValues values = new ContentValues();

        values.put("name", objectProduct.getName());
        values.put("xcor", objectProduct.getXcor());
        values.put("ycor", objectProduct.getYcor());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectProduct.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("products", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("products", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;
    }

    public List<ObjectProduct> read(String searchTerm) {

        List<ObjectProduct> recordsList = new ArrayList<ObjectProduct>();

        String sql = "";
        sql += "SELECT * FROM products";
        sql += " WHERE name LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY name DESC";
        sql += " LIMIT 0,5";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String productName = cursor.getString(cursor.getColumnIndex("name"));
                int productXcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("xcor")));
                int productYcor = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ycor")));

                ObjectProduct objectProduct = new ObjectProduct();
                objectProduct.setId(id);
                objectProduct.setName(productName);
                objectProduct.setXcor(productXcor);
                objectProduct.setYcor(productYcor);
                recordsList.add(objectProduct);


            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }


    public String[] getItemsFromDb(String searchTerm){

        List<ObjectProduct> products = read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (ObjectProduct record : products) {
            item[x] = record.getName();
            x++;
        }
        return item;
    }




}
