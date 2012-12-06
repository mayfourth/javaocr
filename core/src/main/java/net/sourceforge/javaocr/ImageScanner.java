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
 * Interface contract to perform document scanning
 *
 * @author Konstantin Pribluda
 * @deprecated - do we need this interface at all?
 */
public interface ImageScanner {
    /**
     * scan document and generate events for interested parties
     *
     * @param image    Image to be scanned
     * @param listener Listener receiving events
     * @param left     boundary
     * @param top      boundary
     * @param right    boundary
     * @param bottom   boundary
     */
    void scan(
            Image image,
            DocumentScannerListener listener,
            int left,
            int top,
            int right,
            int bottom);
}
