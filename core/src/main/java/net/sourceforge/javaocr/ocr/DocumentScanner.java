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

import net.sourceforge.javaocr.DocumentScannerListener;
import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.ImageScanner;

import java.util.ArrayList;

/**
 * Utility class to scan a document, breaking it into rows and character blocks.
 * TODO: evaluate and maybe rewrite
 * @author Ronald B. Cemer
 * @author Konstantin Pribluda
 * @deprecated finctionality now in image slicers
 */
public class DocumentScanner implements ImageScanner {

    /**
     * The maximum fraction a row's arrayHeight can be of the previous row's arrayHeight,
     * in order for the new (short) row to be merged in with the previous (tall)
     * row to form a single row.
     */
    protected float shortRowFraction = 0.125f;
    /**
     * The minimum fraction of pixels in an area which must be white in order for the area to
     * be considered whitespace when the liberal whitespace policy is in effect.
     */
    protected float liberalPolicyAreaWhitespaceFraction = 0.95f;
    /**
     * The minimum arrayWidth of a space, expressed as a fraction of the arrayHeight of a row of text.
     */
    protected float minSpaceWidthAsFractionOfRowHeight = 0.6f;
    /**
     * The minimum arrayWidth of a character, expressed as a fraction of the arrayHeight of a row of text.
     */
    protected float minCharWidthAsFractionOfRowHeight = 0.35f;
    /**
     * The minimum arrayWidth of a character break (a vertical column of whitespace that separates
     * two characters on a row of text), expressed as a fraction of the arrayHeight of a row of text.
     */
    protected float minCharBreakWidthAsFractionOfRowHeight = 0.05f;
    /**
     * The white threshold.  Any pixel empty that is greater than or equal to this empty,
     * will be considered to be white space for the purpose of separating rows of text
     * and characters within each row.
     */
    protected int whiteThreshold = 128;

    /**
     * @return The maximum fraction a row's arrayHeight can be of the previous row's arrayHeight,
     *         in order for the new (short) row to be merged in with the previous (tall)
     *         row to form a single row.
     */
    public float getShortRowFraction() {
        return shortRowFraction;
    }

    /**
     * @param shortRowFraction The maximum fraction a row's arrayHeight can be of the previous
     *                         row's arrayHeight, in order for the new (short) row to be merged in with the previous (tall)
     *                         row to form a single row.
     */
    public void setShortRowFraction(float shortRowFraction) {
        this.shortRowFraction = shortRowFraction;
    }

    /**
     * @return The minimum fraction of pixels in an area which must be white in order for
     *         the area to be considered whitespace when the liberal whitespace policy is in effect.
     */
    public float getLiberalPolicyAreaWhitespaceFraction() {
        return liberalPolicyAreaWhitespaceFraction;
    }

    /**
     * @param liberalPolicyAreaWhitespaceFraction
     *         The minimum fraction of pixels in an area which
     *         must be white in order for the area to be considered whitespace when the liberal whitespace
     *         policy is in effect.
     */
    public void setLiberalPolicyAreaWhitespaceFraction(float liberalPolicyAreaWhitespaceFraction) {
        this.liberalPolicyAreaWhitespaceFraction = liberalPolicyAreaWhitespaceFraction;
    }

    /**
     * @return The minimum arrayWidth of a space, expressed as a fraction of the arrayHeight of a row of
     *         text.
     */
    public float getMinSpaceWidthAsFractionOfRowHeight() {
        return minSpaceWidthAsFractionOfRowHeight;
    }

    /**
     * @param minSpaceWidthAsFractionOfRowHeight
     *         The minimum arrayWidth of a space, expressed as a
     *         fraction of the arrayHeight of a row of text.
     */
    public void setMinSpaceWidthAsFractionOfRowHeight(float minSpaceWidthAsFractionOfRowHeight) {
        this.minSpaceWidthAsFractionOfRowHeight = minSpaceWidthAsFractionOfRowHeight;
    }

    /**
     * @return The minimum arrayWidth of a character, expressed as a fraction of the arrayHeight of a row
     *         of text.
     */
    public float getMinCharWidthAsFractionOfRowHeight() {
        return minCharWidthAsFractionOfRowHeight;
    }

    /**
     * @param minCharWidthAsFractionOfRowHeight
     *         The minimum arrayWidth of a character, expressed as a
     *         fraction of the arrayHeight of a row of text.
     */
    public void setMinCharWidthAsFractionOfRowHeight(float minCharWidthAsFractionOfRowHeight) {
        this.minCharWidthAsFractionOfRowHeight = minCharWidthAsFractionOfRowHeight;
    }

    /**
     * @return The minimum arrayWidth of a character break (a vertical column of whitespace that
     *         separates two characters on a row of text), expressed as a fraction of the arrayHeight of
     *         a row of text.
     */
    public float getMinCharBreakWidthAsFractionOfRowHeight() {
        return minCharBreakWidthAsFractionOfRowHeight;
    }

