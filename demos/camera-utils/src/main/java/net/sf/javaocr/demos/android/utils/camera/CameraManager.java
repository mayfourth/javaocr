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

package net.sf.javaocr.demos.android.utils.camera;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * android camera manager encapsulating camera management and image acquisition
 * @author Konstantin Pribluda
 */
public class CameraManager implements Camera.AutoFocusCallback, Camera.PreviewCallback {

    private static final String LOG_TAG = "camera_manager";

    Camera camera;


    boolean cameraActive = false;
    boolean previewActive = false;
    private Camera.Size previewSize;
    public static final int MIN_PREVIEW_WIDTH = 480;

    boolean focusState;



    // whether we are waiting for focus now, guard agains
    // possible race condition

    boolean waitingForFocus = false;
    // whether we are waiting for preview frame right now
    boolean waitingForPreview = false;

    // pointer to preview image.  this is managed  by camera app
    // and shall be never allocated and deallocated,
    byte[] previewImage = null;

    /**
     * preview size ist read only
     *
     * @return
     */
    public Camera.Size getPreviewSize() {
        return previewSize;
    }

    /**
     * start camera manager
     * (call it in onStart() )
     */
    public synchronized void start(SurfaceHolder holder) throws IOException {
        Log.d(LOG_TAG, " starting requested ");
        if (!cameraActive) {
            Log.d(LOG_TAG, " starting");
            camera = Camera.open();

            startPreview(holder);
            cameraActive = true;
            waitingForFocus = false;
            waitingForPreview = false;

            Log.d(LOG_TAG, " started");
        }
    }

    /**
     * stop camera manager and give up resources
     * (shall be called from onPause() )
     */
    public synchronized void stop() {
        if (cameraActive) {
            if (camera != null) {
                stopPreview();
                camera.release();
                camera = null;
            }
            cameraActive = false;
        }
    }


    /**
     * configure camera, start preview and pipe it to surface holder
     * use it to whenever you are starting preview
     * - in resume
     * - when surface changed
     * - on config changes etc
     *
     * @param holder
     */
    private void startPreview(SurfaceHolder holder) throws IOException {
        if (!previewActive) {
            Log.d(LOG_TAG, " starting preview ");

            Camera.Parameters cameraParameters = camera.getParameters();
            // print out parameters
            Log.d(LOG_TAG, "flash modes:" + cameraParameters.getSupportedFlashModes());
            Log.d(LOG_TAG, "autofocus:" + cameraParameters.getSupportedFocusModes());
            Log.d(LOG_TAG, "preview formats:" + cameraParameters.getSupportedPreviewFormats());
            Log.d(LOG_TAG, "scene modes:" + cameraParameters.getSupportedSceneModes());
            Log.d(LOG_TAG, "white balance modes:" + cameraParameters.getSupportedWhiteBalance());


            // set preview display destination
            camera.setPreviewDisplay(holder);

            // set camera parameters  and activate preview

            // as we know,  that big preview size can produce RE on HTC Hero,
            // we just iterate through allowed preview sizes until we find something proper
            //  go for maximal allowed preview size

            previewSize = cameraParameters.getPreviewSize();

            if (previewSize.width <= MIN_PREVIEW_WIDTH) {
                Log.d(LOG_TAG, "preview size is too small:" + previewSize.width + "x" + previewSize.height);
                final List<Camera.Size> sizes = cameraParameters.getSupportedPreviewSizes();
                Collections.sort(sizes, new Comparator<Camera.Size>() {
                    public int compare(Camera.Size o1, Camera.Size o2) {
                        return new Integer(o2.width).compareTo(o1.width);
                    }
                });

                for (Camera.Size size : sizes) {
                    cameraParameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(cameraParameters);
                    Log.d(LOG_TAG, "attempt preview size:" + size.width + "x" + size.height);
                    try {
                        camera.startPreview();
                        Log.d(LOG_TAG, "...accepted - go along");
                        //  ok, camera accepted out settings.  since we know,
                        // that some implementations may choose different preview format,
                        // we retrieve parameters again. just to be sure
                        cameraParameters = camera.getParameters();
                        break;
                    } catch (RuntimeException rx) {
                        // ups, camera did not like this size
                        Log.d(LOG_TAG, "...barfed, try next");
                    }

                }
            } else {
                Log.d(LOG_TAG, " accepted default preview size on the spot:" + previewSize.width + "x" + previewSize.height);
                camera.startPreview();
            }

            // in case it was modified
            previewSize = cameraParameters.getPreviewSize();


            previewActive = true;
        }
    }

    /**
     * stop preview before starting it again
     */
    private void stopPreview() {
        if (previewActive) {
            camera.stopPreview();
            previewActive = false;
            previewSize = null;
        }
    }

    /**
     * return camera object of any
     *
     * @return
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * whether we are active at the moment
     *
     * @return
     */
    public boolean isCameraActive() {
        return cameraActive;
    }

    /**
     * acquire autofocus. this method blocks current thread until autofocus  callback
     * is called.
     *
     * @return autofocus callback
     */
    public synchronized boolean doAutofocus() {

        Log.d(LOG_TAG, "autofocus requested");
        // if camera is not active, no autofocus possible
        if (!isCameraActive())
            return false;

        // if we are already waiting for focus,  return false on the spot
        // ( is it possible at all?)
        if (waitingForFocus)
            return false;

        focusState = false;

        // start autofocus attempt
        camera.autoFocus(this);

        // wait till autofocus callback

        try {
            wait();
        } catch (InterruptedException e) {
            // we were interrupted. no autofocus
            focusState = false;
        }
        //
        waitingForFocus = false;
        Log.d(LOG_TAG, "autofocus ready");
        return focusState;
    }


    /**
     * acquires preview frame.  thread is blocked until callback is fired
     *
     * @return byte data of preview frame,  null is not successfull
     */
    public synchronized byte[] getPreviewFrame() {
        Log.d(LOG_TAG, "preview frame requested");
        // if camera is not active, no autofocus possible
        if (!isCameraActive())
            return null;

        // if we are already waiting for focus,  return false on the spot
        // ( is it possible at all?)
        if (waitingForPreview)
            return null;

        waitingForPreview = true;

        previewImage = null;
        // request snapshot right now
        camera.setOneShotPreviewCallback(this);
        try {
            wait();
        } catch (InterruptedException e) {
            // we were interrupted.no preview image
            return previewImage = null;
        }
        waitingForPreview = false;
        return previewImage;
    }

    /**
     * synchronize on object itself to prevent race condition
     *
     * @param b      status of ayto focus
     * @param camera camera acquiring focus
     */
    public synchronized void onAutoFocus(boolean b, Camera camera) {
        // autofocus was received.  notify waiting threads and save state
        Log.d(LOG_TAG, "autofocus callback received");
        focusState = b;
        notify();
    }

    /**
     * save data to pointer and wake up sleeping thread.
     *
     * @param bytes  byte array containig image data
     * @param camera camera object
     */
    public synchronized void onPreviewFrame(byte[] bytes, Camera camera) {
        Log.d(LOG_TAG, "preview frame received");
        previewImage = bytes;
        notify();
    }
}
