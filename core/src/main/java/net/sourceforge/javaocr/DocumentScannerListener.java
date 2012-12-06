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
package net.sourceforge.javaocr;


/**
 * Listener interface for the DocumentScanner utility class.
 *
 * @author Ronald B. Cemer
 * @deprecated use image slicing instead
 */
public interface DocumentScannerListener {

    /**
     * This method is called when scanning of the document begins.
     *
     * @param pixelImage The <code>PixelImage</code> containing the document
     *                   which is being scanned.
     */
    public void beginDocument(Image pixelImage);

    /**
     * This method is called when scanning of a row of text within the document
     * begins.
     *
     * @param pixelImage The <code>PixelImage</code> containing the document
     *                   which is being scanned.
     * @param y1         The y position of the top pixel row of the row of text that
     *                   is being scanned.
     * @param y2         The y position of the pixel row immediately below the last
     *                   pixel row of the row of text that is being scanned.  This is always
     *                   one more than the last pixel row of the row of text that is being
     *                   scanned.
     */
    public void beginRow(Image pixelImage, int y1, int y2);

    /**
     * This method is called to process a character or group of characters
     * within the document.<p>
     * Note that when two or more characters are connected together without
     * enough whitespace to determine where the first one ends and the next
     * one begins, the document scanner may not be able to split the characters
     * properly.  If this occurs, then two or more characters may be included
     * in a single call to this method.  In this case, it is the responsibility
     * of this method to process all characters included in the area passed
     * into this method.
     *
     * @param pixelImage The <code>PixelImage</code> containing the document
     *                   which is being scanned.
     * @param x1         The x position of the first pixel column of the character
     *                   or group of characters being scanned.
     * @param y1         The y position of the top pixel row of the row of text that
     *                   is being scanned.
     * @param x2         The x position of the pixel column immediately to the right
     *                   of the last pixel column of the character or group of characters being
     *                   scanned.  This is always one more than the last pixel column of the
     *                   character or group of characters that is being scanned.
     * @param y2         The y position of the pixel row immediately below the last
     *                   pixel row of the row of text that is being scanned.  This is always
     *                   one more than the last pixel row of the row of text that is being
     *                   scanned.
     * @param rowY1      The y position of the top scan line of the row.
     * @param rowY2      The y position of the scan line immediately below the bottom of the row.
     */
    public void processChar(Image pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2);

    /**
     * This method is called to process a space within the document.<p>
     * A space is simply an area of whitespace between two characters that is
     * deemed by the document scanner to be wide enough to represent a space
     * character.
     *
     * @param pixelImage The <code>PixelImage</code> containing the document
     *                   which is being scanned.
     * @param x1         The x position of the first pixel column of the character
     *                   or group of characters being scanned.
     * @param y1         The y position of the top pixel row of the row of text that
     *                   is being scanned.
     * @param x2         The x position of the pixel column immediately to the right
     *                   of the last pixel column of the character or group of characters being
     *                   scanned.  This is always one more than the last pixel column of the
     *                   character or group of characters that is being scanned.
     * @param y2         The y position of the pixel row immediately below the last
     *                   pixel row of the row of text that is being scanned.  This is always
     *                   one more than the last pixel row of the row of text that is being
     *                   scanned.
     */
    public void processSpace(Image pixelImage, int x1, int y1, int x2, int y2);

    /**
     * This method is called when scanning of a row of text within the document
     * is complete.
     *
     * @param pixelImage The <code>PixelImage</code> containing the document
     *                   which is being scanned.
     * @param y1         The y position of the top pixel row of the row of text that
     *                   is being scanned.
     * @param y2         The y position of the pixel row immediately below the last
     *                   pixel row of the row of text that is being scanned.  This is always
     *                   one more than the last pixel row of the row of text that is being
     *                   scanned.
     */
    public void endRow(Image pixelImage, int y1, int y2);

    /**
     * This method is called when scanning of the document is complete.
     *
     * @param pixelImage The <code>PixelImage</code> containing the document
     *                   which is being scanned.
     */
    public void endDocument(Image pixelImage);
}