    /**
     * @param minCharBreakWidthAsFractionOfRowHeight
     *         The minimum arrayWidth of a character break (a
     *         vertical column of whitespace that separates two characters on a row of text), expressed
     *         as a fraction of the arrayHeight of a row of text.
     */
    public void setMinCharBreakWidthAsFractionOfRowHeight(float minCharBreakWidthAsFractionOfRowHeight) {
        this.minCharBreakWidthAsFractionOfRowHeight = minCharBreakWidthAsFractionOfRowHeight;
    }

    /**
     * @return The white threshold.  Any pixel empty that is greater than or equal to this empty,
     *         will be considered to be white space for the purpose of separating rows of text
     *         and characters within each row.
     */
    public int getWhiteThreshold() {
        return whiteThreshold;
    }

    /**
     * @param whiteThreshold The white threshold.  Any pixel empty that is greater than or equal
     *                       to this empty, will be considered to be white space for the purpose of separating rows of
     *                       text and characters within each row.
     */
    public void setWhiteThreshold(int whiteThreshold) {
        this.whiteThreshold = whiteThreshold;
    }

    /**
     * @param pixelImage The <code>PixelImage</code> object to be scanned.
     * @param listener   The <code>DocumentScannerListener</code> to receive
     *                   notifications during the scanning process.
     * @param blockX1    The leftmost pixel position of the area to be scanned,
     *                   or <code>0</code> to start scanning at the left boundary of the image.
     * @param blockY1    The topmost pixel position of the area to be scanned,
     *                   or <code>0</code> to start scanning at the top boundary of the image.
     * @param blockX2    The rightmost pixel position of the area to be scanned,
     *                   or <code>0</code> to stop scanning at the right boundary of the image.
     * @param blockY2    The bottommost pixel position of the area to be scanned,
     *                   or <code>0</code> to stop scanning at the bottom boundary of the image.
     */
    public final void scan(
            Image pixelImage,
            DocumentScannerListener listener,
            int blockX1,
            int blockY1,
            int blockX2,
            int blockY2) {


        int w = pixelImage.getWidth();
        int h = pixelImage.getHeight();

        if (blockX1 < 0) {
            blockX1 = 0;
        } else if (blockX1 >= w) {
            blockX1 = w - 1;
        }
        if (blockY1 < 0) {
            blockY1 = 0;
        } else if (blockY1 >= h) {
            blockY1 = h - 1;
        }
        if ((blockX2 <= 0) || (blockX2 >= w)) {
            blockX2 = w - 1;
        }
        if ((blockY2 <= 0) || (blockY2 >= h)) {
            blockY2 = h - 1;
        }


        blockX2++;
        blockY2++;

        boolean whiteLine = true;
        listener.beginDocument(pixelImage);
        // First build list of rows of text.
        ArrayList al = new ArrayList();
        int y1 = 0;
        for (int y = blockY1; y < blockY2; y++) {
            boolean isWhiteSpace = pixelImage.horizontalSpanEquals(y, blockX1, blockX2, 0);

            if (isWhiteSpace) {
                if (!whiteLine) {
                    whiteLine = true;
                    al.add(new Integer(y1));
                    al.add(new Integer(y));
                }
            } else {
                if (whiteLine) {
                    whiteLine = false;
                    y1 = y;
                }
            }
        }
        if (!whiteLine) {
            al.add(new Integer(y1));
            al.add(new Integer(blockY2));
        }
        // Now for each row that looks unreasonably short
        // compared to the previous row, merge the short row into
        // the previous row.  This accommodates characters such as
        // underscores.
        for (int i = 0; (i + 4) <= al.size(); i += 2) {
            int bY0 = ((Integer) (al.get(i))).intValue();
            int bY1 = ((Integer) (al.get(i + 1))).intValue();
            int bY2 = ((Integer) (al.get(i + 2))).intValue();
            int bY3 = ((Integer) (al.get(i + 3))).intValue();
            int row0H = bY1 - bY0;
            int whiteH = bY2 - bY1;
            int row1H = bY3 - bY2;
            if (((row1H <= (int) ((float) row0H * shortRowFraction))
                    || (row1H < 6))
                    && ((whiteH <= (int) ((float) row0H * shortRowFraction))
                    || (whiteH < 6))) {
                al.remove(i + 2);
                al.remove(i + 1);
                i -= 2;
            }
        }
        if (al.size() == 0) {
            al.add(new Integer(blockY1));
            al.add(new Integer(blockY2));
        }
        // Process the rows.
        for (int i = 0; (i + 1) < al.size(); i += 2) {
            int bY1 = ((Integer) (al.get(i))).intValue();
            int bY2 = ((Integer) (al.get(i + 1))).intValue();

///
///System.err.println("process row: "+blockX1+","+bY1+" "+blockX2+","+bY2);
            processRow(pixelImage, listener, w, h, blockX1, bY1, blockX2, bY2);
        }
    }

