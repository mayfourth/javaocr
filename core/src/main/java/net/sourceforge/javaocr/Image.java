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
 * Image functionality. Images are opaque objects and hide implementation details
 * (especially storage layout or actual pixel representation). Images provide way to iterate
 * over it (thus, they are statefull and NOT thread safe), and retrieve and set current pixel.
 * Convenience methods to set given pixels are provided, but iterating methods shall be used for
 * high performance bulk operations
 *
 * @author Konstantin Pribluda
 */
public interface Image {


    /**
     * retrieve value of pixel in image specific fashion
     *
     * @param x
     * @param y
     * @return
     */
    int get(int x, int y);

    /**
     * store pixel at specified position
     *
     * @param x
     * @param y
     * @param value
     */
    void put(int x, int y, int value);

    /**
     * retrieve pixekl at current position, does not modify current image pointer
     *
     * @return
     */
    int get();

    /**
     * store pixel at current position, does not modify current image pointer
     *
     * @param value
     */
    void put(int value);

    /**
     * whether horizontal line is empty between specified coordinates
     * in image specific fashion
     *
     * @param y     Y value
     * @param from  inclusive from
     * @param to    inclusive to
     * @param value
     * @return whether line is empty in image specific fashion
     * @deprecated use iterator instead
     */
    boolean horizontalSpanEquals(final int y, final int from, final int to, final int value);

    /**
     * whether vertical line is empty between specified points
     *
     * @param x     X Value
     * @param from  inclusive from
     * @param to    inclusive to
     * @param value
     * @return whether line is empty in image specific fashion
     * @deprecated use iterators instead
     */
    boolean verticalSpanEquals(final int x, final int from, final int to, final int value);

    /**
     * @return image width
     */
    int getWidth();

    /**
     * @return   image height
     */
    int getHeight();

    /**
     * @return origin inside bigger image.  0 if this is not subimage
     */
    public int getOriginX();

    public int getOriginY();

    /**
     * convenience method to initialize iterator over whole image row
     */
    void iterateH(int y);

    /**
     * initialize iterator over part of row
     *
     * @param from from position
     * @param to   to position
     */
    void iterateH(int y, int from, int to);

    /**
     * convenience method to initialize iterator over whole image column
     */
    void iterateV(int x);

    /**
     * initialize iterator over part of column
     *
     * @param from
     * @param to
     */
    void iterateV(int x, int from, int to);

    /**
     * advance and retrieve next available pixel
     *
     * @return
     */
    int next();

    /**
     * store and advance to next pixel
     */
    void next(int pixel);

    /**
     * whether next pixel is available
     *
     * @return
     */
    boolean hasNext();

    /**
     * copy image content to another image
     *
     * @param dst destination image
     */
    void copy(Image dst);

    /**
     * copy image swithcing axes in process
     *
     * @param dst destination image
     */
    void flip(Image dst);

    /**
     * chisel out sub-image out of original one.  be careful with dimensions -
     * no internal checks are done. Operation works in place and no data is copied.
     *
     * @param fromX    subimage origin X
     * @param fromY    subimage origin Y
     * @param width    subimage width
     * @param height   subimage height
     * @return image region as image
     */
    Image chisel(int fromX, int fromY, int width, int height);

    /**
     * full row out of image
     * @param y  coordinate
     * @return row image
     */
    Image row(int y);

    /**
     * full column out of image
     * @param x coordinate
     * @return   column image
     */
    Image column(int x);
    
    /**
     * aspect ration of this image
     * TODO: do we actually need this?
     *
     * @return
     */
    float getAspectRatio();
}
