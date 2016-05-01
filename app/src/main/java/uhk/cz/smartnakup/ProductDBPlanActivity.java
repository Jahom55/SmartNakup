package uhk.cz.smartnakup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import uhk.cz.smartnakup.utils.OnPhotoTapListenerForDBPlanInsert;
import uhk.cz.smartnakup.utils.OnPhotoTapListenerForPlanMessage;
import uhk.cz.smartnakup.utils.RecolorImage;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ProductDBPlanActivity extends AppCompatActivity {
    ImageView mImageView;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_dbplan);

        mImageView = (ImageView) findViewById(R.id.imageView);
        RecolorImage.initializateWidthAndHeightFromBitmap(getApplicationContext());

        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListenerForDBPlanInsert(RecolorImage.getWidth(), RecolorImage.getHeight(), mImageView));
    }
}
