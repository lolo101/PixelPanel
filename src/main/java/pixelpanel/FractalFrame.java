package pixelpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FractalFrame extends JFrame {

    private static final String PARAM_FORMAT = "c = %g + %gi";
    private final JLabel parameterLabel = new JLabel(String.format(PARAM_FORMAT, 0., 0.));
    private final FunctionPanel panelMandelbrot = new FunctionPanel(Main.mandelbrot);
    private final JPanel panelJulia = new FunctionPanel(Main.julia);
    private final MouseListener parameterMouseSelector = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                Point2D c = panelMandelbrot.transform(e.getX(), e.getY());
                parameterLabel.setText(String.format(PARAM_FORMAT, c.getX(), c.getY()));
                Main.julia.setParameter(new Complex(c));
                panelJulia.repaint();
            }
        }
    };

    public FractalFrame() throws HeadlessException {
        super("Fractal Factory");

        parameterLabel.setForeground(Color.RED);
        JPanel parameterPanel = new JPanel();
        FlowLayout layout = (FlowLayout) parameterPanel.getLayout();
        layout.setAlignment(FlowLayout.CENTER);
        parameterPanel.add(parameterLabel);
        add(parameterPanel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.LINE_AXIS));
        add(centerPanel, BorderLayout.CENTER);

        panelJulia.setPreferredSize(new Dimension(640, 480));
        centerPanel.add(panelJulia);

        panelMandelbrot.setPreferredSize(new Dimension(640, 480));
        panelMandelbrot.addMouseListener(parameterMouseSelector);
        centerPanel.add(panelMandelbrot);
    }
}
