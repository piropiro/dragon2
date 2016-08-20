package dragon2.paint;

import mine.util.Point;

import dragon2.ActionBase;
import dragon2.common.Body;
import mine.io.JsonIO;

public class KakuseiPaint extends ActionBase {

	public KakuseiPaint(Body body) {
		sister = body;
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.S(1, 0, body.getX(), body.getY(), 4);
		PaintBase.V.S(3, 0, body.getX(), body.getY(), 2);
		PaintBase.V.S(4, 0, body.getX(), body.getY(), 0);
	}

	public void actionMain() {
		PaintBase.V.S(1, 0, sister.getX(), sister.getY(), 0);
		PaintBase.V.S(3, 0, kakusei.getX(), kakusei.getY(), 0);
		PaintBase.V.S(5, 0, kakusei.getX(), kakusei.getY(), 0);
		Point point = new Point(kakusei.getX(), kakusei.getY());
		PaintBase.uw.setAnime(1, -11, point, point);
		PaintBase.uw.setAnime(1, -9, point, point);
		PaintBase.V.S(2, 0, kakusei.getX(), kakusei.getY(), kakusei.getImg());
		PaintBase.map.repaint();
		PaintBase.map.setBasicPaint();
	}

	private Body getKakuseiData() {
		return JsonIO.read("data/body/kakusei.json", Body[].class)[0];
	}

	private void setStatus() {
		int i = sister.getDefMax() + sister.getMdfMax() + sister.getMisMax();
		kakusei.setStrMax(sister.getStrMax() * 2 + i);
		kakusei.setMstMax(sister.getMstMax() + i);
		kakusei.setHitMax(sister.getHitMax() + i);
		kakusei.setMax();
		kakusei.newType();
		kakusei.setX(sister.getX());
		kakusei.setY(sister.getY());
		kakusei.setGoalX(sister.getGoalX());
		kakusei.setGoalY(sister.getGoalY());
	}

	public void mouseMoved(Point point) {
		rightPressed();
	}

	public void leftPressed() {
		kakusei = getKakuseiData();
		setStatus();
		PaintBase.uw.changeChara(sister, kakusei);
		action();
	}

	public void rightPressed() {
		PaintBase.V.S(1, 0, sister.getX(), sister.getY(), 0);
		PaintBase.V.S(3, 0, sister.getX(), sister.getY(), 1);
		PaintBase.map.repaint();
		PaintBase.map.setBasicPaint();
	}

	private Body sister;
	private Body kakusei;
}
