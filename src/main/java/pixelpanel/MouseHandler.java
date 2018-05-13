package pixelpanel;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

public class MouseHandler extends MouseAdapter {

	public static final double SCALE_STEP = 2.0;
	private final AffineTransform transform;
	private int x0, y0;

	public MouseHandler(AffineTransform transform) {
		this.transform = transform;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			double s = 1 / SCALE_STEP;
			transform.scale(s, s);

			Component source = (Component) e.getSource();
			int cx = source.getWidth() / 2;
			int cy = source.getHeight() / 2;
			transform.translate(SCALE_STEP * e.getX() - cx, SCALE_STEP * e.getY() - cy);
			source.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		x0 = e.getX();
		y0 = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		transform.translate(x0 - x, y0 - y);
		x0 = x;
		y0 = y;
		((Component) e.getSource()).repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// Scale...
		int rotation = e.getWheelRotation();
		double s = Math.pow(SCALE_STEP, rotation);
		transform.scale(s, s);

		// Then translate to the event location
		int x = e.getX();
		int y = e.getY();
		double k = (1 / s) - 1;
		transform.translate(x * k, y * k);
		((Component) e.getSource()).repaint();
	}
}
