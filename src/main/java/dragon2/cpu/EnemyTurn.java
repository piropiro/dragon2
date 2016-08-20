package dragon2.cpu;





import java.awt.Dimension;
import mine.util.Point;
import java.util.Iterator;

import dragon2.ActionBase;
import dragon2.Rewalk;
import dragon2.Walk;
import dragon2.attack.FightManager;
import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.BodyKind;
import dragon2.common.constant.GameColor;
import dragon2.paint.PaintBase;

public class EnemyTurn extends ActionBase {

	public EnemyTurn() {
		PaintBase.uw.closeHelp();
		action();
	}

	public void mouseMoved(Point point) {
	}

	public void leftPressed() {
	}

	public void rightPressed() {
		PaintBase.uw.waitFast();
	}

	public void rightReleased() {
		PaintBase.uw.waitSlow();
	}

	public void actionMain() {
		PaintBase.uw.closeAPanel();
		PaintBase.uw.sleep(300L);
		for (Iterator iterator = PaintBase.Charas.iterator(); iterator
				.hasNext();) {
			ba = (Body) iterator.next();
			if (ba.isAlive()
					&& !ba.isType(BodyAttribute.ANTI_SLEEP)
					&& (ba.getKind() == BodyKind.DOLL && ba.isType(BodyAttribute.BERSERK) || (GameColor.Companion.isPlayer(ba) ? ba
							.isType(BodyAttribute.CHARM) : !ba.isType(BodyAttribute.CHARM)))) {
				move();
				if (PaintBase.uw.endJudge(ba))
					return;
			}
		}

		PaintBase.uw.mensTurnStart();
	}

	public void move() {
		Rewalk.set(ba);
		boolean flag = false;
		flag |= walk();
		flag |= attack();
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.clear(4, 0, 0);
		if (flag) {
			PaintBase.map.repaint();
			PaintBase.uw.sleep(200L);
		}
	}

