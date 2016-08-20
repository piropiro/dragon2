package dragon2.paint;





import mine.util.Point;

import dragon2.ActionBase;
import dragon2.common.Body;

public class ChangePaint extends ActionBase {

	public ChangePaint(Body body, Body body1) {
		ba = body;
		bb = body1;
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.S(1, 0, body.getX(), body.getY(), 4);
		PaintBase.V.S(3, 0, body.getX(), body.getY(), 5);
		PaintBase.V.S(4, 0, body.getX(), body.getY(), 0);
	}

	public void actionMain() {
		Point point = new Point(ba.getX(), ba.getY());
		PaintBase.uw.setAnime(-10, 0, point, point);
		PaintBase.uw.setAnime(-8, bb.getImg(), point, point);
		PaintBase.V.S(1, 0, bb.getX(), bb.getY(), 0);
		PaintBase.V.S(2, 0, bb.getX(), bb.getY(), bb.getImg());
		PaintBase.map.repaint();
		PaintBase.map.setBasicPaint();
	}

	private void setStatus() {
		bb.setX(ba.getX());
		bb.setY(ba.getY());
	}

	public void mouseMoved(Point point) {
		rightPressed();
	}

	public void leftPressed() {
		setStatus();
		PaintBase.uw.changeChara(ba, bb);
		action();
	}

	public void rightPressed() {
		PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 0);
		PaintBase.V.S(3, 0, ba.getX(), ba.getY(), 1);
		PaintBase.map.repaint();
		PaintBase.map.setBasicPaint();
	}

	private Body ba;
	private Body bb;
}
