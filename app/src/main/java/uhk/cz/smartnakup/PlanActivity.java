package uhk.cz.smartnakup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import uhk.cz.smartnakup.utils.OnPhotoTapListenerForPlanMessage;
import uhk.cz.smartnakup.utils.RecolorImage;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PlanActivity extends AppCompatActivity {
    ImageView mImageView;
    PhotoViewAttacher mAttacher;

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
