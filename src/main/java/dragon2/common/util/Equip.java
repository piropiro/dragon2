package dragon2.common.util;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import dragon2.Statics;
import dragon2.UnitWorks;
import dragon2.attack.AttackData;
import dragon2.common.Body;
import dragon2.common.constant.BodyKind;
import dragon2.common.constant.GameColor;
import dragon2.common.constant.Texts;

public class Equip {

	public Equip(List<Body> bodys, UnitWorks unitworks) {
		uw = unitworks;
		Equips = bodys;
	}

	public Body search(int i, int j) {
		for (Iterator iterator = Equips.iterator(); iterator.hasNext();) {
			Body body = (Body) iterator.next();
			if (body.getX() == i && body.getY() == j)
				return body;
		}

		return null;
	}

	private Body searchItem(int i, int j) {
		for (Iterator iterator = Equips.iterator(); iterator.hasNext();) {
			Body body = (Body) iterator.next();
			if (body.getColor() == GameColor.GREEN && body.getX() == i && body.getY() == j)
				return body;
		}

		return null;
	}

	public void addBody(Body body) {
		Equips.add(body);
	}

	public void removeBody(Body body) {
		Equips.remove(body);
	}

	public List<Body> getEquips() {
		return Equips;
	}

	public void getEXP(Body body, Body body1) {
		int i = body1.getExp();
		if (body.getLevel() != 0)
			if (body1.getLevel() == 0)
				i = (int) ((double) i * 1.5D);
			else if (body.getLevel() > body1.getLevel())
				i /= Math.min((body.getLevel() - body1.getLevel()) + 1, 4);
			else
				i += (i * (body1.getLevel() - body.getLevel())) / 2;
		body.setExp(body.getExp() + i);
		body1.setExp(0);
		if (body.getKind() == BodyKind.DOLL) {
			for (int j = 2; j <= 11; j++) {
				Body body2 = searchItem(body.getGoalX() + j, body.getGoalY());
				if (body2 != null)
					body2.setExp((int) (body2.getExp() + i * 1.5D));
			}

		} else {
			for (int k = 1; k <= 4; k++) {
				Body body3 = searchItem(body.getGoalX() + k, body.getGoalY());
				if (body3 != null)
					body3.setExp(body3.getExp() + i);
			}

		}
	}

	public void levelup(Body body) {
		if (body.getLevel() == 0) {
			if (body.getExp() >= 1000) {
				body.setExp(body.getExp() - 1000);
				uw.setMPanel(body.getName() + Texts.ha);
				uw.setMPanel(Texts.equip1);
				statusup(body);
				uw.startMPanel(body);
			}
			for (int i = 2; i <= 11; i++) {
				Body body2 = searchItem(body.getGoalX() + i, body.getGoalY());
				itemup(body2);
			}

			return;
		}
		Body body1 = searchItem(body.getGoalX() + 1, body.getGoalY());
		Body body3 = searchItem(body.getGoalX() + 2, body.getGoalY());
		Body body4 = searchItem(body.getGoalX() + 3, body.getGoalY());
		Body body5 = searchItem(body.getGoalX() + 4, body.getGoalY());
		itemup(body1);
		itemup(body3);
		itemup(body4);
		itemup(body5);
		while (body.getExp() >= 100) {
			body.setExp(body.getExp() - 100);
			if (body.getName().length() <= 1) {
				uw.setMPanel(body.getName() + Texts.ha + Texts.equip1);
			} else {
				uw.setMPanel(body.getName() + Texts.ha);
				uw.setMPanel(Texts.equip1);
			}
			statusup(body);
			body.setLevel(body.getLevel() + 1);
		}
		attackup(body);
		uw.startMPanel(body);
	}

	private void itemup(Body body) {
		if (body == null)
			return;
		if (body.getExp() < 1000) {
			return;
		} else {
			body.setExp(body.getExp() - 1000);
			body.setLevel(body.getLevel() + 10);
			body.setHpMax((body.getHpMax() * 3) / 2 + 1);
			body.setStrMax((body.getStrMax() * 3) / 2 + 10);
			body.setDefMax((body.getDefMax() * 3) / 2 + 10);
			body.setMstMax((body.getMstMax() * 3) / 2 + 10);
			body.setMdfMax((body.getMdfMax() * 3) / 2 + 10);
			body.setHitMax((body.getHitMax() * 3) / 2 + 10);
			body.setMisMax((body.getMisMax() * 3) / 2 + 10);
			restrict(body);
			return;
		}
	}

