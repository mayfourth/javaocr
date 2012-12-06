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

package net.sourceforge.javaocr.filter;

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.ImageFilter;

/**
 * abstract base class for filters operating on single pixels
 */
public abstract class AbstractSinglePixelFilter implements ImageFilter {
    public void process(Image image) {
        final int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            for (image.iterateH(i); image.hasNext();) {
                processPixel(image);
            }
        }
    }

    /**
     * process single image pixel subclass shall retrieve current pixel value with image.next()
     * and store it with image.put() after processing
     * @param image
     */
    protected abstract void processPixel(Image image);
}
