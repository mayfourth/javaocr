Simple android demo to serve as technology  demonstrator,
or application to collect samples for training of OCR
applications.

You will have to create project for your favorite IDE yourself,
as this can not be shared.


Using it

1.  Start application
2.  Enter expected text into edit field
3.  Aim to text (dark on light, uniform lighting not necessary as adaptive thresholding
can deal with it)
4.  Press "snap" button.

=== Application attempts to perform autofocus and snaps image derectlz after ===

5. If enough glyphs were found ( same amount as expected ) - they are highlighted green
and recognition/saving is allowed

6. press "save" to teach meatchers and save sample for later processing

This application is just a demo ho it can work in real life.  It is based loosely on:

https://market.android.com/details?id=de.pribluda.android.ocrcall&feature=more_from_developer

Feel free to play around and tune recognition paratemetrs (feature extractors etc)