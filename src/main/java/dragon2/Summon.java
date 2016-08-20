package dragon2;





import mine.util.Point;
import java.util.Iterator;
import java.util.Vector;

import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import mine.UnitMap;

public class Summon {

	public Summon(Vector vector, UnitWorks unitworks, UnitMap unitmap) {
		uw = unitworks;
		V = unitmap;
		devils = new Body[15];
		int i = 0;
		for (Iterator iterator = vector.iterator(); iterator.hasNext();) {
			Body body = (Body) iterator.next();
			if (body.isType(BodyAttribute.SUMMON)) {
				unitmap.S(0, 0, body.getGoalX(), body.getGoalY(), 25);
				body.setX(body.getGoalX());
				body.setY(body.getGoalY());
				body.setGoalX(0);
				body.setGoalY(0);
				body.setHp(0);
				devils[i++] = body;
			}
		}

	}

	public int getLimitTurn(Point point) {
		int i = 64;
		for (int j = 0; j < devils.length; j++) {
			Body body = devils[j];
			if (body != null && point.x == body.getX() && point.y == body.getY()
					&& i > body.getLimitTurn())
				i = body.getLimitTurn();
		}

		return i;
	}

	public void summon() {
		int i = uw.getTurn();
		for (int j = 0; j < devils.length; j++) {
			Body body = devils[j];
			if (body != null && body.getLimitTurn() <= i
					&& V.G(2, 0, body.getX(), body.getY()) == 0) {
				Point point = new Point(body.getX(), body.getY());
				V.S(0, 0, body.getX(), body.getY(), 24);
				uw.setAnime(1, -8, point, point);
				uw.setAnime(-8, body.getImg(), point, point);
				body.setHp(body.getHpMax());
				devils[j] = null;
			}
		}

	}

	private static Body devils[];
	static final int MAX = 15;
	private UnitWorks uw;
	private UnitMap V;
}
