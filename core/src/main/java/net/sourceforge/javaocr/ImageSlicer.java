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
 * slices image into pieces. utilises iterator pattern, concrete implementations are provided for
 * horizontal and vertical slicing
 */
public interface ImageSlicer {
    /**
     * start horizontal slicing iteration from certain position  empty row means new image
     *
     * @param fromY starting Y
     */
    public ImageSlicer slice(int fromY);

    /**
     * start horizontal slicing with defined tolerance
     *
     * @param fromY     starting position
     * @param tolerance amount of empty rows allowed inside consecutive image
     */
    public ImageSlicer slice(int fromY, int tolerance);


    /**
     * whether next slice is available
     *
     * @return availability of next slice
     */
    boolean hasNext();


    /**
     * retrieve next slice and advance
     *
     * @return next image slice
     */
    Image next();
}
