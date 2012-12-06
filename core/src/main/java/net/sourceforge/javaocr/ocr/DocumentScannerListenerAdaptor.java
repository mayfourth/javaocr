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

/**
 * Empty implementation of DocumentScannerListener interface which can be
 * subclassed and only have the needed methods overridden.  This prevents
 * implementing classes from being forced to implement all methods in the
 * interface.
 * @author Ronald B. Cemer
 * @deprecated this functionality is provided by slicers
 */
public class DocumentScannerListenerAdaptor
        implements DocumentScannerListener
{

    public void beginDocument(Image pixelImage)
    {
    }

    public void beginRow(Image pixelImage, int y1, int y2)
    {
    }

    public void processChar(Image pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2)
    {
    }

    public void processSpace(Image pixelImage, int x1, int y1, int x2, int y2)
    {
    }

    public void endRow(Image pixelImage, int y1, int y2)
    {
    }

    public void endDocument(Image pixelImage)
    {
    }
}
