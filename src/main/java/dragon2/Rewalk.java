package dragon2;





import dragon2.common.Body;
import dragon2.map.Map;
import mine.UnitMap;

public class Rewalk {

	public Rewalk() {
	}

	public static void setup(UnitWorks unitworks, UnitMap unitmap, Map map1) {
		uw = unitworks;
		V = unitmap;
		map = map1;
	}

	public static void set(Body body) {
		x = body.getX();
		y = body.getY();
	}

	public static void rewalk(Body body) {
		V.clear(4, 0, 0);
		V.clear(1, 0, 0);
		V.S(2, 0, body.getX(), body.getY(), 0);
		V.S(3, 0, body.getX(), body.getY(), 0);
		int i = V.G(5, 0, body.getX(), body.getY());
		V.S(5, 0, body.getX(), body.getY(), 0);
		body.setX(x);
		body.setY(y);
		V.S(2, 0, body.getX(), body.getY(), body.getImg());
		V.S(5, 0, body.getX(), body.getY(), i);
		uw.closeHPanel();
		uw.closeTPanel();
		uw.closeAPanel();
		map.setWalkPaint(body);
		map.repaint();
	}

	public static boolean isWalked(Body body) {
		if (body.getX() != x)
			return true;
		return body.getY() != y;
	}

	static UnitWorks uw;
	static UnitMap V;
	static Map map;
	static int x;
	static int y;
}
