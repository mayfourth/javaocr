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
package net.sourceforge.javaocr.ocrPlugins.aspectRatio;

import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImage;
import net.sourceforge.javaocr.scanner.DocumentScanner;
import net.sourceforge.javaocr.scanner.DocumentScannerListenerAdaptor;
import net.sourceforge.javaocr.scanner.PixelImage;
import net.sourceforge.javaocr.scanner.accuracy.AccuracyListenerInterface;
import net.sourceforge.javaocr.scanner.accuracy.AccuracyProviderInterface;
import net.sourceforge.javaocr.scanner.accuracy.OCRComp;
import net.sourceforge.javaocr.scanner.accuracy.OCRIdentification;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.logging.Logger;

/**
 * Provides an OCR that can be used in addition to other OCR plug-ins to
 * increase accuracy.
 * @author William Whitney
 */
public class AspectRatioOCR extends DocumentScannerListenerAdaptor implements AccuracyProviderInterface
{

    private AccuracyListenerInterface listener;
    private final HashMap<Character, ArrayList<TrainingImage>> trainingImages;
    private final ArrayList<CharacterRatio> charRatioList = new ArrayList<CharacterRatio>();
    private DocumentScanner documentScanner = new DocumentScanner();

    public AspectRatioOCR(HashMap<Character, ArrayList<TrainingImage>> trainingImages)
    {
        this.trainingImages = trainingImages;
        processTrainingImages();

    }

    public void scan(BufferedImage targetBfImage)
    {
        PixelImage pixelImage = new PixelImage(targetBfImage);
        pixelImage.toGrayScale(true);
        pixelImage.filter();
        documentScanner.scan(pixelImage, this, 0, 0, pixelImage.width, pixelImage.height);

    }

    public void acceptAccuracyListener(AccuracyListenerInterface listener)
    {
        this.listener = listener;
    }

    @Override
    public void endRow(PixelImage pixelImage, int y1, int y2)
    {
        //Send accuracy of this identification to the listener
        if (listener != null)
        {
            OCRIdentification identAccuracy = new OCRIdentification(OCRComp.ASPECT_RATIO);
            identAccuracy.addChar('\n', 0.0);
            listener.processCharOrSpace(identAccuracy);
        }
    }

    @Override
    public void processChar(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2)
    {
        int width = x2 - x1;
        int height = y2 - y1;
        double currRatio = getRatio(width, height);

        if (listener != null)
        {
            listener.processCharOrSpace(determineCharacterPossibilities(currRatio));
        }

    }

    @Override
    public void processSpace(PixelImage pixelImage, int x1, int y1, int x2, int y2)
    {
        if (listener != null)
        {
            OCRIdentification identAccuracy = new OCRIdentification(OCRComp.ASPECT_RATIO);
            identAccuracy.addChar(' ', 0.0);
            listener.processCharOrSpace(identAccuracy);
        }
    }

    private void processTrainingImages()
    {
        for (Iterator<Character> it = trainingImages.keySet().iterator(); it.hasNext();)
        {
            Character key = it.next();
            ArrayList<TrainingImage> charTrainingImages = trainingImages.get(key);
            if (charTrainingImages != null)
            {
                for (int i = 0; i < charTrainingImages.size(); i++)
                {
                    int width = charTrainingImages.get(i).width;
                    int height = charTrainingImages.get(i).height;
                    charRatioList.add(new CharacterRatio(key, getRatio(width, height)));
                }
            }
        }

        Collections.sort(charRatioList);
    }
    private static final Logger LOG = Logger.getLogger(AspectRatioOCR.class.getName());

    private double getRatio(int width, int height)
    {
        return ((double) width) / ((double) height);
    }

    private OCRIdentification determineCharacterPossibilities(double targetRatio)
    {
        double smallestError = Double.MAX_VALUE;
        Stack<CharacterRatio> bestResults = new Stack<CharacterRatio>();
        for (CharacterRatio currChar : charRatioList)
        {
            if (Math.abs(currChar.getRatio() - targetRatio) < smallestError)
            {
                smallestError = Math.abs(currChar.getRatio() - targetRatio);
                bestResults.push(currChar);
            }
        }

        OCRIdentification newIdent = new OCRIdentification(OCRComp.ASPECT_RATIO);

        int count = 0;
        while (!bestResults.isEmpty() && count < 5)
        {
            CharacterRatio currChar = bestResults.pop();
            double errorAmount = Math.abs(targetRatio - currChar.getRatio());
            newIdent.addChar(currChar.getCharacter(), errorAmount);
            count++;
        }

        return newIdent;
    }
}
