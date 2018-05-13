package pixelpanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class SurfaceFunctionPanel extends FunctionPanel {

    public SurfaceFunctionPanel(Function function) {
        super(function);
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
}
