package pixelpanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    private static final int N_MAX = 255;
    private static final double MODULUS_MAX = 4.0;

    public static final Function mandelbrot = (re, im) -> iteration(0, 0, re, im);

    public static final ParametrisedFunction julia = new ParametrisedFunction() {
        private Complex c = new Complex(0, 0);

        @Override
        public double execute(double re, double im) {
            return iteration(re, im, c.re(), c.im());
        }

        /**
         * Si c est dans l'ensemble de Mandelbrot, l'ensemble de Julia sera connexe.
         * Il sera une 'poussière de Fatou' (ou 'ensemble de Cantor') sinon.
         */
        @Override
        public void setParameter(Complex c) {
            this.c = c;
        }
    };

    /**
     * z(n) = z(n-1)² + c
     */
    private static double iteration(double zre, double zim, double cre, double cim) {
        for (int i = 0; i < N_MAX; ++i) {
            double zre2 = zre * zre;
            double zim2 = zim * zim;
            if (zre2 + zim2 >= MODULUS_MAX) return (double) i / N_MAX ;
            double nextRe = zre2 - zim2 + cre;
            zim = 2 * zre * zim + cim;
            zre = nextRe;
        }
        return 1.0;
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new FractalFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
