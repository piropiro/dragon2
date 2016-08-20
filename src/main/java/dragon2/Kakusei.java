package dragon2;





import mine.util.Point;
import java.util.Iterator;
import java.util.Vector;

import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import dragon2.paint.PaintBase;
import mine.DataStream;
import mine.UnitMap;
import mine.io.BeanIO;
import mine.io.JsonIO;

public class Kakusei extends ActionBase {

	public Kakusei(Vector vector) {
		Charas = vector;
		sister = getSister(vector);
		kakusei = getKakuseiData();
		if (!isChangeable()) {
			return;
		} else {
			setStatus();
			vector.remove(sister);
			vector.add(kakusei);
			action();
			return;
		}
	}

	private Body getSister(Vector vector) {
		for (Iterator iterator = vector.iterator(); iterator.hasNext();) {
			Body body = (Body) iterator.next();
			if (body.isType(BodyAttribute.SISTER))
				return body;
		}

		return null;
	}

	private Body getKakuseiData() {
		return JsonIO.read("data/body/kakusei.json", Body[].class)[0];
	}

	private boolean isChangeable() {
		if (sister == null)
			return false;
		if (!sister.isAlive())
			return false;
		return kakusei != null;
	}

	private void setStatus() {
		int i = sister.getDefMax() + sister.getMdfMax() + sister.getMisMax();
		kakusei.setStrMax(sister.getStrMax() * 2 + i);
		kakusei.setMstMax(sister.getMstMax() + i);
		kakusei.setHitMax(sister.getHitMax() + i);
		kakusei.setMoveStep(sister.getMoveStep() * 2);
		kakusei.setMax();
		kakusei.newType();
		kakusei.setX(sister.getX());
		kakusei.setY(sister.getY());
	}

	public void actionMain() {
		PaintBase.V.S(3, 0, kakusei.getX(), kakusei.getY(), 0);
		PaintBase.V.S(5, 0, kakusei.getX(), kakusei.getY(), 0);
		Point point = new Point(kakusei.getX(), kakusei.getY());
		PaintBase.uw.setAnime(1, -11, point, point);
		PaintBase.uw.setAnime(1, -9, point, point);
		PaintBase.V.S(2, 0, kakusei.getX(), kakusei.getY(), kakusei.getImg());
		PaintBase.map.repaint();
		PaintBase.map.setBasicPaint();
	}

	public void leftPressed() {
	}

	public void rightPressed() {
	}

	private Body sister;
	private Body kakusei;
}