    private final void processRow(
            Image pixelImage,
            DocumentScannerListener listener,
            int w,
            int h,
            int x1,
            int y1,
            int x2,
            int y2) {

        listener.beginRow(pixelImage, y1, y2);
        int rowHeight = y2 - y1;
        int minCharBreakWidth = Math.max(
                1,
                (int) ((float) rowHeight * minCharBreakWidthAsFractionOfRowHeight));
        int liberalWhitspaceMinWhitePixelsPerColumn =
                (int) ((float) rowHeight * liberalPolicyAreaWhitespaceFraction);
        // First store beginning and ending character
        // X positions and calculate average character spacing.
        ArrayList al = new ArrayList();
        boolean inCharSeparator = true;
        int charX1 = 0, prevCharX1 = -1;
        boolean liberalWhitespacePolicy = false;
        int numConsecutiveWhite = 0;
        for (int x = x1 + 1; x < (x2 - 1); x++) {
            if ((!liberalWhitespacePolicy)
                    && (numConsecutiveWhite == 0)
                    && ((x - charX1) >= rowHeight)) {
                // Something's amiss.  No whitespace.
                // Try again but do it with the liberal whitespace
                // detection algorithm.
                x = charX1;
                liberalWhitespacePolicy = true;
            }
            int numWhitePixelsThisColumn = 0;
            boolean isWhiteSpace = pixelImage.verticalSpanEquals(x,y1,y2-1, 0);

            if ((liberalWhitespacePolicy)
                    && (numWhitePixelsThisColumn
                    < liberalWhitspaceMinWhitePixelsPerColumn)) {
                isWhiteSpace = false;
            }
            if (isWhiteSpace) {
                numConsecutiveWhite++;
                if (numConsecutiveWhite >= minCharBreakWidth) {
                    if (!inCharSeparator) {
                        inCharSeparator = true;
                        al.add(new Integer(charX1));
                        al.add(new Integer(x - (numConsecutiveWhite - 1)));
                    }
                }
            } else {
                numConsecutiveWhite = 0;
                if (inCharSeparator) {
                    inCharSeparator = false;
                    prevCharX1 = charX1;
                    charX1 = x;
                    liberalWhitespacePolicy = false;
                }
            }
        }
        if (numConsecutiveWhite == 0) {
            al.add(new Integer(charX1));
            al.add(new Integer(x2));
        }
        int minSpaceWidth =
                (int) ((float) rowHeight * minSpaceWidthAsFractionOfRowHeight);
        // Next combine concecutive supposed character cells where their
        // leftmost X positions are too close together.
        int minCharWidth =
                (int) ((float) rowHeight * minCharWidthAsFractionOfRowHeight);
        if (minCharWidth < 1) {
            minCharWidth = 1;
        }
        for (int i = 0; (i + 4) < al.size(); i += 2) {
            int thisCharWidth =
                    ((Integer) (al.get(i + 2))).intValue()
                            - ((Integer) (al.get(i))).intValue();
            if ((thisCharWidth < minCharWidth) || (thisCharWidth < 6)) {
                al.remove(i + 2);
                al.remove(i + 1);
                i -= 2;
            }
        }
        // Process the remaining character cells.
        for (int i = 0; (i + 1) < al.size(); i += 2) {
            if (i >= 2) {
                int cx1 = ((Integer) (al.get(i - 1))).intValue();
                int cx2 = ((Integer) (al.get(i))).intValue();
                while ((cx2 - cx1) >= minSpaceWidth) {
                    int sx2 = Math.min(cx1 + minSpaceWidth, cx2);
                    listener.processSpace(pixelImage, cx1, y1, sx2, y2);
                    cx1 += minSpaceWidth;
                }
            }
            int cx1 = ((Integer) (al.get(i))).intValue();
            int cx2 = ((Integer) (al.get(i + 1))).intValue();
            int cy1 = y1;
            // Adjust cy1 down to point to the the top line which is not all white.
            while (cy1 < y2) {
                boolean isWhiteSpace = pixelImage.horizontalSpanEquals(cy1,cx1,cx2-1, 0);
                if (!isWhiteSpace) {
                    break;
                }
                cy1++;
            }
            int cy2 = y2;
            // Adjust cy2 up to point to the the line after the last line
            // which is not all white.
            while (cy2 > cy1) {
                boolean isWhiteSpace = pixelImage.horizontalSpanEquals(cy2-1,cx1,cx2-1, 0);
   
                if (!isWhiteSpace) {
                    break;
                }
                cy2--;
            }
            if (cy1 >= cy2) {
                // Everything is white in this cell.  Make it a space.
                listener.processSpace(pixelImage, cx1, y1, cx2, y2);
            } else {
                listener.processChar(pixelImage, cx1, cy1, cx2, cy2, y1, y2);
            }
        }
        listener.endRow(pixelImage, y1, y2);
    }
}
