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
import net.sourceforge.javaocr.ImageSlicer;

/**
 * abstract base slicer class encapsulating common functionality
 */
public abstract class AbstractBaseSlicer implements ImageSlicer {
    Image image;
    int empty;
    protected int currentPosition;
    int tolerance;
    protected int border;

    public AbstractBaseSlicer(int empty, Image image,int border) {
        this.empty = empty;
        this.image = image;
        this.border = border;
    }

    /**
     * start slicing from designated position with zero tolerance
     *
     * @param fromY starting Y
     */
    public ImageSlicer slice(int fromY) {
        return slice(fromY, 0);
    }

    /**
     * whether we have next image chunk available
     * @return
     */
    public boolean hasNext() {
        return currentPosition < border;
    }

    /**
     * start horizontal slicing from designated position tolerating some empty rows
     *
     * @param from      starting position
     * @param tolerance amount of empty rows allowed inside consecutive image
     */
    public ImageSlicer slice(int from, int tolerance) {

        boolean rowEmpty;
        this.tolerance = tolerance;
        for (currentPosition = from; currentPosition < border; currentPosition++) {
            rowEmpty = spanEmpty();          
            if (!rowEmpty) {
                break;
            }
        }
        return this;
    }

    /**
     * whether current image span is empty
     *
     * @return
     */
    protected boolean spanEmpty() {
        boolean spanEmpty;
        spanEmpty = true;
        // walk through row
        iterateSpan();
        while (image.hasNext())
            if (image.next() != empty) {
                spanEmpty = false;
                break;
            }
        return spanEmpty;
    }

    /**
     * chisel out sub-image  with pre configured tolerance
     *
     * @return subimage
     */
    public Image next() {
        // we stay on first row of image,  which is per definition non
        // empty (that's why  we terminated previous loop)

        // init tolerance reserve counter
        int toleranceReserve = tolerance + 1;
        // save current position
        int imageStart = currentPosition;
        // iterate over image as long as there rows left and tolerance reserve met
        for (; currentPosition < border && toleranceReserve > 0; currentPosition++) {
            if (spanEmpty()) {
                // decrease reserve
                toleranceReserve--;
            } else {
                // initialise reserve
                toleranceReserve = tolerance + 1;
            }
        }

        // we bailed out.  step back for used up tolerance
        currentPosition -= tolerance - toleranceReserve + 1;

        // ok,  now we are ready with scan did we collected something?
        if( imageStart < currentPosition) {
            // apparently. chisel image
            final Image returnImage = chisel(imageStart);

            // skip to the next image
            slice(currentPosition,tolerance);
            return  returnImage;
        }
        // that's pathologic and shall not happen
        // TODO: maybe bomb with exception?
        return null;
    }

    protected abstract Image chisel(int imageStart);

    protected abstract void iterateSpan();
}
