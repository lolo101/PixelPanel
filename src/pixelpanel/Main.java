package pixelpanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    private static final int N_MAX = 255;
    public static final Function mandelbrot = (double... p) -> {
        double cx = p[0];
        double cy = p[1];
        if (cx * cx + cy * cy >= 4.0) {
            return 0;
        }
        double zx = 0;
        double zy = 0;
        int n;
        // z(n) = z(n-1)² + c
        for (n = 0; n < N_MAX && zx * zx + zy * zy < 4.0; ++n) {
            double x = zx * zx - zy * zy + cx;
            double y = 2 * zx * zy + cy;
            zx = x;
            zy = y;
        }
        return (double) n / N_MAX;
    };
    public static final ParametrisedFunction julia = new ParametrisedFunction() {
        // Si c est dans l'ensemble de Mandelbrot, l'ensemble de Julia sera connexe.
        // Il sera une 'poussière de Cantor' sinon.
        private double cx, cy;

        @Override
        public Number execute(double... p) {
            double zx = p[0];
            double zy = p[1];
            int n;
            // z(n) = z(n-1)² + c
            for (n = 0; n < N_MAX && zx * zx + zy * zy < 4.0; ++n) {
                double x = zx * zx - zy * zy + cx;
                double y = 2 * zx * zy + cy;
                zx = x;
                zy = y;
            }
            return (double) n / N_MAX;
        }

        @Override
        public void setParameter(double... p) {
            cx = p[0];
            cy = p[1];
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new FractalFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
