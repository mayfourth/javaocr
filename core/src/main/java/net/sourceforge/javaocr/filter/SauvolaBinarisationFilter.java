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
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * Performs local image threshloding with Sauvola algorithm.
 * allocates integral images to achieve O(N) performance instead  ow O(N2W2).
 * This filter allocates working images and can  be reused with destination image of same size.
 * Please note,  that  window/2 pixels on borders of image are invalid. It is responsibility of caller
 * to provide suitable padding.
 * <p/>
 * Based on "Efficient Implementation of Local Adaptive Thresholding Techniques Using Integral Images" by
 * Faisal Shafait , Daniel Keysers , Thomas M. Breuel from Image Understanding and Pattern Recognition (IUPR) Research Group
 * German Research Center for Artificial Intelligence (DFKI) GmbH
 * D-67663 Kaiserslautern, Germany
 * and  Thomas M. Breuel from Department of Computer Science, Technical University of Kaiserslautern
 * D-67663 Kaiserslautern, Germany
 *
 * @author Konstantin Pribluda
 */
public class SauvolaBinarisationFilter extends MedianFilter {


    final protected int above;
    final protected int below;
    final protected int range;
    final protected double weight;
    private SquaredIntergalImageFilter squaredIntergalImageFilter;

    private Image squaresImage;

    /**
     * @param above       value to use for pixels above threshold
     * @param below       value to use for pixels below threshold
     * @param destination destination image
     * @param maxValue    maxValue of intensity values
     * @param weight      weight of variance term (determines relation of threshold to local mean)
     * @param window      computing Window size
     */
    public SauvolaBinarisationFilter(int above, int below, Image destination, int maxValue, double weight, int window) {
        super(destination, window);
        this.above = above;
        this.below = below;

        this.range = maxValue / 2;
        this.weight = weight;


        // augmented images have empty borders for kernel processing
        squaresImage = new PixelImage(destination.getWidth(), destination.getHeight());
        squaredIntergalImageFilter = new SquaredIntergalImageFilter(squaresImage);
    }

    /**
     * traversal will be done by median filter, actual processing delegated
     * to derived method
     *
     * @param image
     */
    @Override
    public void process(Image image) {
        // compute squares first, as we need them for processing of individual pixels
        squaredIntergalImageFilter.process(image);
        super.process(image);
    }

    @Override
    protected int computePixel(Image image, int y, int x) {
        double mean = super.computePixel(image, y, x);

        double meanSquaresSum = squaredIntergalImageFilter.windowValue(x - getHalfWindow(), y - getHalfWindow(), x + getHalfWindow(), y + getHalfWindow()) / getSquareWindow();

        // this is our supercool local variance
        double variance = meanSquaresSum - mean * mean;

        double thr = mean * (1 + weight * (Math.sqrt(variance) / range - 1));

        if (image.get(x, y) > thr) {
            return above;
        } else {
            return below;
        }
    }

    protected int retrievePixel(Image image, int y, int x) {
        return image.get(x, y);
    }

    public int getAbove() {
        return above;
    }

    public int getBelow() {
        return below;
    }

    public int getRange() {
        return range;
    }

    public double getWeight() {
        return weight;
    }

    public SquaredIntergalImageFilter getSquaredIntergalImageFilter() {
        return squaredIntergalImageFilter;
    }
}
