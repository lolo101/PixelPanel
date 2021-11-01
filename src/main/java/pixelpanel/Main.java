package pixelpanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    private static final int N_MAX = 255;
    private static final double MODULUS_MAX = 4.0;

    public static final Function mandelbrot = c -> iteration(new Complex(0, 0), c, 0);

    public static final ParametrisedFunction julia = new ParametrisedFunction() {
        private Complex c = new Complex(0, 0);

        @Override
        public Number execute(Complex z) {
            return iteration(z, c, 0);
        }

        /**
         * Si c est dans l'ensemble de Mandelbrot, l'ensemble de Julia sera connexe.
         * Il sera une 'poussière de Fatou' sinon.
         */
        @Override
        public void setParameter(Complex c) {
            this.c = c;
        }
    };

    /**
     * z(n) = z(n-1)² + c
     */
    private static Number iteration(Complex z, Complex c, int step) {
        if (z.modSquare() >= MODULUS_MAX || step >= N_MAX) return (double) step / N_MAX;
        return iteration(z.square().add(c), c, step + 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new FractalFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