	public boolean attack() {
		FightManager fightmanager = new FightManager(ba);
		if (fightmanager.enemySelect()) {
			PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 4);
			fightmanager.enemy();
			return true;
		} else {
			return false;
		}
	}

	public boolean walk() {
		Walk walk1 = new Walk(ba);
		PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 4);
		PaintBase.V.S(4, 0, ba.getX(), ba.getY(), 4);
		setSearchData();
		if (!isWalkable())
			return false;
		setWalkData();
		Point point = getWalkPoint();
		if (ba.getX() == point.x && ba.getY() == point.y) {
			return false;
		} else {
			PaintBase.map.repaint();
			PaintBase.uw.sleep(300L);
			PaintBase.V.S(1, 0, ba.getX(), ba.getY(), 1);
			PaintBase.V.S(4, 0, ba.getX(), ba.getY(), 0);
			walk1.enemy(point.x, point.y);
			return true;
		}
	}

	public void setSearchData() {
		PaintBase.V.clear(1, 1, 0);
		PaintBase.V.J(0, ba.getX(), ba.getY(), 100);
		PaintBase.V.J(1, ba.getX(), ba.getY(), 100);
		Point point;
		switch (ba.getColor()) {
		case BLUE: // '\001'
			point = PaintBase.uw.getCrystal(3);
			break;

		case RED: // '\003'
			point = PaintBase.uw.getCrystal(1);
			break;

		default:
			point = null;
			break;
		}
		if (!GameColor.Companion.isPlayer(ba) && point != null
				&& PaintBase.V.G(1, 0, point.x, point.y) != 0) {
			PaintBase.V.S(1, 1, point.x, point.y, 3);
			return;
		}
		if (!GameColor.Companion.isPlayer(ba) && (ba.getGoalX() != 0 || ba.getGoalY() != 0)) {
			PaintBase.V.fillDia(1, 1, ba.getGoalX(), ba.getGoalY(), 1, 2);
			PaintBase.V.S(1, 1, ba.getGoalX(), ba.getGoalY(), 3);
			return;
		}
		for (Iterator iterator = PaintBase.Charas.iterator(); iterator
				.hasNext();) {
			Body body = (Body) iterator.next();
			if (body.isAlive()
					&& body != ba
					&& (ba.isType(BodyAttribute.CHARM) ? body.getColor() == ba.getColor()
							: body.getColor() != ba.getColor()))
				PaintBase.V.fillDia(1, 1, body.getX(), body.getY(), ba.getScope() - 1, 2);
		}

		for (Iterator iterator1 = PaintBase.Charas.iterator(); iterator1
				.hasNext();) {
			Body body1 = (Body) iterator1.next();
			if (body1.isAlive()
					&& body1 != ba
					&& (ba.isType(BodyAttribute.CHARM) ? body1.getColor() == ba.getColor()
							: body1.getColor() != ba.getColor()))
				PaintBase.V.drawDia(1, 1, body1.getX(), body1.getY(), ba.getScope(), 3);
		}

		for (Iterator iterator2 = PaintBase.Charas.iterator(); iterator2
				.hasNext();) {
			Body body2 = (Body) iterator2.next();
			if (body2.isAlive()
					&& body2 != ba
					&& (ba.isType(BodyAttribute.CHARM) ? body2.getColor() != ba.getColor()
							: body2.getColor() == ba.getColor())
					&& PaintBase.V.G(1, 1, body2.getX(), body2.getY()) != 0)
				PaintBase.V.S(1, 1, body2.getX(), body2.getY(), 1);
		}

	}

	public boolean isWalkable() {
		int i = 255;
		Dimension dimension = PaintBase.V.getSize();
		for (int j = 0; j < dimension.height; j++) {
			for (int k = 0; k < dimension.width; k++)
				if (PaintBase.V.G(1, 1, k, j) != 0)
					i = Math.min(i, PaintBase.V.G(1, 3, k, j));

		}

		if (GameColor.Companion.isPlayer(ba))
			return true;
		if (ba.getRange() == 0)
			return true;
		if (i + ba.getScope() <= ba.getRange() + 1)
			return true;
		return PaintBase.uw.getTurn() >= ba.getLimitTurn();
	}

	public void setWalkData() {
		int i = ba.getX();
		int j = ba.getY();
		int k = 255;
		int l = ba.getX();
		int i1 = ba.getY();
		int j1 = 255;
		int k1 = ba.getX();
		int l1 = ba.getY();
		int i2 = 255;
		Dimension dimension = PaintBase.V.getSize();
		for (int l2 = 0; l2 < dimension.height; l2++) {
			for (int i3 = 0; i3 < dimension.width; i3++) {
				int k3 = PaintBase.V.G(1, 1, i3, l2);
				if (k3 > 1) {
					int j2 = PaintBase.V.G(0, 3, i3, l2);
					if (j2 < k) {
						k = j2;
						i = i3;
						j = l2;
					}
					j2 = PaintBase.V.G(1, 3, i3, l2);
					if (j2 < j1) {
						j1 = j2;
						l = i3;
						i1 = l2;
					}
				}
				if (k3 == 3) {
					int k2 = PaintBase.V.G(0, 3, i3, l2);
					if (k2 < i2) {
						i2 = k2;
						k1 = i3;
						l1 = l2;
					}
				}
			}

		}

		int j3 = ba.getMoveStep();
		if (ba.isType(BodyAttribute.OIL))
			j3 /= 2;
		if (i2 <= j3 + 1)
			PaintBase.V.J(0, k1, l1, 100);
		else if (k < j1 + j3 + 1)
			PaintBase.V.J(0, i, j, 100);
		else if (j1 < 100) {
			PaintBase.V.J(1, l, i1, 100);
			PaintBase.V.copy(1, 3, 0, 3);
		}
	}

	public Point getWalkPoint() {
		int i = ba.getX();
		int j = ba.getY();
		int k = 255;
		Dimension dimension = PaintBase.V.getSize();
		for (int i1 = 0; i1 < dimension.height; i1++) {
			for (int j1 = 0; j1 < dimension.width; j1++)
				if (PaintBase.V.G(1, 0, j1, i1) != 0) {
					int l = PaintBase.V.G(0, 3, j1, i1);
					if (l < k) {
						k = l;
						i = j1;
						j = i1;
					}
				}

		}

		return new Point(i, j);
	}

	Body ba;
}
