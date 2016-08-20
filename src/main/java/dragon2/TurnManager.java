package dragon2;

import mine.util.Point;
import java.util.Iterator;
import java.util.Vector;

import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.BodyKind;
import dragon2.common.constant.GameColor;
import dragon2.common.constant.MoveType;
import dragon2.cpu.EnemyTurn;
import dragon2.paint.PaintBase;

public class TurnManager extends ActionBase {

	public TurnManager(Vector vector, UnitWorks unitworks) {
		Charas = vector;
		uw = unitworks;
	}

	public void reset() {
		turn = 0;
	}

	public int getTurn() {
		return turn;
	}

	public void change(int i) {
		type = i;
		if (i == 0)
			turn++;
		action();
	}

	public void actionMain() {
		switch (type) {
		case 0: // '\0'
			mensTurnStart();
			break;

		case 1: // '\001'
			enemyTurnStart();
			break;


		}
	}

	private void mensTurnStart() {
		turnChange(true);
		PaintBase.map.setBasicPaint();
		PaintBase.map.repaint();
	}

	private void enemyTurnStart() {
		turnChange(false);
		uw.limitOver();
		EnemyTurn enemyturn = new EnemyTurn();
		PaintBase.map.setPaintListener(enemyturn);
	}

	private void turnChange(boolean flag) {
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.clear(3, 0, 0);
		PaintBase.V.clear(4, 0, 0);
		for (Iterator iterator = Charas.iterator(); iterator.hasNext();) {
			Body body = (Body) iterator.next();
			if (body.isAlive()) {
				if (!body.isType(BodyAttribute.S_WAIT))
					PaintBase.V.S(5, 0, body.getX(), body.getY(), 0);
				body.setTypeState(BodyAttribute.SORA, false);
				body.setTypeState(BodyAttribute.RIKU, false);
				setTikei(body, flag);
				setPoison(body, flag);
				setHeal(body, flag);
				setBersek(body, flag);
				setStatus(body, BodyAttribute.ATTACK_UP, 4);
				setStatus(body, BodyAttribute.GUARD_UP, 5);
				setStatus(body, BodyAttribute.CLOSE, 3);
				setStatus(body, BodyAttribute.OIL, 10);
				setStatus(body, BodyAttribute.CHARM, 6);
				setStatus(body, BodyAttribute.ANTI_SLEEP, 1);
				if (body.isType(BodyAttribute.S_WAIT)) {
					body.setTypeState(BodyAttribute.S_WAIT, false);
					if (body.isType(BodyAttribute.CHARM) || body.isType(BodyAttribute.ANTI_SLEEP))
						body.setTypeState(BodyAttribute.S_LOCK, true);
				}
			}
		}

	}

	private void setStatus(Body body, BodyAttribute i, int j) {
		if (!body.isType(BodyAttribute.S_WAIT))
			body.setTypeState(i, false);
		else if (body.isType(i))
			PaintBase.V.S(5, 0, body.getX(), body.getY(), j);
	}

