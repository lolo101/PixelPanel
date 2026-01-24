package pixelpanel;

import java.awt.geom.Point2D;

public record Complex(double re, double im) {

    Complex(Point2D p) {
        this(p.getX(), p.getY());
    }

    public double modSquare() {
        return re * re + im * im;
    }

    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex square() {
        return new Complex(re * re - im * im, im * re + re * im);
    }
}
