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

package net.sourceforge.javaocr.plugin.cluster.extractor;

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.cluster.FeatureExtractor;
import net.sourceforge.javaocr.filter.AbstractSinglePixelFilter;

/**
 * extracts amount of contiguous free spaces in a glyph.  As it is basically integer value,
 * and also pretty stable - some classificators utilizing statistical methods may barf due to zero variance
 * TODO: I'm not happy with this location - maybe it is best somewhere else
 * 
 * @author Konstantin Pribluda
 */
public class FreeSpacesExtractor extends AbstractSinglePixelFilter implements FeatureExtractor {
    int[] workingScan;
    // count ot found contiguous spaces
    int spaceCount;
    // index into processing row
    int index;
    //whether we are on a free scan
    boolean free = false;
    // number of currently scanned space group
    int currentSpaceNumber;
    // amount to reduce
    int reductionFactor;

    public int getSize() {
        return 1;
    }

    public double[] extract(Image image) {

        double[] retval = new double[1];
        process(image);
        retval[0] = spaceCount - reductionFactor;
        return retval;
    }


    /**
     * prepare image processing
     *
     * @param image
     */
    @Override
    public void process(Image image) {
        // initialize process
        workingScan = new int[image.getWidth()];
        index = 0;
        free = false;
        spaceCount = 0;
        reductionFactor = 0;
        super.process(image);
    }

    /**
     * process single pixel. a lot of voodoo magic emerged  from
     * test driven development - it is correct but not designed  
     * @param image
     */
    @Override
    protected void processPixel(Image image) {
        // in case next pixel is free
        if (image.next() == 0) {
            // in case we just started new empty scan,
            // but only if there is no free space on top
            if (!free) {
                // starting new scan going from taken position to free
                if (workingScan[index] == 0) {
                    // contiguous spaces number is increased
                    spaceCount++;
                    // current space number is set to  this group
                    currentSpaceNumber = spaceCount;
                } else {
                    // otherwise join actual acan from previous line
                    currentSpaceNumber = workingScan[index];
                }
                // and say we are on free area
                free = true;
            } else {
                // we are on a free scan, check whether wo have joined something from previous
                // line
                if (workingScan[index] > 0 && workingScan[index] != currentSpaceNumber) {
                    // ok, we are joining with another scan. bump reduction factor
                    reductionFactor++;
                    // current space number becomes one from previous line
                    currentSpaceNumber = workingScan[index];
                    // propagate actual value at index to the left completely
                    // to the right it is correct
                    for (int i = index - 1; i >= 0; i--) {
                        if (workingScan[i] == 0)
                            break;
                        workingScan[i] = currentSpaceNumber;
                    }
                }
            }
            // claim this space count to be of current scan
            workingScan[index] = currentSpaceNumber;
        } else {
            // say we are on claimed area now
            free = false;
            //  and mark it on scan
            workingScan[index] = 0;
        }
        // advance index
        index++;
        // reset index just in case
        if (index >= workingScan.length) {
            index = 0;
            // and prepare to start new scan
            free = false;
        }
    }
}
