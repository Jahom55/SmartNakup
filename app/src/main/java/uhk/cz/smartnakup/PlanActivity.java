package uhk.cz.smartnakup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import uhk.cz.smartnakup.db.ObjectCart;
import uhk.cz.smartnakup.db.ObjectProduct;
import uhk.cz.smartnakup.tables.TableControllerProductCart;
import uhk.cz.smartnakup.tables.TableControllerProductDB;
import uhk.cz.smartnakup.utils.OnPhotoTapListenerForPlanMessage;
import uhk.cz.smartnakup.utils.RecolorImage;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PlanActivity extends AppCompatActivity {
    ImageView mImageView;
    PhotoViewAttacher mAttacher;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mImageView = (ImageView) findViewById(R.id.imageView);


        RecolorImage.getNakup(mImageView, getApplicationContext());
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListenerForPlanMessage(RecolorImage.getWidth(), RecolorImage.getHeight(), mImageView));
    }

}