	private void attackup(Body body) {
		boolean aflag[] = getAttack(body);
		attacklearn(Texts.buki, aflag[1], body.getAtk()[1]);
		attacklearn(Texts.bougu, aflag[2], body.getAtk()[2]);
		attacklearn(Texts.shokugyo, aflag[3], body.getAtk()[3]);
		attacklearn(Texts.shokugyo, aflag[4], body.getAtk()[4]);
		attacklearn(Texts.komono, aflag[5], body.getAtk()[5]);
	}

	private void attacklearn(String s, boolean flag, int i) {
		if (!flag)
			return;
		AttackData attackdata = Statics.getAttackData(i);
		uw.setMPanel(s + Texts.equip1);
		if (attackdata.getName().length() <= 5) {
			uw.setMPanel(attackdata.getName() + Texts.wo + Texts.equip2);
		} else {
			uw.setMPanel(attackdata.getName() + Texts.wo);
			uw.setMPanel(Texts.equip2);
		}
	}

	private void statusup(Body body) {
		int i = body.getHpMax();
		int j = body.getStrMax() / 10;
		int k = body.getDefMax() / 10;
		int l = body.getMstMax() / 10;
		int i1 = body.getMdfMax() / 10;
		int j1 = body.getHitMax() / 10;
		int k1 = body.getMisMax() / 10;
		Body body1 = searchItem(body.getGoalX() + 1, body.getGoalY());
		Body body2 = searchItem(body.getGoalX() + 4, body.getGoalY());
		if (body.getLevel() == 0) {
			body.setHpMax(body.getHpMax() + 10);
			body.setStrMax(body.getStrMax() * 2);
			body.setDefMax(body.getDefMax() * 2);
			body.setMstMax(body.getMstMax() * 2);
			body.setMdfMax(body.getMdfMax() * 2);
			body.setHitMax(body.getHitMax() * 2);
			body.setMisMax(body.getMisMax() * 2);
		} else if (body1 == null) {
			body.setHpMax(body.getHpMax() + Luck.rnd(2));
			body.setStrMax(body.getStrMax() + Luck.rnd(12));
			body.setDefMax(body.getDefMax() + Luck.rnd(12));
			body.setMstMax(body.getMstMax() + Luck.rnd(12));
			body.setMdfMax(body.getMdfMax() + Luck.rnd(12));
			body.setHitMax(body.getHitMax() + Luck.rnd(12));
			body.setMisMax(body.getMisMax() + Luck.rnd(12));
		} else if (body2 == null) {
			body.setHpMax(body.getHpMax() + (body1.getHpMax() + Luck.rnd(9)) / 10);
			body.setStrMax(body.getStrMax() + body1.getStrMax() / 10);
			body.setDefMax(body.getDefMax() + body1.getDefMax() / 10);
			body.setMstMax(body.getMstMax() + body1.getMstMax() / 10);
			body.setMdfMax(body.getMdfMax() + body1.getMdfMax() / 10);
			body.setHitMax(body.getHitMax() + body1.getHitMax() / 10);
			body.setMisMax(body.getMisMax() + body1.getMisMax() / 10);
		} else {
			body.setHpMax(body.getHpMax() + (body1.getHpMax() + body2.getHpMax() + Luck.rnd(9)) / 10);
			body.setStrMax(body.getStrMax() + (body1.getStrMax() + body2.getStrMax()) / 10);
			body.setDefMax(body.getDefMax() + (body1.getDefMax() + body2.getDefMax()) / 10);
			body.setMstMax(body.getMstMax() + (body1.getMstMax() + body2.getMstMax()) / 10);
			body.setMdfMax(body.getMdfMax() + (body1.getMdfMax() + body2.getMdfMax()) / 10);
			body.setHitMax(body.getHitMax() + (body1.getHitMax() + body2.getHitMax()) / 10);
			body.setMisMax(body.getMisMax() + (body1.getMisMax() + body2.getMisMax()) / 10);
		}
		restrict(body);
		body.setHp(body.getHp() + statusup(Texts.hp, body.getHpMax() * 10, i));
		body.setStr(body.getStr() + statusup(Texts.kougekiryoku, body.getStrMax(), j));
		body.setDef(body.getDef() + statusup(Texts.bougyoryoku, body.getDefMax(), k));
		body.setMst(body.getMst() + statusup(Texts.mahouryoku, body.getMstMax(), l));
		body.setMdf(body.getMdf() + statusup(Texts.teikouryoku, body.getMdfMax(), i1));
		body.setHit(body.getHit() + statusup(Texts.meichuritu, body.getHitMax(), j1));
		body.setMis(body.getMis() + statusup(Texts.kaihiritu, body.getMisMax(), k1));
	}

