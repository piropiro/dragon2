package dragon2.paint;





import mine.util.Point;

import dragon2.ActionBase;
import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;

public class BerserkPaint extends ActionBase {

	public BerserkPaint(Body body) {
		ba = body;
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.S(1, 0, body.getX(), body.getY(), 4);
		PaintBase.V.S(3, 0, body.getX(), body.getY(), 6);
		PaintBase.V.S(4, 0, body.getX(), body.getY(), 0);
	}

	public void actionMain() {
		PaintBase.V.S(3, 0, ba.getX(), ba.getY(), 0);
		Point point = new Point(ba.getX(), ba.getY());
		PaintBase.uw.setAnime(1, -1, point, point);
		PaintBase.uw.setAnime(-7, 12, point, point);
		PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 0);
		PaintBase.map.setBasicPaint();
	}

	private void setStatus() {
		ba.setTypeState(BodyAttribute.BERSERK, true);
		ba.setHp(ba.getHpMax());
		ba.setStr((int)(ba.getStr() * 1.5D));
		ba.setDef((int)(ba.getDef() * 1.5D));
		ba.setMst((int)(ba.getMst() * 1.5D));
		ba.setMdf((int)(ba.getMdf() * 1.5D));
		ba.setHit((int)(ba.getHit() * 1.5D));
		ba.setMis((int)(ba.getMis() * 1.5D));
	}

	public void mouseMoved(Point point) {
		rightPressed();
	}

	public void leftPressed() {
		setStatus();
		PaintBase.uw.bersekChara(ba);
		action();
	}

	public void rightPressed() {
		PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 0);
		PaintBase.V.S(3, 0, ba.getX(), ba.getY(), 1);
		PaintBase.map.repaint();
		PaintBase.map.setBasicPaint();
	}

	private Body ba;
}
