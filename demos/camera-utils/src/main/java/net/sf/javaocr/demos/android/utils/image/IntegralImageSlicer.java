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

package net.sf.javaocr.demos.android.utils.image;

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.filter.IntegralImageFilter;
import net.sourceforge.javaocr.ocr.PixelImage;
import net.sourceforge.javaocr.ocr.Shrinker;
import net.sourceforge.javaocr.ocr.SlicerH;
import net.sourceforge.javaocr.ocr.SlicerV;

import java.util.ArrayList;
import java.util.List;


/**
 * utilise integral images to slice up
 */
public class IntegralImageSlicer implements ImageSlicer {
    public static final int EMPTY_PIXEL = 0;

    IntegralImageFilter integrator;
    private final Shrinker shrinker;

    // destination of integral image
    private Image integrated;

    /**
     * construct for given image size
     *
     * @param w
     * @param h
     */
    public IntegralImageSlicer(int w, int h) {
        this(new PixelImage(w, h));
    }

    /**
     * create with supplied image
     *
     * @param template integral image destination
     */
    public IntegralImageSlicer(Image template) {
        integrated = template;
        integrator = new IntegralImageFilter(integrated);

        shrinker= new Shrinker(EMPTY_PIXEL);
    }

    /**
     * slice image into distinct glyphs
     *
     * @param image image to be sliced
     * @return list of glyphs
     */
    public List<List<Image>> sliceUp(Image image) {
        List<List<Image>> values = new ArrayList();
        int imageHeight = image.getHeight();

        // compute integral image, use later for recognition of glyphs with content
        //integrator.process(image);

        int rowThreshold = imageHeight / 10;

        // sauvola params
        double weight = 0.8;
        double range = image.getWidth() / 2;

        final int windowSize = 10;
        final int halfWindow = windowSize / 2;
        int[] pixels = new int[imageHeight + windowSize];
        int[] pixelSum = new int[imageHeight + windowSize];
        int[] pixelSquare = new int[imageHeight + windowSize];

        // compute cumulative values

        int prevSum = 0;
        int prevSquare = 0;


        for (int i = halfWindow; i < imageHeight + halfWindow; i++) {
            image.iterateH(i - halfWindow);
            int value = 0;
            while (image.hasNext()) {
                value += image.next();
            }
            pixels[i] = value;
            prevSum = pixelSum[i] = prevSum + value;
            prevSquare = pixelSquare[i] = prevSquare + value * value;
        }
        //not sure whether it is necessary
        for (int i = 0; i < halfWindow; i++) {
            pixelSum[i] = pixelSum[halfWindow];
            pixelSquare[i] = pixelSquare[halfWindow];
        }

        for (int i = imageHeight + halfWindow; i < imageHeight + windowSize; i++) {
            pixelSum[i] = pixelSum[imageHeight + 4];
            pixelSquare[i] = pixelSquare[imageHeight + 4];
        }
        // now perform linear sauvola filtering
        for (int i = halfWindow; i < imageHeight + halfWindow; i++) {

            double mxSquares = (pixelSquare[i + halfWindow] - pixelSquare[i - halfWindow]) / (windowSize + 1);
            double mx = (pixelSum[i + halfWindow] - pixelSum[i - halfWindow]) / (windowSize + 1);
            double variance = mxSquares - mx * mx;


            double thr = mx * (1 + weight * (Math.sqrt(variance) / range - 1));
            //  System.err.println("charger: idx:" + i + " value:" + pixels[i] + " variance: " + variance + " thr: " + thr);
            pixels[i] = pixels[i] > thr ? 1 : 0;

        }

        Image augmentedRow = new PixelImage(pixels, 1, imageHeight + windowSize);

        Image collapsedRow = augmentedRow.chisel(0, halfWindow, 1, imageHeight);


        List<Image> candidateRows = new ArrayList();

        SlicerH slicerH = new SlicerH(collapsedRow, EMPTY_PIXEL);
        slicerH.slice(0);
        while (slicerH.hasNext()) {
            Image rowDesc = slicerH.next();

            // System.err.println("charger: candidate row:" + rowDesc);
            if (rowDesc.getHeight() >= rowThreshold) {
                Image candidate = image.chisel(0, rowDesc.getOriginY() - halfWindow, image.getWidth(), rowDesc.getHeight());
                //  System.err.println("charger: accept row:" + candidate);
                candidateRows.add(candidate);
            }
        }
        for (Image row : candidateRows) {
            //System.err.println("charger: candidate row: " + row);
            List<Image> rowList = new ArrayList();
            SlicerV vslicer = new SlicerV(row, EMPTY_PIXEL);
            vslicer.slice(0);
            while (vslicer.hasNext())
                rowList.add(shrinker.shrink(vslicer.next()));
            values.add(rowList);
        }


        return values;

    }

  

}
