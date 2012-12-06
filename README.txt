JavaOCR is pure java image processing library with focus on OCR.  As this is pure java and
does not use AWT code it is suitable for android.  JavaOCR os split in several modules.

Module overview

core - core image processing stuff (images over linear arrays, some basic filters,
       image slicing and traversal code)
plugins - separated plugin modules (awt - utilises AWT routines for image processing,
       fir - FIR filtering, moment - invariant moments computation, cluster analysis)
demos - usage demos, focused on android with complete roundtrip (sample acquisition,
       training, recognition)


Legacy code

Some code is orphaned and never reached release quality.  This code is moved into legacy subdirectory.
If you like to adopt and improve it - you are welcome.
