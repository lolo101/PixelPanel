package pixelpanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    private static final int N_MAX = 255;
    public static final Function mandelbrot = c -> {
        if (c.modSquare() >= 4.0) {
            return 0;
        }
        Complex z = new Complex(0, 0);
        int n;
        // z(n) = z(n-1)² + c
        for (n = 0; n < N_MAX && z.modSquare() < 4.0; ++n) {
            z = z.mult(z).add(c);
        }
        return (double) n / N_MAX;
    };
    public static final ParametrisedFunction julia = new ParametrisedFunction() {
        // Si c est dans l'ensemble de Mandelbrot, l'ensemble de Julia sera connexe.
        // Il sera une 'poussière de Cantor' sinon.
        private Complex c = new Complex(0, 0);

        @Override
        public Number execute(Complex z) {
            int n;
            // z(n) = z(n-1)² + c
            for (n = 0; n < N_MAX && z.modSquare() < 4.0; ++n) {
                z = z.mult(z).add(c);
            }
            return (double) n / N_MAX;
        }

        @Override
        public void setParameter(Complex c) {
            this.c = c;
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
