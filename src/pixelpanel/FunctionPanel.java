package pixelpanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

public class FunctionPanel extends JPanel {

    protected final Function function;
    protected final AffineTransform transform = AffineTransform.getScaleInstance(1.0, -1.0);

    public FunctionPanel(Function function) {
        this.function = function;
        MouseHandler mouseHandler = new MouseHandler(transform);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        int width = preferredSize.width;
        int height = preferredSize.height;
        int minSize = Math.min(width, height);
        transform.setToScale(1.0, -1.0);
        if (minSize > 4 * MouseHandler.SCALE_STEP) {
            // On zoom si on a de la place
            // On cherche n entier tel que 2^n soit le plus proche de 4 / minSize
            // Car on veut afficher les points (x,y) avec -2 < x < 2 et -2 <y < 2
            long n = Math.round((Math.log(4) - Math.log(minSize)) / Math.log(MouseHandler.SCALE_STEP));
            double s = Math.pow(MouseHandler.SCALE_STEP, n);
            transform.scale(s, s);
        }
        // Et on centre
        transform.translate(-width >> 1, -height >> 1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getForeground());

        Path2D path = new Path2D.Double(Path2D.WIND_NON_ZERO, width);
        for (int x = 0; x < width; ++x) {
            Point2D px = transform(x, 0);
            double dx = px.getX();
            double dy = function.execute(dx).doubleValue();
            if (Double.isNaN(dy)) {
                if (path.getCurrentPoint() != null) {
                    g2.draw(path);
                    path.reset();
                }
            } else {
                try {
                    Point2D py = new Point2D.Double(dx, dy);
                    transform.inverseTransform(py, py);
                    if (path.getCurrentPoint() == null) {
                        path.moveTo(py.getX(), py.getY());
                    } else {
                        path.lineTo(py.getX(), py.getY());
                    }
                } catch (NoninvertibleTransformException ex) {
                    throw new AssertionError(ex);
                }
            }
        }
        g2.draw(path);

        g2.drawString("X" + 1.0 / transform.getScaleX(), 5, 14);
        g2.dispose();
    }

    /**
     * Transforme un point de coordonnées entières x et y de l'espace de la
     * fenêtre graphique en un point de l'espace de la fonction.
     *
     * @param x
     * @param y
     * @return
     */
    public Point2D transform(int x, int y) {
        Point2D ret = new Point2D.Double(x, y);
        return transform.transform(ret, ret);
    }
}
