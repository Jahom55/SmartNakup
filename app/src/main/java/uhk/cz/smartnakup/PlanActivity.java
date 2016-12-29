package uhk.cz.smartnakup;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import uhk.cz.smartnakup.utils.OnPhotoTapListenerForPlanMessage;
import uhk.cz.smartnakup.utils.RecolorImage;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PlanActivity extends AppCompatActivity implements SensorEventListener{
    ImageView mImageView;
    PhotoViewAttacher mAttacher;
    private SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;
    Float azimut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mImageView = (ImageView) findViewById(R.id.imageView);

        RecolorImage.getNakup(mImageView, getApplicationContext(),false,933, 1600,false, azimut);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListenerForPlanMessage(RecolorImage.getWidth(), RecolorImage.getHeight(), mImageView));
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RecolorImage.getNakup(mImageView,getApplicationContext(),false, 0 ,0,true, azimut);
                //Toast.makeText(PlanActivity.this,"Click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor =sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(PlanActivity.this, "Není povolena poloha.", Toast.LENGTH_SHORT).show();
        }
        //Registrace kompasu
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float i;

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            i = event.values[0];
            RecolorImage.getNakup(mImageView,getApplicationContext(),false, 0 ,0,true, azimut);
            //Toast.makeText(PlanActivity.this,"Azimut " + azimut, Toast.LENGTH_SHORT).show();
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        getAzimut();
    }

    private void getAzimut(){
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // azimut, pitch and roll
                //System.out.println(azimut);    // N = 0; E - -1.6; S = 3.1; W - 1.6
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
