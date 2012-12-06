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
package net.sourceforge.javaocr.ocr;

import net.sourceforge.javaocr.Image;


/**
 * contains pixel representation of an image
 *
 * @author Ronald B. Cemer
 * @author Konstantin Pribluda
 */
public class PixelImage extends AbstractLinearImage {


    /**
     * An array of pixels.  we make no assumption about components here
     */
    public final int[] pixels;


    /**
     * create empty pixel image
     *
     * @param height
     * @param width
     */
    public PixelImage(int width, int height) {
        this(new int[width * height], width, height, 0, 0, width, height);
    }

    /**
     * Construct a new <code>PixelImage</code> object from an array of
     * pixels.
     *
     * @param pixels An array of pixels.
     * @param width  Width of the image, in pixels.
     * @param height Height of the image, in pixels.
     */
    public PixelImage(int[] pixels, int width, int height) {
        this(pixels, width, height, 0, 0, width, height);
    }

    public PixelImage(int[] pixels, int width, int height, int originX, int originY, int boxW, int boxH) {
        super(width, height, originX, originY, boxW, boxH);
        this.pixels = pixels;
    }

    @Override

    public int get() {
        return pixels[currentIndex];
    }

    @Override
    public void put(int value) {
        pixels[currentIndex] = value;

    }

    public Image chisel(int fromX, int fromY, int width, int height) {
        return new PixelImage(pixels, arrayWidth, arrayHeight, originX + fromX, originY + fromY, width, height);
    }

    @Override
    public String toString() {
        return "PixelImage{} " + super.toString();
    }
}
