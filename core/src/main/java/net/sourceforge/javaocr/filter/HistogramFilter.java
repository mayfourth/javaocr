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

import java.util.Arrays;

/**
 * collect image histogram and perform useful calculations on it
 *
 * @author Konstantin Pribluda
 */
public class HistogramFilter extends AbstractSinglePixelFilter {
    int[] defaultSmooth = {1, 2, 1};
    int[] histogramm;
    int totalCount;
    private static final int DEFAULT_BINS = 256;
    final int amount_bins;


    public HistogramFilter() {
        this(DEFAULT_BINS);
    }

    public HistogramFilter(int amount_bins) {
        this.amount_bins = amount_bins;
        histogramm = new int[amount_bins];
    }

    public int[] getHistogramm() {
        return histogramm;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void reset() {
        Arrays.fill(histogramm, 0);
        totalCount = 0;
    }

    @Override
    protected void processPixel(Image image) {
        totalCount++;
        histogramm[image.next()]++;
    }

    /**
     * perform historgramm smoothing woth default coeefficitns { 1,2,1}
     *
     * @deprecated does it makes sense at all?
     */
    public void smooth() {
        fold(defaultSmooth);
    }

    /**
     * perform folding (smoothing, sharpening... depending on coeff array).
     * TODO:  what do we do with border conditions?  zero them? let in peace? assume folding coefficients outside border to be 0?
     *
     * @param folder array contaning folding coefficients. must have odd length
     * @deprecated does it makes sense at all?
     */
    public void fold(int[] folder) throws IllegalArgumentException {
        if (folder.length % 2 == 0)
            throw new IllegalArgumentException("folding array must have odd length");
        int[] result = new int[amount_bins];
        final int start = folder.length / 2;
        final int end = histogramm.length - start;
        for (int i = start; i < end; i++) {
            int sum = 0;
            for (int j = 0; j < folder.length; j++) {
                sum += histogramm[i - start + j] * folder[j];
            }
            result[i] = sum / folder.length;
        }
        histogramm = result;
    }

    /**
     * compute adaptive threshold in iterative way starting from the middle
     * of histogramm. for the best results image shall be normalised
     * or enhanced befode doing this  
     * @return
     */
    public int adaptiveThreshold() {
        return adaptiveThreshold(amount_bins / 2);
    }

    /**
     * recursively compute adaptive threshold
     *
     * @param threshold start value for threshold computation
     * @return
     */
    public int adaptiveThreshold(int threshold) {
        //System.err.println("computing threshold from:" + threshold);
        int mLeft = computeM(0, threshold);
        int mRight = computeM(threshold, amount_bins);
        //System.err.println("left: " + mLeft + " right: " + mRight);
        int newThr = (mLeft + mRight) / 2;
        if(Math.abs(newThr - threshold) <= 1)
            return threshold;
        return adaptiveThreshold(newThr);
    }

    /**
     * compute meadian out of defined span
     *
     * @param from value  (inclusive)
     * @param to   too value (exclusive)
     * @return
     */
    private int computeM(int from, int to) {
        float retval = 0;
        float norm = 0;
        for (int i = from; i < to; i++) {
            retval += histogramm[i] * i;
            norm += histogramm[i];
        }
        if(norm == 0) norm = 1;
        return (int)(retval / norm);
    }
}
