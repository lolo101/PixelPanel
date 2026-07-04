package pixelpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.StructuredTaskScope;

public class FunctionPanel extends JPanel {

    /**
     * {@link Function#execute} renvoie une valeur dans [0, 1] qui ne peut
     * prendre que N_MAX + 1 valeurs distinctes. On précalcule donc la couleur
     * correspondante une fois pour toutes, ce qui évite d'appeler
     * {@link Color#HSBtoRGB} pour chaque pixel.
     */
    private static final int[] PALETTE = new int[256];
    static {
        for (int i = 0; i < PALETTE.length; ++i) {
            float v = i / (float) (PALETTE.length - 1);
            PALETTE[i] = Color.HSBtoRGB(v, 1f, v);
        }
    }

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
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        double[] matrix = new double[6];
        transform.getMatrix(matrix);
        double m00 = matrix[0];
        double m11 = matrix[3];
        double m02 = matrix[4];
        double m12 = matrix[5];

        try (var scope = StructuredTaskScope.open()) {
            for (int y = 0; y < height; ++y) {
                final int row = y;
                scope.fork(
                    () -> {
                        double py = row * m11 + m12;
                        int offset = row * width;
                        for (int x = 0; x < width; ++x) {
                            double px = x * m00 + m02;

                            double result = function.execute(px, py);
                            pixels[offset + x] = PALETTE[(int) (result * (PALETTE.length - 1))];
                        }
                        return null;
                    });
            }
            scope.join();
        } catch (InterruptedException _) {
        }

        Graphics2D g2 = (Graphics2D) g.create();
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
