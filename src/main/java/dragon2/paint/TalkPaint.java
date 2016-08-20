package dragon2.paint;





import mine.util.Point;

import dragon2.ActionBase;
import dragon2.Rewalk;
import dragon2.card.CardPaint;
import dragon2.common.Body;
import dragon2.common.constant.Texts;
import dragon2.common.constant.BodyAttribute;

public class TalkPaint extends ActionBase {

	public TalkPaint(Body body) {
		ba = body;
		target = new Body[4];
		target[0] = getTarget(body.getX() - 1, body.getY());
		target[1] = getTarget(body.getX() + 1, body.getY());
		target[2] = getTarget(body.getX(), body.getY() - 1);
		target[3] = getTarget(body.getX(), body.getY() + 1);
	}

	public void show() {
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.S(3, 0, ba.getX(), ba.getY(), 3);
		PaintBase.V.S(1, 0, ba.getX() - 1, ba.getY(), 2);
		PaintBase.V.S(1, 0, ba.getX() + 1, ba.getY(), 2);
		PaintBase.V.S(1, 0, ba.getX(), ba.getY() - 1, 2);
		PaintBase.V.S(1, 0, ba.getX(), ba.getY() + 1, 2);
		for (int i = 0; i < target.length; i++)
			if (target[i] != null)
				PaintBase.V.S(1, 0, target[i].getX(), target[i].getY(), 3);

		setHelp();
	}

	private void setHelp() {
		PaintBase.uw.setHelp(Texts.help[17], 1);
	}

	public boolean isEnable() {
		for (int i = 0; i < target.length; i++)
			if (target[i] != null)
				return true;

		return false;
	}

	private Body getTarget(int i, int j) {
		Body body = PaintBase.uw.search(i, j);
		if (body == null)
			return null;
		if (body.getColor() == ba.getColor())
			return null;
		if (body.isType(BodyAttribute.ANTI_SLEEP))
			return null;
		if (ba.getLevel() != 0 && body.getLevel() > ba.getLevel())
			return null;
		if (PaintBase.uw.have(body))
			return null;
		if (ba.isType(BodyAttribute.MASTER))
			return body;
		if ((ba.isType(BodyAttribute.ASK) || ba.isType(BodyAttribute.TALKABLE))
				&& (body.isType(BodyAttribute.ASK) || body.isType(BodyAttribute.TALKABLE)))
			return body;
		else
			return null;
	}

	public void actionMain() {
		PaintBase.map.setWaitPaint();
		CardPaint cardpaint = new CardPaint(ba, bb);
		PaintBase.map.setPaintListener(cardpaint);
	}

	public void leftPressed() {
		Point point = PaintBase.map.getWaku();
		if (point.x == ba.getX() && point.y == ba.getY()) {
			PaintBase.map.setEndPaint(ba);
			PaintBase.map.repaint();
		}
		if (PaintBase.V.G(1, 0, point.x, point.y) != 3)
			return;
		bb = getTarget(point.x, point.y);
		if (bb != null) {
			PaintBase.V.clear(1, 0, 0);
			PaintBase.map.repaint();
			PaintBase.uw.closeAPanel();
			action();
		}
	}

	public void rightPressed() {
		Rewalk.rewalk(ba);
	}

	public boolean isNextPoint(Point point) {
		return PaintBase.V.G(1, 0, point.x, point.y) == 3;
	}

	private Body ba;
	private Body bb;
	private Body target[];
}
