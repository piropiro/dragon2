package dragon2.attack;

import mine.util.Point;

import dragon2.ActionBase;
import dragon2.Rewalk;
import dragon2.Tutorial;
import dragon2.common.Body;
import dragon2.common.constant.AttackEffect;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.GameColor;
import dragon2.common.constant.Texts;
import dragon2.paint.PaintBase;
import dragon2.paint.TalkPaint;

public class FightManager extends ActionBase {

	public FightManager(Body body) {
		ba = body;
		n = -1;
		setHelp(false);
	}

	private void setHelp(boolean flag) {
		if (!GameColor.Companion.isPlayer(ba))
			return;
		
		if (flag)
			PaintBase.uw.setHelp(Texts.help[12], 2);
		else
			PaintBase.uw.setHelp(Texts.help[13], 1);
	}

	public void nextSelect() {
		while (++n < ba.getAtk().length)
			if (select(n, false)) {
				PaintBase.map.setPaintListener(this);
				return;
			}
		TalkPaint talkpaint = new TalkPaint(ba);
		if (talkpaint.isEnable()) {
			PaintBase.map.setPaintListener(talkpaint);
			talkpaint.show();
			PaintBase.map.repaint();
		} else {
			PaintBase.map.setEndPaint(ba);
			PaintBase.map.repaint();
		}
		PaintBase.uw.closeTPanel();
	}

	public boolean select(int i, boolean flag) {
		if (ba.getAtk()[i] == 0)
			return false;
		if (i > 0 && ba.isType(BodyAttribute.CLOSE))
			return false;
		attack = new AttackBase(ba, ba.getAtk()[i]);
		if (attack.isAlive(flag)) {
			attack.show();
			PaintBase.uw.selectPanel(true);
			PaintBase.uw.setTPanel(attack, ba);
			PaintBase.map.repaint();
			return true;
		} else {
			return false;
		}
	}

	public boolean enemySelect() {
		int i = -1;
		int j = -1;
		for (int k = 0; k < ba.getAtk().length; k++) {
			if (k > 0 && ba.isType(BodyAttribute.CLOSE))
				break;
			AttackBase attackbase = new AttackBase(ba, ba.getAtk()[k]);
			if (attackbase.isAlive(true) && j < attackbase.getBestDamage()) {
				j = attackbase.getBestDamage();
				i = k;
			}
		}

		if (i != -1)
			return select(i, true);
		else
			return false;
	}

	private void setCounter(Body body) {
		bb = body;
		counter = null;
		if (!attack.isEffect(AttackEffect.COUNTER_ABLE))
			return;
		for (int i = body.getAtk().length - 1; i >= 0; i--) {
			if (body.getAtk()[i] == 0)
				continue;
			AttackBase attackbase = new AttackBase(body, body.getAtk()[i]);
			if (!attackbase.isCounterable(ba, true))
				continue;
			counter = attackbase;
			break;
		}

		for (int j = 0; j < body.getAtk().length; j++) {
			if (counter != null)
				break;
			if (body.getAtk()[j] == 0)
				continue;
			AttackBase attackbase1 = new AttackBase(body, body.getAtk()[j]);
			if (!attackbase1.isCounterable(ba, false))
				continue;
			counter = attackbase1;
			break;
		}

		if (counter != null) {
			PaintBase.uw.selectPanel(false);
			counter.selectTarget(ba);
			PaintBase.uw.selectPanel(true);
		}
	}

	public void mouseMoved(Point point) {
		setTarget(point);
	}

	public void setTarget(Point point) {
		Point point1 = PaintBase.map.getWaku();
		PaintBase.map.wakuMove(point);
		if (PaintBase.V.G(1, 0, point1.x, point1.y) == 0) {
			if (PaintBase.V.G(1, 0, point.x, point.y) == 0) {
				PaintBase.map.wakuPaint(true);
			} else {
				PaintBase.V.clear(4, 0, 0);
				PaintBase.map.wakuPaint(false);
				attack.setTarget(point.x, point.y);
				PaintBase.map.repaint();
			}
		} else if (PaintBase.V.G(1, 0, point.x, point.y) == 0) {
			PaintBase.V.clear(4, 0, 0);
			PaintBase.map.wakuPaint(false);
			PaintBase.map.repaint();
		} else {
			PaintBase.V.clear(4, 0, 0);
			PaintBase.map.wakuPaint(false);
			attack.setTarget(point.x, point.y);
			PaintBase.map.repaint();
		}
		bb = PaintBase.uw.search(point.x, point.y);
		attack.selectTarget(bb);
		if (PaintBase.uw.isFirst())
			Tutorial.setHelp(ba, bb, n, PaintBase.uw);
		else
			setHelp(bb != null);
		if (bb != null && bb != ba) {
			if (!attack.isTarget(bb))
				return;
			setCounter(bb);
			PaintBase.uw.setAPanel(attack, counter);
		} else {
			PaintBase.uw.closeAPanel();
			attack.selectTarget(null);
		}
	}

	public void leftPressed() {
		Point point = PaintBase.map.getWaku();
		if (point.x == ba.getX() && point.y == ba.getY()) {
			nextSelect();
			return;
		}
		if (PaintBase.V.G(1, 0, point.x, point.y) != 0
				&& attack.searchTargets()) {
			action();
		}
	}

	public void rightPressed() {
		Rewalk.rewalk(ba);
	}

	public void setSelectBody(Body body) {
	}

	public boolean isNextPoint(Point point) {
		if (PaintBase.V.G(1, 0, point.x, point.y) == 3) {
			Body body = PaintBase.uw.search(point.x, point.y);
			return body != null;
		} else {
			return false;
		}
	}

	public void enemy() {
		bb = attack.getBestTarget();
		attack.setTarget(bb.getX(), bb.getY());
		auto();
	}

	public void remote(int i, int j, int k) {
		select(i, false);
		bb = PaintBase.uw.search(j, k);
		attack.setTarget(j, k);
		auto();
	}

	private void auto() {
		attack.searchTargets();
		attack.selectTarget(bb);
		PaintBase.uw.setTPanel(attack, ba);
		PaintBase.V.clear(4, 0, 0);
		PaintBase.map.repaint();
		setCounter(bb);
		if (bb != null)
			PaintBase.uw.setAPanel(attack, counter);
		PaintBase.uw.sleep(300L);
		attack();
		PaintBase.map.repaint();
	}

	private void counter() {
		if (counter == null)
			return;
		if (bb == null)
			return;
		if (!bb.isAlive())
			return;
		if (bb.isType(BodyAttribute.ANTI_SLEEP)) {
			return;
		} else {
			PaintBase.uw.setTPanel(counter, bb);
			PaintBase.uw.selectPanel(false);
			counter.attack();
			PaintBase.uw.selectPanel(true);
			return;
		}
	}

	public void attack() {
		attack.attack();
		counter();
		PaintBase.uw.closeHPanel();
		PaintBase.uw.closeNPanel();
		PaintBase.uw.closeTPanel();
		PaintBase.uw.closeAPanel();
		PaintBase.uw.levelup(ba);
		PaintBase.uw.levelup(bb);
		PaintBase.uw.message();
		PaintBase.V.clear(1, 0, 0);
		PaintBase.V.clear(4, 0, 0);
	}

	public void actionMain() {
		attack();
		PaintBase.uw.setEnd(ba, false);
	}

	private Body ba;
	private Body bb;
	private AttackBase attack;
	private AttackBase counter;
	private int n;
}
