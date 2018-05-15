package pixelpanel;

import java.awt.geom.Point2D;

class Complex {

    private final double re, im;

    Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    Complex(Point2D p) {
        this.re = p.getX();
        this.im = p.getY();
    }

    public double modSquare() {
        return re * re + im * im;
    }

    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex mult(Complex c) {
        return new Complex(re * c.re - im * c.im, im * c.re + re * c.im);
    }
}
