package com.example.root.flipcoin_toss;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity implements SensorEventListener{

    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;
    private SensorManager sensorMgr;
    private RelativeLayout background;
    private TextView text;
    private boolean isShaked = false;
    ImageView para;
    Random r;
    int side;
    Button btnUp;
    Button btnDown;
    int count = 1;

    int[] coinImageFront={
            R.drawable.t1,
            R.drawable.c2,
            R.drawable.d2,
            R.drawable.transparent,
    };
    int [] coinImageBack={
            R.drawable.t2,
            R.drawable.c1,
            R.drawable.d1,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        para = (ImageView) findViewById(R.id.coin);
        btnDown = (Button)findViewById(R.id.down);
        r = new Random();
        final Context context = getApplicationContext();
        final CharSequence tyazi = "Yazı";
        final CharSequence ttura = "Tura";
        final int duration = Toast.LENGTH_SHORT;



        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                para.setImageResource(coinImageFront[count++]);
                if(count == 4){
                    count=1;
                }
            }
        });
        para.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int a = r.nextInt(2);

                if(a == 0){
                    Toast.makeText(context,tyazi, duration).show();
                    para.setImageResource(coinImageFront[count-1]);
                }else{
                    Toast.makeText(context,ttura, duration).show();
                    para.setImageResource(coinImageBack[count-1]);
                }

                RotateAnimation rotate = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                        0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.49f);
                rotate.setDuration(900);

                para.startAnimation(rotate);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
            sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
            boolean accelSupported = sensorMgr.registerListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

            if (!accelSupported) {
                // on accelerometer on this device
                sensorMgr.unregisterListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            }
        }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 250) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z)
                        / diffTime * 10900;
                if (speed > SHAKE_THRESHOLD) {
                    // yes, this is a shake action! Do something about it!
                    isShaked = true;
                    int a = r.nextInt(2);

                    if(a == 0){
                        para.setImageResource(coinImageFront[count-1]);
                    }else{
                        para.setImageResource(coinImageBack[count-1]);
                    }

                    RotateAnimation rotate = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                            0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.49f);
                    rotate.setDuration(900);

                    para.startAnimation(rotate);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
