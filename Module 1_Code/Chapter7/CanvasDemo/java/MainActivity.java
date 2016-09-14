package com.packtpub.canvasdemo.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get a reference to our ImageView in the layout
        ImageView ourFrame = (ImageView) findViewById(R.id.imageView);

        //Create a bitmap object to use as our canvas
        Bitmap ourBitmap = Bitmap.createBitmap(300,600, Bitmap.Config.ARGB_8888);
        Canvas ourCanvas = new Canvas(ourBitmap);

        //A paint object that does our drawing, on our canvas
        Paint paint = new Paint();

        //Set the background color
        ourCanvas.drawColor(Color.BLACK);

        //Change the color of the virtual paint brush
        paint.setColor(Color.argb(255, 255, 255, 255));

        //Now draw a load of stuff on our canvas
        ourCanvas.drawText("Score: 42 Lives: 3 Hi: 97", 10, 10, paint);
        ourCanvas.drawLine(10, 50, 200, 50, paint);
        ourCanvas.drawCircle(110, 160, 100, paint);
        ourCanvas.drawPoint(10, 260, paint);

        //Now put the canvas in the frame
        ourFrame.setImageBitmap(ourBitmap);
    }

}
