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

package net.sourceforge.javaocr.awt;

import net.sourceforge.javaocr.ocr.PixelImage;

import java.awt.*;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

/**
 * encapsulates AWT related functionality (image creation out of awt)
 */
public class AwtImage extends PixelImage {

    /**
     *create pixel image out of awt image
     * @param image
     */
    public AwtImage(Image image) {
        super(image.getWidth(null), image.getHeight(null));
        
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, arrayWidth, arrayHeight, pixels, 0, arrayWidth);
        try {
            grabber.grabPixels();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * retrieve awt image out of pixels
     * @param rgbPixels
     * @param width
     * @param height
     * @param comp
     * @return
     */
    public java.awt.Image rgbToImage(int[] rgbPixels, int width, int height, Component comp) {
        return comp.createImage(new MemoryImageSource(width, height, rgbPixels, 0, width));
    }


}
