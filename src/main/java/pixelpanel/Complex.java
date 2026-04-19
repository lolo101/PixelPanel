package pixelpanel;

import java.awt.geom.Point2D;

public record Complex(double re, double im) {

    Complex(Point2D p) {
        this(p.getX(), p.getY());
    }
}