	private void setTikei(Body body, boolean flag) {
		if (Walk.getTikei(body) == 1)
			return;
		Point point = new Point(body.getX(), body.getY());
		switch (PaintBase.V.G(0, 0, body.getX(), body.getY())) {
		case 17: // '\021'
			if (GameColor.Companion.isPlayer(body) != flag)
				return;
			if (body.getHp() == body.getHpMax() && !body.isType(BodyAttribute.POISON)) {
				return;
			} else {
				body.setTypeState(BodyAttribute.POISON, false);
				uw.setAnime(1, -1, point, point);
				body.setHp(body.getHp() + Math.max(2, body.getHpMax() / 4));
				body.setHp(Math.min(body.getHp(), body.getHpMax()));
				return;
			}

		case 3: // '\003'
		case 4: // '\004'
			if (body.isType(BodyAttribute.ANTI_ALL))
				return;
			if (body.isType(BodyAttribute.SWIM_ABLE))
				return;
			if (body.getMoveType() == MoveType.SWIM)
				return;
			if (body.getMoveType() == MoveType.TWIN)
				return;
			body.setTypeState(BodyAttribute.CLOSE, true);
			if (body.isType(BodyAttribute.ANTI_SLEEP))
				return;
			if (body.isType(BodyAttribute.CHARM)) {
				return;
			} else {
				body.setTypeState(BodyAttribute.S_WAIT, true);
				return;
			}

		case 7: // '\007'
			if (body.isType(BodyAttribute.ANTI_ALL))
				return;
			if (body.isType(BodyAttribute.ANTI_POISON)) {
				return;
			} else {
				body.setTypeState(BodyAttribute.POISON, true);
				return;
			}

		case 8: // '\b'
			if (body.isType(BodyAttribute.ANTI_ALL))
				return;
			body.setTypeState(BodyAttribute.OIL, true);
			if (body.isType(BodyAttribute.ANTI_SLEEP))
				return;
			if (body.isType(BodyAttribute.CHARM)) {
				return;
			} else {
				body.setTypeState(BodyAttribute.S_WAIT, true);
				return;
			}

		case 9: // '\t'
			if (GameColor.Companion.isPlayer(body) != flag)
				return;
			if (body.isType(BodyAttribute.FIRE_0))
				return;
			int i = 4;
			if (body.isType(BodyAttribute.FIRE_200))
				i *= 2;
			if (body.isType(BodyAttribute.OIL))
				i *= 2;
			if (body.isType(BodyAttribute.FIRE_50))
				i /= 2;
			uw.setAnime(1, -10, point, point);
			body.setHp(body.getHp() - Math.max(2, (body.getHpMax() * i) / 16));
			body.setHp(Math.max(1, body.getHp()));
			// fall through

		case 5: // '\005'
		case 6: // '\006'
		case 10: // '\n'
		case 11: // '\013'
		case 12: // '\f'
		case 13: // '\r'
		case 14: // '\016'
		case 15: // '\017'
		case 16: // '\020'
		default:
			return;
		}
	}

	private void setPoison(Body body, boolean flag) {
		if (!body.isType(BodyAttribute.POISON))
			return;
		if (GameColor.Companion.isPlayer(body) != flag || body.getHp() == 1) {
			PaintBase.V.S(5, 0, body.getX(), body.getY(), 2);
		} else {
			Point point = new Point(body.getX(), body.getY());
			uw.setAnime(-7, 2, point, point);
			body.setHp(body.getHp() - Math.max(1, body.getHpMax() / 8));
			body.setHp(Math.max(1, body.getHp()));
		}
		if (body.getHp() == 1)
			body.setTypeState(BodyAttribute.POISON, false);
	}

	private void setBersek(Body body, boolean flag) {
		if (body.getKind() != BodyKind.DOLL)
			return;
		if (!body.isType(BodyAttribute.BERSERK))
			return;
		if (GameColor.Companion.isPlayer(body) != flag) {
			PaintBase.V.S(5, 0, body.getX(), body.getY(), 12);
		} else {
			body.setStr(Math.max(0, body.getStr() - 1));
			body.setDef(Math.max(0, body.getDef() - 1));
			body.setMst(Math.max(0, body.getMst() - 1));
			body.setMdf(Math.max(0, body.getMdf() - 1));
			body.setHit(Math.max(0, body.getHit() - 1));
			body.setMis(Math.max(0, body.getMis() - 1));
			Point point = new Point(body.getX(), body.getY());
			uw.setAnime(-7, 12, point, point);
		}
	}

	private void setHeal(Body body, boolean flag) {
		if (!body.isType(BodyAttribute.HEAL))
			return;
		if (GameColor.Companion.isPlayer(body) != flag || body.getHp() == body.getHpMax())
			PaintBase.V.S(5, 0, body.getX(), body.getY(), 7);
		else if (body.getMst() > 0) {
			Point point = new Point(body.getX(), body.getY());
			uw.setAnime(-7, 7, point, point);
			body.setMst(Math.max(0, body.getMst() - 2));
			body.setHp(Math.min(body.getHpMax(), body.getHp() + body.getHpMax() / 2));
		} else {
			body.setTypeState(BodyAttribute.HEAL, false);
		}
	}

	public void setSelectBody(Body body) {
	}

	public void mouseMoved(Point point) {
	}

	public void leftPressed() {
	}

	public void rightPressed() {
		uw.waitFast();
	}

	public void rightReleased() {
		uw.waitSlow();
	}

	static final int MENS = 0;
	static final int ENEMY = 1;
	static final int REMOTE = 2;
	private int turn;
	private int type;
	private Vector Charas;
	private UnitWorks uw;
}
