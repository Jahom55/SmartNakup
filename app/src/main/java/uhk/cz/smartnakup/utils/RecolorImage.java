package uhk.cz.smartnakup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import java.util.List;

import uhk.cz.smartnakup.R;
import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.controllers.TableControllerProductCart;
import uhk.cz.smartnakup.controllers.TableControllerProductDB;

/**
 * Created by Jaromir on 10.4.2016.
 */
public class RecolorImage {
    static int width = 0;
    static int height = 0;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        RecolorImage.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        RecolorImage.height = height;
    }

    public static void initializateWidthAndHeightFromBitmap(Context context){
        Bitmap myBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lidl);
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        height =tempBitmap.getHeight();
        width = tempBitmap.getWidth();
    }

    public static void getNakup(ImageView imageView, Context context){
        List<ObjectCart> products = new TableControllerProductCart(context).read();

        Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap myBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lidl);
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        height =tempBitmap.getHeight();
        width = tempBitmap.getWidth();
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        for (ObjectCart pr: products) {
            ObjectProduct objectProduct1 = new TableControllerProductDB(context).readSingleRecord(pr.getProduct());
            if (pr.getBought() == 0) {
                myPaint.setColor(Color.RED);
            }else {
                myPaint.setColor(Color.GREEN);
            }
            tempCanvas.drawCircle(objectProduct1.getXcor(), objectProduct1.getYcor(), 30, myPaint);
        }
        Drawable d = new BitmapDrawable(context.getResources(), tempBitmap);
        Drawable bitmap = ContextCompat.getDrawable(context, R.drawable.lidl);
        imageView.setImageDrawable(d);
    }
}
