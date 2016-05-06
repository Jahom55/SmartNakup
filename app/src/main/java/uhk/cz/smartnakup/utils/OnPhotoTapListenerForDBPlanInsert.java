package uhk.cz.smartnakup.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.List;

import uhk.cz.smartnakup.R;
import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.tables.TableControllerProductCart;
import uhk.cz.smartnakup.tables.TableControllerProductDB;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Jaromir on 22.4.2016.
 */
public class OnPhotoTapListenerForDBPlanInsert implements PhotoViewAttacher.OnPhotoTapListener {
    int width, height;
    ImageView imageView;

    public OnPhotoTapListenerForDBPlanInsert(int width, int height, ImageView imageView) {
        this.width = width;
        this.height = height;
        this.imageView = imageView;
    }

    @Override
    public void onPhotoTap(final View view, final float x, final float y) {

        final Context context = view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.productdb_input_form, null, false);
        final EditText editTextProductName = (EditText) formElementsView.findViewById(R.id.editTextProductName);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle(R.string.createItemToDb_OnPhotoListener)
                .setPositiveButton(R.string.create,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String productName = editTextProductName.getText().toString();
                                int[] xy = roundCoordinatesToPlan((int) (x * width), (int) (y * height));
                                ObjectProduct objectProduct = new ObjectProduct();
                                objectProduct.setName(productName);
                                objectProduct.setXcor(xy[0]);
                                objectProduct.setYcor(xy[1]);

                                boolean createSuccessful = new TableControllerProductDB(context).create(objectProduct);

                                ObjectProduct objectToFirebase = new TableControllerProductDB(context).readSingleRecordByName(productName);
                                if (InternetUtilsForFirebase.haveInternet(context)) {
                                    Firebase.setAndroidContext(context);
                                    Firebase myFirebaseRef = new Firebase("https://brilliant-torch-5232.firebaseio.com/products");
                                    myFirebaseRef.push().setValue(objectToFirebase);
                                } else {
                                    Toast.makeText(context, R.string.noInternetAcces, Toast.LENGTH_SHORT).show();
                                }

                                if (createSuccessful) {
                                    Toast.makeText(context, "Product information was saved.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Unable to save product information.", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();


                            }

                        }).show();


    }

    //TODO Method for Accurate rounding the map
    public int[] roundCoordinatesToPlan(int x, int y) {
        int[] xy = new int[2];
        if (x < 145 && y > 65 && y < 1600) {
            xy[0] = 103;
            xy[1] = y;
        } else if (x > 406 && x < 550 && y > 160 && y < 800) {
            xy[0] = 463;
            xy[1] = y;
        } else {
            xy[0] = x;
            xy[1] = y;
        }
        return xy;
    }


}

