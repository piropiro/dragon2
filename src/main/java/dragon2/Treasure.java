package dragon2;





import mine.util.Point;
import java.util.ArrayList;
import java.util.List;

import dragon2.common.Body;
import dragon2.common.constant.Texts;
import dragon2.common.constant.BodyAttribute;
import mine.UnitMap;

public class Treasure {

	public Treasure(List<Body> vector, UnitWorks unitworks, UnitMap unitmap) {
		uw = unitworks;
		V = unitmap;
		item = null;
		treasure = new Body[30];
		holder = new Body[30];
		status = new int[30];
		Sources = new ArrayList<>();
		Comment = new ArrayList<>();
		Point point = unitworks.getCrystal(1);
		int i = 0;
		for (Body body : vector) {
			if (unitworks.have(body)) {
				body.setTypeState(BodyAttribute.S_LOCK, true);
				body.setHp(0);
			} else if (body.getX() == point.x && body.getY() == point.y) {
				item = body;
				body.setHp(0);
				status[i] = 4;
				i++;
			} else {
				switch (body.getKind()) {
				case CLASS: // '\001'
				case WEPON: // '\002'
				case ARMOR: // '\003'
				case ITEM: // '\004'
				case DOLL: // '\''
					treasure[i] = body;
					body.setHp(0);
					i++;
					break;
				default:
				}
			}
		}

		for (int j = 0; j < i; j++) {
			Body body1 = treasure[j];
			if (body1 != null && (body1.getGoalX() != 0 || body1.getGoalY() != 0)) {
				status[j] = 2;
				for (Body body3 : vector) {
					if (body3 != body1 && body3.isAlive()
							&& body3.getX() == body1.getGoalX() && body3.getY() == body1.getGoalY()) {
						holder[j] = body3;
						status[j] = 1;
					}
				}

			}
		}

		for (int k = 0; k < i; k++)
			if (holder[k] == null) {
				Body body2 = treasure[k];
				if (body2 != null && body2.getLimitTurn() != 0) {
					unitmap.S(0, 0, body2.getX(), body2.getY(), 21);
					status[k] = 3;
				}
			}

	}

	public int getLimitTurn(Point point) {
		for (int i = 0; i < treasure.length; i++) {
			Body body = treasure[i];
			if (body != null && holder[i] == null && point.x == body.getX()
					&& point.y == body.getY())
				return body.getLimitTurn();
		}

		return 0;
	}

	public int getLimitTurn() {
		if (item == null)
			return 0;
		else
			return item.getLimitTurn();
	}

	public String getCount() {
		String s = "";
		for (int i = 0; i < treasure.length; i++)
			switch (status[i]) {
			case 0: // '\0'
			default:
				break;

			case 1: // '\001'
				if (holder[i].isAlive() && isAlive(treasure[i]))
					s = s + Texts.kigo[0];
				else
					s = s + Texts.kigo[1];
				break;

			case 2: // '\002'
				if (isAlive(treasure[i]))
					s = s + Texts.kigo[2];
				else
					s = s + Texts.kigo[3];
				break;

			case 3: // '\003'
				if (isAlive(treasure[i]))
					s = s + Texts.kigo[4];
				else
					s = s + Texts.kigo[5];
				break;

			case 4: // '\004'
				if (isAlive(item))
					s = s + Texts.kigo[6];
				else
					s = s + Texts.kigo[7];
				break;

			case 5: // '\005'
				s = s + Texts.kigo[8];
				break;
			}

		return s;
	}

	public List<Body> getSources() {
		return Sources;
	}

	public void add(Body body) {
		Sources.add(body);
		uw.countItem();
	}

	public void limitOver() {
		for (int i = 0; i < treasure.length; i++) {
			Body body = treasure[i];
			if (body != null && body.getLimitTurn() == uw.getTurn()
					&& V.G(0, 0, body.getX(), body.getY()) == 21) {
				V.S(0, 0, body.getX(), body.getY(), 23);
				Point point = new Point(body.getX(), body.getY());
				uw.setAnime(-7, 11, point, point);
				uw.sleep(300L);
				V.S(5, 0, body.getX(), body.getY(), 0);
			}
		}

	}