	private int statusup(String s, int i, int j) {
		int k = i / 10 - j;
		if (k > 0)
			uw.setMPanel(s + Texts.ga + " " + k + Texts.equip3);
		return k;
	}

	public static void restrict(Body body) {
		body.setHpMax(Math.min(999, body.getHpMax()));
		body.setStrMax(Math.min(9999, body.getStrMax()));
		body.setDefMax(Math.min(9999, body.getDefMax()));
		body.setMstMax(Math.min(9999, body.getMstMax()));
		body.setMdfMax(Math.min(9999, body.getMdfMax()));
		body.setHitMax(Math.min(9999, body.getHitMax()));
		body.setMisMax(Math.min(9999, body.getMisMax()));
	}

	public Vector getPlayers() {
		Vector vector = new Vector();
		for (int i = 4; i >= 0; i--) {
			Body body = search(1, 1 + i * 3);
			if (body != null) {
				body.setColor(GameColor.BLUE);
				body.setMax();
				equip(body);
				vector.add(body);
			}
		}

		return vector;
	}

	public Body getChangeChara(Body body) {
		Body body1 = null;
		for (Iterator iterator = Equips.iterator(); iterator.hasNext();) {
			Body body2 = (Body) iterator.next();
			if (GameColor.Companion.isPlayer(body2) && body2.getGoalX() == body.getGoalX() + 7
					&& body2.getGoalY() == body.getGoalY()) {
				body1 = body2;
				break;
			}
		}

		if (body1 == null) {
			return null;
		} else {
			body1.setMax();
			equip(body1);
			return body1;
		}
	}

	public void equip(Body body) {
		body.setMax();
		body.newType();
		if (body.getKind() == BodyKind.DOLL) {
			equipDoll(body);
			return;
		}
		Body body1 = searchItem(body.getGoalX() + 1, body.getGoalY());
		Body body2 = searchItem(body.getGoalX() + 2, body.getGoalY());
		Body body3 = searchItem(body.getGoalX() + 3, body.getGoalY());
		Body body4 = searchItem(body.getGoalX() + 4, body.getGoalY());
		if (body1 != null)
			body.mergeTypeState(body1.getAttrList());
		if (body2 != null)
			body.mergeTypeState(body2.getAttrList());
		if (body3 != null)
			body.mergeTypeState(body3.getAttrList());
		if (body4 != null)
			body.mergeTypeState(body4.getAttrList());
		equip(body, body2);
		equip(body, body3);
		equip(body, body4);
		getAttack(body);
		restrict(body);
	}

	public void equipDoll(Body body) {
		for (int i = 2; i < 12; i++) {
			Body body1 = searchItem(body.getGoalX() + i, body.getGoalY());
			if (body1 != null) {
				equip(body, body1);
				body.mergeTypeState(body1.getAttrList());
			}
		}

		Body body2 = searchItem(body.getGoalX() + 2, body.getGoalY());
		Body body3 = searchItem(body.getGoalX() + 5, body.getGoalY());
		Body body4 = searchItem(body.getGoalX() + 6, body.getGoalY());
		Body body5 = searchItem(body.getGoalX() + 8, body.getGoalY());
		Body body6 = searchItem(body.getGoalX() + 9, body.getGoalY());
		Body body7 = searchItem(body.getGoalX() + 10, body.getGoalY());
		Body body8 = searchItem(body.getGoalX() + 11, body.getGoalY());
		body.getAtk()[0] = body2 == null ? 0 : body2.getAtk()[0];
		body.getAtk()[2] = body3 == null ? 0 : body3.getAtk()[0];
		body.getAtk()[5] = body4 == null ? 0 : body4.getAtk()[0];
		body.getAtk()[1] = 0;
		if (body5 == null)
			return;
		if (body6 == null)
			return;
		if (body7 == null)
			return;
		if (body8 == null) {
			return;
		} else {
			int j = body5.getImg() % 3 + (body5.getImg() / 75) * 3;
			int k = body6.getImg() % 3 + (body6.getImg() / 75) * 3;
			int l = body7.getImg() % 3 + (body7.getImg() / 75) * 3;
			int i1 = body8.getImg() % 3 + (body8.getImg() / 75) * 3;
			body.getAtk()[1] = (j + k * 3 + l * 9 + i1 * 27)
					% Statics.getAttackDataSize();
			return;
		}
	}

