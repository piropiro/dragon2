package dragon2.paint;





import mine.util.Point;

import dragon2.ActionBase;
import dragon2.Rewalk;
import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.GameColor;
import dragon2.common.constant.Texts;

public class EndPaint extends ActionBase {

	public EndPaint(Body body) {
		ba = body;
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.S(1, 0, body.getX(), body.getY(), 3);
		PaintBase.V.S(3, 0, body.getX(), body.getY(), 1);
		setHelp();
	}

	private void setHelp() {
		if (!GameColor.Companion.isPlayer(ba)) {
			return;
		} else {
			PaintBase.uw.setHelp(Texts.help[11], 1);
			return;
		}
	}

	public void actionMain() {
		Point point = PaintBase.map.getWaku();
		if (point.x == ba.getX() && point.y == ba.getY())
			PaintBase.uw.setEnd(ba, false);
		else
			PaintBase.uw.setEnd(ba, true);
	}

	public void leftPressed() {
		PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 0);
		action();
	}

	public void rightPressed() {
		Rewalk.rewalk(ba);
	}

	public boolean isNextPoint(Point point) {
		Body body = PaintBase.uw.search(point.x, point.y);
		if (body == null)
			return false;
		if (body.isType(BodyAttribute.ANTI_SLEEP))
			return false;
		if (PaintBase.V.G(3, 0, point.x, point.y) != 0)
			return false;
		if (GameColor.Companion.isPlayer(body)) {
			if (body.isType(BodyAttribute.CHARM))
				return false;
		} else if (!body.isType(BodyAttribute.CHARM))
			return false;
		return true;
	}

	private Body ba;
}