	public void addMember(Body body) {
		add(body);
		getTreasure(body, true);
		message();
	}

	public void getTreasure(Body body, boolean flag) {
		for (int i = 0; i < holder.length; i++)
			if (body == holder[i]) {
				Body body1 = treasure[i];
				if (isAlive(body1) && (body1.getLimitTurn() != 0 || flag)) {
					add(body1);
					Comment.add(new Integer(i));
					status[i] = 5;
				}
			}

	}

	public void searchTreasure(Body body) {
		for (int i = 0; i < treasure.length; i++) {
			Body body1 = treasure[i];
			if (isAlive(body1) && holder[i] == null && body1.getX() == body.getX()
					&& body1.getY() == body.getY()) {
				treasure[i] = null;
				status[i] = 5;
				message(body, body1);
				add(body1);
				if (V.G(0, 0, body.getX(), body.getY()) == 21)
					V.S(0, 0, body.getX(), body.getY(), 22);
			}
		}

	}

	public void getClearItem() {
		if (!isAlive(item)) {
			return;
		} else {
			message(null, item);
			add(item);
			return;
		}
	}

	private boolean isAlive(Body body) {
		if (body == null)
			return false;
		if (body.getLimitTurn() == 0)
			return true;
		return body.getLimitTurn() >= uw.getTurn();
	}

	public void message() {
		for (int i : Comment) {
			Body body = holder[i];
			Body body1 = treasure[i];
			uw.setMPanel(body.getName() + Texts.ha);
			uw.setMPanel(Texts.treasure1);
			uw.setMPanel(body1.getName() + Texts.wo);
			uw.setMPanel(Texts.treasure2);
			uw.startMPanel(body1);
		}

		Comment.clear();
	}

	public void message(Body body, Body body1) {
		if (body != null) {
			if (body.getName().length() <= 2) {
				uw.setMPanel(body.getName() + Texts.ha + Texts.treasure3);
			} else {
				uw.setMPanel(body.getName() + Texts.ha);
				uw.setMPanel(Texts.treasure3);
			}
		} else {
			uw.setMPanel(Texts.treasure3);
		}
		uw.setMPanel(body1.getName() + Texts.wo);
		uw.setMPanel(Texts.treasure2);
		uw.startMPanel(body1);
	}

	public void down() {
		for (Body body : Sources) {
			switch (body.getKind()) {
			case WEPON: // '\002'
			case ARMOR: // '\003'
			case ITEM: // '\004'
				down(body);
				break;
			default:
			}
		}

	}

	public static void down(Body body) {
		body.setHpMax(Math.max(1, body.getHpMax() - 1));
		body.setStrMax(Math.max(0, body.getStrMax() - 6));
		body.setDefMax(Math.max(0, body.getDefMax() - 6));
		body.setMstMax(Math.max(0, body.getMstMax() - 6));
		body.setMdfMax(Math.max(0, body.getMdfMax() - 6));
		body.setHitMax(Math.max(0, body.getHitMax() - 6));
		body.setMisMax(Math.max(0, body.getMisMax() - 6));
		body.getAttrList().add(BodyAttribute.BADITEM);
		body.setTypeState(BodyAttribute.BADITEM, true);
	}

	private Body item;
	private Body treasure[];
	private Body holder[];
	private int status[];
	static final int MAX = 30;
	static final int S_NONE = 0;
	static final int S_ENEMY = 1;
	static final int S_GROUND = 2;
	static final int S_BOX = 3;
	static final int S_CLEAR = 4;
	static final int S_HAVE = 5;
	private List<Body> Sources;
	private List<Integer> Comment;
	private UnitWorks uw;
	private UnitMap V;
}
