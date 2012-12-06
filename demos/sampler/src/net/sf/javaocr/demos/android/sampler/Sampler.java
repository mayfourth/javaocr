/*
 * Copyright (c) 2003-2012, Ronald B. Cemer , Konstantin Pribluda, William Whitney, Andrea De Pasquale
 *
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sf.javaocr.demos.android.sampler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import net.sf.javaocr.demos.android.utils.camera.CameraManager;
import net.sf.javaocr.demos.android.utils.image.ImageProcessor;
import net.sf.javaocr.demos.android.utils.image.ImageSlicer;
import net.sf.javaocr.demos.android.utils.image.IntegralImageSlicer;
import net.sf.javaocr.demos.android.utils.image.SauvolaImageProcessor;
import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.filter.ThresholdFilter;
import net.sourceforge.javaocr.ocr.PixelImage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * android demo application gathering samples. no real OCR here, just preprocessing
 * and sample saving. Saved samples can be retrieved from SD card, and used to train
 * macthers for later recognition.
 *
 * @author Konstantin Pribluda
 */
public class Sampler extends Activity implements SurfaceHolder.Callback, View.OnClickListener{


    // pixels for display
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BLACK = 0xff000000;

    private SurfaceView surfaceView;
    private SurfaceHolder preview;
    private static final String LOG_TAG = "javaocr.sampler";

    CameraManager cameraManager;

    boolean haveSurface = false;


    private ImageView scanArea;
    private ImageView resultArea;

    // computed position of viewfinder origin.
    // we will use it to calculate processing image
    private int viewfinderOriginX;
    private int viewfinderOriginY;


    // size of surface
    private int overlayW;
    private int overlayH;

    // scale factor
    private double scaleW;
    private double scaleH;

    // resulting bitmap size
    private int bitmapW;
    private int bitmapH;

    // bitmap to be used as back buffer for drawing
    private Bitmap backBuffer;

    // image processor for preprocessing
    private ImageProcessor imageProcessor;

    // provides advanced image slicing
    private ImageSlicer slicer;


    // paint used to draw borders
    private Paint redPaint;
    private Button snap;
    private Button save;

