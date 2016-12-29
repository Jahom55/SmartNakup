package uhk.cz.smartnakup.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.controllers.TableControllerProductCart;
import uhk.cz.smartnakup.controllers.TableControllerProductDB;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Jaromir on 9.4.2016.
 */
public class OnPhotoTapListenerForPlanMessage implements PhotoViewAttacher.OnPhotoTapListener {
    int width, height;
    ImageView imageView;

    public OnPhotoTapListenerForPlanMessage(int width, int height, ImageView imageView) {
        this.width = width;
        this.height = height;
        this.imageView = imageView;
    }

    @Override
    public void onPhotoTap(final View view, float x, float y) {
        List<ObjectCart> products = new TableControllerProductCart(view.getContext()).read();

        for (ObjectCart pr : products) {
            final ObjectCart product = pr;
            final ObjectProduct objectProduct1 = new TableControllerProductDB(view.getContext()).readSingleRecord(pr.getProduct());

            if (Math.abs(x * width - objectProduct1.getXcor()) < 30 && Math.abs(y * height - objectProduct1.getYcor()) < 30) {
                AlertDialog alert = new AlertDialog.Builder(view.getContext()).create();
                alert.setTitle(objectProduct1.getName() + " " + pr.getQuantity() + " kusů");
                alert.setMessage("Nakoupit tuto věc?");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Nenakoupit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        product.setBought(0);
                        new TableControllerProductCart(view.getContext()).update(product);
                        RecolorImage.getNakup(imageView, view.getContext(), true, objectProduct1.getXcor(), objectProduct1.getYcor(), false, null);

                    }
                });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Nakoupit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        product.setBought(1);
                        new TableControllerProductCart(view.getContext()).update(product);
                        RecolorImage.getNakup(imageView, view.getContext(), true, objectProduct1.getXcor(), objectProduct1.getYcor(), false, null);
                    }
                });
                alert.show();
            }
        }
    }


}
