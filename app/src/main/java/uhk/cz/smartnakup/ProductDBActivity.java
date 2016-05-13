package uhk.cz.smartnakup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.List;

import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.tables.TableControllerProductCart;
import uhk.cz.smartnakup.tables.TableControllerProductDB;
import uhk.cz.smartnakup.utils.InternetUtilsForFirebase;

public class ProductDBActivity extends AppCompatActivity {
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://brilliant-torch-5232.firebaseio.com/products");

        setContentView(R.layout.activity_product_db);
        countRecords();
        readRecords();

    }


    @Override
    protected void onResume() {
        super.onResume();
        countRecords();
        readRecords();
    }

    public void countRecords() {
        int recordCount = new TableControllerProductDB(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " records found.");
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<ObjectProduct> products = new TableControllerProductDB(this).read();

        if (products.size() > 0) {

            for (ObjectProduct obj : products) {

                int id = obj.getId();
                String productName = obj.getName();
                int productXcor = obj.getXcor();
                int productYcor = obj.getYcor();

                String textViewContents = productName + " - " + productXcor + " - " + productYcor;

                TextView textViewProductItem = new TextView(this);
                textViewProductItem.setPadding(0, 10, 0, 10);
                textViewProductItem.setText(textViewContents);
                textViewProductItem.setTag(Integer.toString(id));
                textViewProductItem.setOnLongClickListener(OnlongClickProductDBListener());

                linearLayoutRecords.addView(textViewProductItem);
            }

        } else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");
            linearLayoutRecords.addView(locationItem);
        }

    }


    public void actionBtnAddtoDB(View view) {
        Intent intent = new Intent(this, ProductDBPlanActivity.class);
        startActivity(intent);
    }


    private View.OnLongClickListener OnlongClickProductDBListener() {
        View.OnLongClickListener clickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final Context context = v.getContext();
                final String id = v.getTag().toString();
                final CharSequence[] items = {"Smazat"};

                new AlertDialog.Builder(context).setTitle("Product zaznam")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    boolean deleteSuccessful = false;
                                    ObjectProduct objectProduct = new TableControllerProductDB(context).readSingleRecord(Integer.parseInt(id));
                                    ObjectCart objectCart = new TableControllerProductCart(context).readSingleRecordByProductId(objectProduct.getId());
                                    if (objectCart == null) {
                                        deleteSuccessful = new TableControllerProductDB(context).delete(Integer.parseInt(id));
                                        if (InternetUtilsForFirebase.haveInternet(context)) {
                                            Firebase.setAndroidContext(context);
                                            mRef = new Firebase("https://brilliant-torch-5232.firebaseio.com/products");
                                            InternetUtilsForFirebase.deleteProduct(mRef, id);
                                        } else {
                                            Toast.makeText(ProductDBActivity.this, R.string.noInternetAcces, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    if (deleteSuccessful) {
                                        Toast.makeText(context, R.string.productDeleteSuccesful_productdb, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, R.string.productDeleteUnsuccesful_productdb, Toast.LENGTH_SHORT).show();
                                    }

                                    countRecords();
                                    readRecords();
                                }
                                dialog.dismiss();

                            }
                        }).show();


                return false;
            }

        };

        return clickListener;
    }

}