    private EditText expected;
    private List<Image> images;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.main);

        scanArea = (ImageView) findViewById(R.id.scanarea);
        resultArea = (ImageView) findViewById(R.id.workarea);

        redPaint = new Paint();
        redPaint.setColor(0xffFF0000);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(1);


        // create camera manager
        cameraManager = new CameraManager();

        // initialize surface view for preview purposes
        surfaceView = (SurfaceView) findViewById(R.id.preview);
        preview = surfaceView.getHolder();
        preview.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        preview.addCallback(this);

        // set up buttons for scan and  save
        snap = (Button) findViewById(R.id.snap);
        snap.setOnClickListener(this);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        expected = (EditText) findViewById(R.id.expectedText);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "paunsing");
        cameraManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resuming execution");
        if (haveSurface) {
            try {
                Log.d(LOG_TAG, "existing surface - start camera now");
                startCamera();
            } catch (IOException e) {
                Log.e(LOG_TAG, "error starting preview in on resume", e);
            }
        }
    }

    /**
     * perfrom necessary operations to start camera
     *
     * @throws IOException
     */
    private void startCamera() throws IOException {
        cameraManager.start(preview);
        setUpImagesAndBitmaps();
    }


    /**
     * fire up image processing in separate thread
     */
    private void startProcessing() {
        Thread worker = new Thread(new Runnable() {
            public void run() {
                acquireAndProcess();
            }
        });

        worker.start();
    }

    /**
     * acquire and process image
     */
    private void acquireAndProcess() {
        // first request auto focus
        Log.d(LOG_TAG, "autofocus requested");
        if (!cameraManager.doAutofocus()) {
            Log.d(LOG_TAG, "autofocus failed");
            return;
        }
        Log.d(LOG_TAG, "autofocus obtained");

        // perform haptic feedback
        scanArea.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING | HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);

        byte[] previewFrame = cameraManager.getPreviewFrame();

        Log.d(LOG_TAG, "got preview frame" + previewFrame);

        // recompute offsets in case of layout shift due to as display
        computeViewfinderOrigin();

        PixelImage processedImage = imageProcessor.prepareImage(previewFrame,  (int) ((float) viewfinderOriginX / scaleW), (int) ((float) viewfinderOriginY / scaleH));
        Log.d(LOG_TAG, "frame processed" + processedImage);

        List<List<Image>> slices = slicer.sliceUp(processedImage);
        Log.d(LOG_TAG, "sliced up:" + slices.size());


        //  get and save 12 slices from last row
        images = slices.get(slices.size() - 1);
        int length = expected.getText().toString().length();

        if (images.size() >= length) {
            images = images.subList(0, length);
        }

        // copy imiage away to not affect samples
        PixelImage displayImage = new PixelImage(processedImage.getWidth(),processedImage.getHeight());
        processedImage.copy(displayImage);

        // transfer image to B&W ARGB
        final ThresholdFilter argbFilter = new ThresholdFilter(0, BLACK, WHITE);
        argbFilter.process(displayImage);

        Log.d(LOG_TAG, "thresholded back to android B&W");


        // create canvas to draw borders to bitmap
        Canvas canvas = new Canvas(backBuffer);
        // offset , stride, width, height
        canvas.drawBitmap(
                Bitmap.createBitmap(displayImage.pixels,
                        // initial pixel offset
                        (displayImage.getArrayWidth() + 1) * (displayImage.getArrayWidth() - displayImage.getWidth()) / 2,  // offset
                        displayImage.getArrayWidth(), // stride
                        bitmapW, // width
                        bitmapH, //height
                        Bitmap.Config.ARGB_8888), 0, 0, null);

        // draw onto bitmap sliced up images
        for (Image glyph : images) {
            canvas.drawRect(glyph.getOriginX() - processedImage.getOriginX(), glyph.getOriginY() - processedImage.getOriginY(), glyph.getOriginX() + glyph.getWidth() - processedImage.getOriginX(), glyph.getOriginY() + glyph.getHeight() - processedImage.getOriginY(), redPaint);
        }


        Log.d(LOG_TAG, "canvas drawn");
        //  final calls to interface on UI thread, show recognition result
        runOnUiThread(new Runnable() {
            public void run() {
                // display recognition result
                resultArea.setImageBitmap(backBuffer);
                resultArea.invalidate();
                // reenable snap and save
                snap.setEnabled(true);
                save.setEnabled(true);
            }
        }
        );
    }

    /**
     * callback is pretty meaningless
     *
     * @param surfaceHolder
     */
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(LOG_TAG, "surface created");
    }

    /**
     * callback to show us that surface was created and size set.
     * from now on we can start camera preview
     *
     * @param surfaceHolder
     * @param i
     * @param width
     * @param height
     */
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
        haveSurface = true;
        Log.d(LOG_TAG, "surface changed " + width + "x" + height);
        overlayW = width;
        overlayH = height;
        try {
            startCamera();
        } catch (IOException e) {
            Log.e(LOG_TAG, "error starting preview", e);
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        haveSurface = false;
        cameraManager.stop();
    }

    /**
     * react on button press
     *
     * @param view
     */
    public void onClick(View view) {
        if (view == snap) {
            snap.setEnabled(false);
            save.setEnabled(false);
            // snap image
            startProcessing();
        } else if (view == save) {
            // do it only once
            save.setEnabled(false);
            try {
                saveSample();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * save actual sample
     */
    private void saveSample() throws IOException {
        final String exp = expected.getText().toString();

        File outdir = new File(Environment.getExternalStorageDirectory() + "/" + LOG_TAG);
        outdir.mkdirs();
        final DataOutputStream dos = new DataOutputStream(new FileOutputStream(outdir.getAbsolutePath() + "/" + exp + "_" + (System.currentTimeMillis() / 1000) + ".dat"));


        dos.writeInt(images.size());
        // write images sequencially
        for (Image image : images) {
            dos.writeInt(image.getWidth());
            dos.writeInt(image.getHeight());
            for (int i = 0; i < image.getHeight();i++) {
                image.iterateH(i);
                while (image.hasNext()) {
                    dos.writeInt(image.next());
                }
            }
        }

        dos.close();
    }


    /**
     * encapsulate logic to compute viewfinder origin. it is lower left corner of viewfinder wedge
     */
    private void computeViewfinderOrigin() {
        int[] absPos = new int[2];
        scanArea.getLocationOnScreen(absPos);


        viewfinderOriginX = absPos[0];
        viewfinderOriginY = absPos[1];

        // subtract origin of preview view
        surfaceView.getLocationOnScreen(absPos);

        viewfinderOriginX -= absPos[0];
        viewfinderOriginX -= absPos[1];
    }


    /**
     * set up images and bitmaps to adjust for change in screen size
     */
    private void setUpImagesAndBitmaps() {

        Camera.Size previewSize = cameraManager.getPreviewSize();
        Log.d(LOG_TAG, "preview width:" + previewSize.width + " preview height:" + previewSize.height);

        // compute and prepare working images

        // size of preview area in screen coordinates
        int viewfinderH = scanArea.getBottom() - scanArea.getTop();
        int viewfinderW = scanArea.getRight() - scanArea.getLeft();

        // scaling factor between preview image and screen coordinates
        scaleW = (float) overlayW / (float) previewSize.width;
        scaleH = (float) overlayH / (float) previewSize.height;

        // bitmap size
        bitmapW = (int) ((float) viewfinderW / scaleW);
        bitmapH = (int) ((float) viewfinderH / scaleH);

        // bitmap to draw information
        backBuffer = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888);

        imageProcessor =  new SauvolaImageProcessor(previewSize.width,previewSize.height,bitmapW, bitmapH, 0, 1);
        // slicer receivers template image which will hold integral image copy
        slicer = new IntegralImageSlicer(new PixelImage(bitmapW, bitmapH));
    }

}
