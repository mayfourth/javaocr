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
package net.sourceforge.javaocr.ocrPlugins.charExtractor;

import net.sourceforge.javaocr.scanner.DocumentScanner;
import net.sourceforge.javaocr.scanner.DocumentScannerListenerAdaptor;
import net.sourceforge.javaocr.scanner.PixelImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Saves all the characters in an image to an output directory individually. 
 * @author William Whitney
 */
public class CharacterExtractor extends DocumentScannerListenerAdaptor
{

    private int num = 0;
    private DocumentScanner documentScanner = new DocumentScanner();
    private File outputDir = null;
    private File inputImage = null;
    private int std_width;
    private int std_height;

    public void slice(File inputImage, File outputDir, int std_width, int std_height)
    {
        try
        {
            this.std_width = std_width;
            this.std_height = std_height;
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
            Logger.getLogger(CharacterExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void processChar(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2)
    {
        try
        {
            int areaW = x2 - x1;
            int areaH = y2 - y1;

            //Extract the character
            BufferedImage characterImage = ImageIO.read(inputImage);
            characterImage = characterImage.getSubimage(x1, y1, areaW, areaH);

            //Scale image so that both the arrayHeight and arrayWidth are less than std size
            if (characterImage.getWidth() > std_width)
            {
                //Make image always std_width wide
                double scaleAmount = (double) std_width / (double) characterImage.getWidth();
                AffineTransform tx = new AffineTransform();
                tx.scale(scaleAmount, scaleAmount);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                characterImage = op.filter(characterImage, null);
            }

            if (characterImage.getHeight() > std_height)
            {
                //Make image always std_height tall
                double scaleAmount = (double) std_height / (double) characterImage.getHeight();
                AffineTransform tx = new AffineTransform();
                tx.scale(scaleAmount, scaleAmount);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                characterImage = op.filter(characterImage, null);
            }

            //Paint the scaled image on a white background
            BufferedImage normalizedImage = new BufferedImage(std_width, std_height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = normalizedImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, std_width, std_height);

            //Center scaled image on new canvas
            int x_offset = (std_width - characterImage.getWidth()) / 2;
            int y_offset = (std_height - characterImage.getHeight()) / 2;

            g.drawImage(characterImage, x_offset, y_offset, null);
            g.dispose();

            //Save new image to file
            File outputfile = new File(outputDir + File.separator + "char_" + num + ".png");
            ImageIO.write(normalizedImage, "png", outputfile);
            num++;
        }
        catch (Exception ex)
        {
            Logger.getLogger(CharacterExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static final Logger LOG = Logger.getLogger(CharacterExtractor.class.getName());
}
