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
package net.sourceforge.javaocr.ocrPlugins.lineExtractor;

import net.sourceforge.javaocr.scanner.DocumentScanner;
import net.sourceforge.javaocr.scanner.DocumentScannerListenerAdaptor;
import net.sourceforge.javaocr.scanner.PixelImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Saves all the characters in an image to an output directory individually.
 * @author William Whitney
 */
public class LineExtractor extends DocumentScannerListenerAdaptor
{

    private int num = 0;
    private DocumentScanner documentScanner = new DocumentScanner();
    private File outputDir = null;
    private File inputImage = null;

    public void slice(File inputImage, File outputDir)
    {
        try
        {
            this.inputImage = inputImage;
            this.outputDir = outputDir;
            Image img = ImageIO.read(inputImage);
            PixelImage pixelImage = new PixelImage(img);
            pixelImage.toGrayScale(true);
            pixelImage.filter();
            documentScanner.scan(pixelImage, this, 0, 0, pixelImage.width, pixelImage.height);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }


    public void beginRow(PixelImage pixelImage, int y1, int y2)
    {
        try
        {
            int areaH = y2 - y1;
            BufferedImage img = ImageIO.read(inputImage);
            int areaW = img.getWidth();
            img = img.getSubimage(0, y1, areaW, areaH);
            File outputfile = new File(outputDir + File.separator + "line_" + num + ".png");
            ImageIO.write(img, "png", outputfile);
            num++;
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    private static final Logger LOG = Logger.getLogger(LineExtractor.class.getName());
}