	private void equip(Body body, Body body1) {
		if (body1 == null) {
			return;
		} else {
			body.setStr(body.getStr() + body1.getStr());
			body.setDef(body.getDef() + body1.getDef());
			body.setMst(body.getMst() + body1.getMst());
			body.setMdf(body.getMdf() + body1.getMdf());
			body.setHit(body.getHit() + body1.getHit());
			body.setMis(body.getMis() + body1.getMis());
			return;
		}
	}

	public boolean[] getAttack(Body body) {
		boolean aflag[] = new boolean[6];
		Body body1 = searchItem(body.getGoalX() + 1, body.getGoalY());
		Body body2 = searchItem(body.getGoalX() + 2, body.getGoalY());
		Body body3 = searchItem(body.getGoalX() + 3, body.getGoalY());
		Body body4 = searchItem(body.getGoalX() + 4, body.getGoalY());
		if (body1 != null)
			body.getAtk()[0] = body1.getAtk()[0];
		if (body2 != null) {
			body.getAtk()[0] = body2.getAtk()[0];
			if (body.getAtk()[1] != body2.getAtk()[1])
				body.getAtk()[1] = 0;
		} else {
			AttackData attackdata = Statics.getAttackData(body.getAtk()[0]);
			body.getAtk()[0] = attackdata.getAttackN1();
			body.getAtk()[1] = 0;
		}
		if (body3 != null) {
			if (body.getAtk()[2] != body3.getAtk()[0])
				body.getAtk()[2] = 0;
		} else {
			body.getAtk()[2] = 0;
		}
		if (body4 != null) {
			if (body.getAtk()[5] != body4.getAtk()[0])
				body.getAtk()[5] = 0;
		} else {
			body.getAtk()[5] = 0;
		}
		aflag[1] = getAttack(body, 1, body2, 1, 100);
		aflag[2] = getAttack(body, 2, body3, 0, 100);
		aflag[5] = getAttack(body, 5, body4, 0, 100);
		if (body1 != null && body1.getAtk()[1] != 0 && body1.getAtk()[1] != body.getAtk()[3]
				&& body1.getAtk()[1] != body.getAtk()[4])
			if (body.getAtk()[3] == 0)
				aflag[3] = getAttack(body, 3, body1, 1, 200);
			else if (body.getAtk()[4] == 0) {
				aflag[4] = getAttack(body, 4, body1, 1, 200);
			} else {
				int i = body.getAtk()[4];
				aflag[4] = getAttack(body, 4, body1, 1, 200);
				if (aflag[4])
					body.getAtk()[3] = i;
			}
		return aflag;
	}

	private boolean getAttack(Body body, int i, Body body1, int j, int k) {
		if (body1 == null)
			return false;
		if (body.getAtk()[i] == body1.getAtk()[j])
			return false;
		if (body1.getAtk()[j] != 0 && body1.getExp() >= k) {
			body.getAtk()[i] = body1.getAtk()[j];
			return true;
		} else {
			return false;
		}
	}

	public boolean have(Body body) {
		for (Iterator iterator = Equips.iterator(); iterator.hasNext();) {
			Body body1 = (Body) iterator.next();
			if (body.getImg() == body1.getImg() && body.getName().equals(body1.getName())
					&& !isDust(body1))
				return true;
		}

		return false;
	}

	private boolean isDust(Body body) {
		if (body.getY() != 14)
			return false;
		return body.getX() >= 14;
	}

	private List<Body> Equips;
	private UnitWorks uw;
}
