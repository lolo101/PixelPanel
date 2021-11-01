package pixelpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FunctionPanel extends JPanel {

    private final Function function;
    private final AffineTransform transform = AffineTransform.getScaleInstance(1.0, -1.0);

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
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Point2D p = transform(x, y);
                float result = function.execute(new Complex(p)).floatValue();
                image.setRGB(x, y, Color.HSBtoRGB(result, 1f, result));
            }
        }
        g2.drawImage(image, 0, 0, this);

        g2.setColor(Color.WHITE);
        g2.drawString("X" + 1.0 / transform.getScaleX(), 5, 14);
        g2.dispose();
    }

    /**
     * Transforme un point de coordonnées entières x et y de l'espace de la
     * fenêtre graphique en un point de l'espace de la fonction.
     */
    public Point2D transform(int x, int y) {
        Point2D ret = new Point2D.Double(x, y);
        return transform.transform(ret, ret);
    }
}
