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

/**
 * computes integral image.   result is stored in  the allocated image
 * to be retrieved.  Basically it is sum of all pixels from top-left part
 * of image relative to current pixel. this filter can work in place, but
 * intended usage tells us to create and cache destination image
 */
public class IntegralImageFilter extends AbstractIntegralImageFilter {

    /**
     * image to be used as result.  size shall match to processed
     * images.  responsibility lies on caller. you are also responsible for format
     * so that values do not overflow
     *
     * @param resultImage
     */
    public IntegralImageFilter(Image resultImage) {
        super(resultImage);
    }



    protected int processPixel(final int pixel) {
        return pixel;
    }


}
