# PixelPanel
Plain old Java Swing app that shows fractals

## Usage
The application's panel displays two parts with different but related fractals.

You can use the mouse to navigate in the fractals :

- clic and drag to pan the view
- double-clic to zoom in at the pointer position and center the image
- scroll to zoom in or out at the center of the image

On the right side is a fractal named *Mandelbrot set*.
You can clic on a point of the fractal to set that point as a parameter of the left-side fractal named *Julia set*.
The relation between thoses two fractals is such : if the selected point of the Mandelbrot set is non-zero (colored) then the Julia set will display a nice fractal. If the selected point is zero (black), then the Julia set will be a *Fatou dust* and is not very interesting :)

The nicest Julia sets are obtain by selecting points on the edge of the Mandelbrot set.
