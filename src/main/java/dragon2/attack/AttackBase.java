package dragon2.attack;

import java.awt.Dimension;
import mine.util.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dragon2.Iconable;
import dragon2.Rewalk;
import dragon2.Statics;
import dragon2.UnitWorks;
import dragon2.Walk;
import dragon2.common.Body;
import dragon2.common.constant.AttackEffect;
import dragon2.common.constant.MoveType;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.GameColor;
import dragon2.common.util.Luck;
import dragon2.map.Map;
import mine.Mine;
import mine.UnitMap;

public class AttackBase implements Iconable {

	public AttackBase(Body body, int i) {
		ba = body;
		ad = Statics.getAttackData(i);
		bb = null;
		Targets = null;
		setECT();
		
		setRange(ad.getTargetType().getRangeType(), ad.getTargetType().getRangeN1(), ad.getTargetType().getRangeN2());
		getAttackUnit(ad.getTargetType().getTargetType(), ad.getTargetType().getTargetN1());
	}

	public void show() {
		V.copy(2, 1, 1, 0);
		V.S(3, 0, ba.getX(), ba.getY(), 0);
	}

	public static void setup(UnitWorks unitworks, UnitMap unitmap, Map map1,
			List<Body> vector) {
		uw = unitworks;
		V = unitmap;
		map = map1;
		Charas = vector;
	}





	private void setECT() {
		ECT = new HashSet<>(ad.getEffect());
	}

	private boolean countFuel() {
		switch (ad.getEnergyType()) {
		case STR:
			return ad.getEnergyCost() <= ba.getStr();
		case DEF:
			return ad.getEnergyCost() <= ba.getDef();
		case MST:
			return ad.getEnergyCost() <= ba.getMst();
		case MDF:
			return ad.getEnergyCost() <= ba.getMdf();
		case HIT:
			return ad.getEnergyCost() <= ba.getHit();
		case MIS:
			return ad.getEnergyCost() <= ba.getMis();
		default:
			return true;
		}
	}

	public boolean isEffect(AttackEffect effect) {
		return ECT.contains(effect);
	}

	public int getBuki() {
		return ad.getAttackN1();
	}

	public boolean isCounterable(Body body, boolean flag) {
		if (ba.isType(BodyAttribute.ANTI_SLEEP))
			return false;
		if (flag && !isEffect(AttackEffect.COUNTER_ONLY))
			return false;
		if (!flag && !isEffect(AttackEffect.COUNTER_ABLE))
			return false;
		if (!countFuel())
			return false;
		return V.G(2, 1, body.getX(), body.getY()) != 0;
	}

	public boolean isAlive(boolean flag) {
		if (isEffect(AttackEffect.COUNTER_ONLY))
			return false;
		if (isEffect(AttackEffect.TAME) && Rewalk.isWalked(ba))
			return false;
		if (!countFuel())
			return false;
		if (!flag) {
			Dimension dimension = V.getSize();
			for (int i = 0; i < dimension.width; i++) {
				for (int j = 0; j < dimension.height; j++)
					if (V.G(2, 1, i, j) != 0 && V.G(1, 1, i, j) != 0)
						return true;

			}

		} else if (bbs != null)
			return true;
		return false;
	}

	private void getAttackUnit(int i, int j) {
		V.clear(1, 1, 0);
		dmax = -1;
		for (Body body : Charas) {
			if (isTarget(body)) {
				setSearchData(i, j, body);
				if (V.G(2, 1, body.getX(), body.getY()) != 0) {
					V.S(2, 1, body.getX(), body.getY(), 3);
					int k = getDamage(body) * getRate(body);
					if (!body.isType(BodyAttribute.S_LOCK)) {
						if (isPossible(body, AttackEffect.CHARM))
							k += body.getHp() / 2;
						if (isPossible(body, AttackEffect.SLEEP))
							k += body.getHp() / 2;
					}
					if (isPossible(body, AttackEffect.DEATH))
						k += body.getHp();
					if (ad.getTargetType().getTargetType() == 1)
						k *= 2;
					if (body.getColor() == ba.getColor())
						k = -k;
					if (ba.isType(BodyAttribute.CHARM))
						k = -k;
					if (dmax < k) {
						dmax = k;
						bbs = body;
					}
				}
			}
		}

	}

	private void setSearchData(int i, int j, Body body) {
		switch (i) {
		case 3: // '\003'
			V.fillDia(1, 1, body.getX(), body.getY(), j, 1);
			break;

		default:
			V.S(1, 1, body.getX(), body.getY(), 1);
			break;
		}
	}

	private void decrease() {
		switch (ad.getEnergyType()) {
		case STR:
			ba.setStr(ba.getStr() - ad.getEnergyCost());
			break;
		case DEF:
			ba.setDef(ba.getDef() - ad.getEnergyCost());
			break;
		case MST:
			ba.setMst(ba.getMst() - ad.getEnergyCost());
			break;
		case MDF:
			ba.setMdf(ba.getMdf() - ad.getEnergyCost());
			break;
		case HIT:
			ba.setHit(ba.getHit() - ad.getEnergyCost());
			break;
		case MIS:
			ba.setMis(ba.getMis() - ad.getEnergyCost());
			break;
		default:
		}
	}

	private int getFace(int i, int j) {
		int k = i - ba.getX();
		int l = j - ba.getY();
		if (Math.abs(k) > Math.abs(l))
			return k >= 0 ? 1 : 0;
		return l >= 0 ? 3 : 2;
	}

	public void setTarget(int i, int j) {
		target = new Point(i, j);
		int k = ad.getTargetType().getTargetType();
		int l = ad.getTargetType().getTargetN1();
		int i1 = ad.getTargetType().getTargetN2();
		switch (k) {
		default:
			break;

		case 0: // '\0'
			V.S(4, 0, i, j, 3);
			break;

		case 1: // '\001'
			V.change(1, 0, 0, 4, 0, 0, 3);
			break;

		case 2: // '\002'
			face = getFace(i, j);
			for (int j1 = i1 + 1; j1 <= l; j1++)
				switch (face) {
				case 0: // '\0'
					V.S(4, 0, ba.getX() - j1, ba.getY(), 3);
					break;

				case 1: // '\001'
					V.S(4, 0, ba.getX() + j1, ba.getY(), 3);
					break;

				case 2: // '\002'
					V.S(4, 0, ba.getX(), ba.getY() - j1, 3);
					break;

				case 3: // '\003'
					V.S(4, 0, ba.getX(), ba.getY() + j1, 3);
					break;
				}

			break;

		case 3: // '\003'
			V.fillDia(4, 0, i, j, l, 3);
			break;

		case 4: // '\004'
			face = getFace(i, j);
			for (int k1 = i1 + 1; k1 <= l; k1++) {
				for (int l1 = -k1 + 1; l1 <= k1 - 1; l1++)
					switch (face) {
					case 0: // '\0'
						V.S(4, 0, ba.getX() - k1, ba.getY() + l1, 3);
						break;

					case 1: // '\001'
						V.S(4, 0, ba.getX() + k1, ba.getY() + l1, 3);
						break;

					case 2: // '\002'
						V.S(4, 0, ba.getX() + l1, ba.getY() - k1, 3);
						break;

					case 3: // '\003'
						V.S(4, 0, ba.getX() + l1, ba.getY() + k1, 3);
						break;
					}

			}

			break;
		}
		V.S(4, 0, ba.getX(), ba.getY(), 0);
		V.copy(4, 0, 4, 1);
	}

	private void setRange(int i, int j, int k) {
		V.clear(2, 1, 0);
		switch (i) {
		default:
			break;

		case 0: // '\0'
			V.fillDia(2, 1, ba.getX(), ba.getY(), j, 2);
			V.S(2, 1, ba.getX(), ba.getY(), 0);
			break;

		case 1: // '\001'
			V.fillDia(2, 1, ba.getX(), ba.getY(), j, 2);
			V.fillDia(2, 1, ba.getX(), ba.getY(), k, 0);
			break;

		case 2: // '\002'
			V.fillRect(2, 1, ba.getX() - j, ba.getY() - j, j * 2 + 1, j * 2 + 1, 2);
			V.S(2, 1, ba.getX(), ba.getY(), 0);
			break;

		case 3: // '\003'
			for (int l = k + 1; l <= j; l++) {
				V.S(2, 1, ba.getX() - l, ba.getY(), 2);
				V.S(2, 1, ba.getX() + l, ba.getY(), 2);
				V.S(2, 1, ba.getX(), ba.getY() - l, 2);
				V.S(2, 1, ba.getX(), ba.getY() + l, 2);
			}

			break;

		case 4: // '\004'
			for (int i1 = k + 1; i1 <= j; i1++) {
				for (int j1 = -i1 + 1; j1 <= i1 - 1; j1++) {
					V.S(2, 1, ba.getX() - i1, ba.getY() + j1, 2);
					V.S(2, 1, ba.getX() + i1, ba.getY() + j1, 2);
					V.S(2, 1, ba.getX() + j1, ba.getY() - i1, 2);
					V.S(2, 1, ba.getX() + j1, ba.getY() + i1, 2);
				}

			}

			break;
		}
	}

	private int getDamage(Body body) {
		if (body == null)
			return 0;
		int i = Walk.getTikei(body);
		int j = 0;
		int k = 0;
		int l = 0;
		switch (ad.getDamageType()) {
		case SWORD: // '\001'
			k = ba.getStr();
			l = body.getDef();
			break;

		case MAGIC: // '\002'
			k = ba.getMst();
			l = body.getMdf();
			break;

//		case 3: // '\003'
//			k = ba.hp / 2;
//			break;
//
//		case 4: // '\004'
//			k = ad.energyCost / 2;
//			break;

		case SWORD_ALL: // '\005'
			k = ba.getStr();
			break;

		case MAGIC_ALL: // '\006'
			k = ba.getMst();
			break;
		default:
		}
		if (ba.isType(BodyAttribute.ATTACK_UP))
			k += (ba.getStr() + ba.getMst()) / 4;
		if (body.isType(BodyAttribute.GUARD_UP))
			k -= (body.getDef() + body.getMdf()) / 4;
		if (isEffect(AttackEffect.ICE) && body.isType(BodyAttribute.ANTI_SLEEP))
			k = (int) ((double) k * 1.25D);
		if (isEffect(AttackEffect.THUNDER) && body.isType(BodyAttribute.CHARM))
			k = (int) ((double) k * 1.25D);
		if (isEffect(AttackEffect.ICE) && body.isType(BodyAttribute.CLOSE))
			k = (int) ((double) k * 1.25D);
		if (isEffect(AttackEffect.THUNDER) && body.isType(BodyAttribute.CLOSE))
			k = (int) ((double) k * 1.25D);
		if (isEffect(AttackEffect.FIRE) && body.isType(BodyAttribute.OIL))
			k = (int) ((double) k * 1.5D);
		if (isEffect(AttackEffect.THUNDER) && i == 3)
			k = (int) ((double) k * 1.5D);
		if (isEffect(AttackEffect.ICE) && i == 5)
			k = (int) ((double) k * 1.5D);
		if (ba.isType(BodyAttribute.DRAGON_KILLER) && body.isType(BodyAttribute.DRAGON))
			k = (int) ((double) k * 1.5D);
		if (ba.isType(BodyAttribute.UNDEAD_KILLER) && body.isType(BodyAttribute.UNDEAD))
			k = (int) ((double) k * 1.5D);
		j = Math.max(0, k - l);
		if (isEffect(AttackEffect.REGENE) && !body.isType(BodyAttribute.UNDEAD))
			j *= -1;
		return j;
	}

	private int getRate(Body body) {
		if (body == null)
			return 0;
		int i = Walk.getTikei(body);
		int j = 100;
		if (isEffect(AttackEffect.DAMAGE_200))
			j += 100;
		if (isEffect(AttackEffect.DAMAGE_300))
			j += 200;
		if (isEffect(AttackEffect.DAMAGE_150))
			j += 50;
		if (isEffect(AttackEffect.FIRE) && body.isType(BodyAttribute.FIRE_200))
			j += 100;
		if (isEffect(AttackEffect.ICE) && body.isType(BodyAttribute.ICE_200))
			j += 100;
		if (isEffect(AttackEffect.THUNDER) && body.isType(BodyAttribute.THUNDER_200))
			j += 100;
		if (isEffect(AttackEffect.SORA_200) && i == 1)
			j += 100;
		if (isEffect(AttackEffect.MIZU_200) && i == 3)
			j += 100;
		if (isEffect(AttackEffect.RIKU_150) && i == 2)
			j += 50;
		if (isEffect(AttackEffect.DRAGON_200) && body.isType(BodyAttribute.DRAGON))
			j += 100;
		if (isEffect(AttackEffect.UNDEAD_200) && body.isType(BodyAttribute.UNDEAD))
			j += 100;
		if (body.isType(BodyAttribute.ANTI_SLEEP))
			j += 150;
		if (isEffect(AttackEffect.FIRE) && body.isType(BodyAttribute.CLOSE))
			j /= 2;
		if (isEffect(AttackEffect.FIRE) && body.isType(BodyAttribute.FIRE_50))
			j /= 2;
		if (isEffect(AttackEffect.ICE) && body.isType(BodyAttribute.ICE_50))
			j /= 2;
		if (isEffect(AttackEffect.THUNDER) && body.isType(BodyAttribute.THUNDER_50))
			j /= 2;
		if (isEffect(AttackEffect.FIRE) && i == 3)
			j /= 2;
		if (isEffect(AttackEffect.FIRE) && i == 5)
			j /= 2;
		if (Statics.getBukiType(ad.getAttackN1()) == 1 && body.isType(BodyAttribute.SWORD_50))
			j /= 2;
		if ((body.getMoveType() == MoveType.SWIM || body.getMoveType() == MoveType.TWIN) && !isEffect(AttackEffect.MIZU_100)) {
			if (i == 3 && !isEffect(AttackEffect.THUNDER))
				j /= 2;
			if (i == 4 && !isEffect(AttackEffect.FIRE))
				j /= 2;
		}
		if (body.getMoveType() == MoveType.SWIM || body.getMoveType() == MoveType.TWIN) {
			if (Walk.getTikei(ba) == 1)
				j /= 2;
			if (Walk.getTikei(ba) == 2)
				j /= 2;
		}
		if (isEffect(AttackEffect.RIKU_0) && i != 2)
			j = 0;
		if (isEffect(AttackEffect.MIZU_0) && i != 3)
			j = 0;
		if (isEffect(AttackEffect.FIRE) && body.isType(BodyAttribute.FIRE_0))
			j = 0;
		if (isEffect(AttackEffect.ICE) && body.isType(BodyAttribute.ICE_0))
			j = 0;
		if (isEffect(AttackEffect.THUNDER) && body.isType(BodyAttribute.THUNDER_0))
			j = 0;
		return j;
	}

	private int getMeichu(Body body, boolean flag) {
		if (body == null)
			return 0;
		if (ba.getHit() == 0)
			return 0;
		if (isEffect(AttackEffect.HICHU))
			return 16;
		int i = 32 - (body.getMis() * 32) / ba.getHit();
		if (flag)
			i += Luck.rnd(1, ba);
		if (isEffect(AttackEffect.HIT_12))
			i += 12;
		if (isEffect(AttackEffect.HIT_4))
			i += 4;
		if (isEffect(AttackEffect.MISS_4))
			i -= 4;
		if (body.isType(BodyAttribute.ANTI_SLEEP))
			i = Math.max(16 - body.getStore(), i);
		if (body.isType(BodyAttribute.RIKU))
			i = Math.max(16 - body.getStore(), i);
		if (isEffect(AttackEffect.COMBO))
			i = Math.max(2, i);
		else
			i = Mine.mid(2, i, 32 - body.getStore() - 1);
		if (ba.isType(BodyAttribute.ATTACK_UP))
			i += 2;
		if (body.isType(BodyAttribute.GUARD_UP))
			i -= 2;
		return i;
	}

	public void selectTarget(Body body) {
		bb = body;
		if (body == null) {
			uw.closeHPanel();
		} else {
			if (Targets == null) {
				Targets = new ArrayList<>();
				Targets.add(body);
			}
			meichu = getMeichu(body, false);
			uw.setHPanel(body, ba, (getDamage(body) * getRate(body)) / 100,
					isHit());
		}
	}

	public boolean isTarget(Body body) {
		if (body == ba)
			return false;
		if (!body.isAlive())
			return false;
		if (isEffect(AttackEffect.NO_ATTACK)) {
			if (isEffect(AttackEffect.REGENE) && body.getColor() != ba.getColor())
				return false;
			if (!isEffect(AttackEffect.REGENE) && body.getColor() == ba.getColor())
				return false;
			if (isPossible(body, AttackEffect.CRITICAL))
				return true;
			if (isPossible(body, AttackEffect.DEATH))
				return true;
			if (isPossible(body, AttackEffect.SLEEP))
				return true;
			if (isPossible(body, AttackEffect.CHARM))
				return true;
			if (isPossible(body, AttackEffect.POISON))
				return true;
			if (isPossible(body, AttackEffect.WET))
				return true;
			if (isPossible(body, AttackEffect.ATTACK_UP))
				return true;
			if (isPossible(body, AttackEffect.GUARD_UP))
				return true;
			if (isPossible(body, AttackEffect.UPPER))
				return true;
			if (isPossible(body, AttackEffect.CHOP))
				return true;
			if (isPossible(body, AttackEffect.REFRESH))
				return true;
			return isPossible(body, AttackEffect.OIL);
		}
		if (body.isType(BodyAttribute.CHARM))
			return true;
		int i = getDamage(body);
		if (i == 0)
			if (isEffect(AttackEffect.REGENE))
				return body.getColor() == ba.getColor();
			else
				return body.getColor() != ba.getColor();
		if (i < 0 && body.getHp() >= body.getHpMax())
			return false;
		boolean flag = true;
		if (body.getColor() == ba.getColor())
			flag = !flag;
		if (ba.isType(BodyAttribute.CHARM))
			flag = !flag;
		if (i < 0)
			flag = !flag;
		return flag;
	}

	public boolean searchTargets() {
		if (isEffect(AttackEffect.COUNTER_ABLE) && bb == null)
			return false;
		Targets = new ArrayList<>();
		for (Body body : Charas) {
			if (isTarget(body) && V.G(4, 0, body.getX(), body.getY()) != 0)
				Targets.add(body);
		}

		return Targets.size() != 0;
	}

	public String getName() {
		return ad.getName();
	}

	public String getSubName() {
		return ad.getLabel();
	}

	public GameColor getColor() {
		return ad.getLabelColor();
	}

	public int getStore() {
		return bb.getStore();
	}

	public int getDamage() {
		return getDamage(bb);
	}

	public int getMeichu() {
		return meichu;
	}

	public int getRate() {
		return getRate(bb);
	}

	public Body getBody(boolean flag) {
		if (flag)
			return ba;
		else
			return bb;
	}

	private void anime(boolean flag) {
		Point point = new Point(ba.getX(), ba.getY());
		Point point1;
		if (bb != null)
			point1 = new Point(bb.getX(), bb.getY());
		else
			point1 = target;
		switch (ad.getAnimeType()) {
		default:
			break;

		case ALL: // '\002'
			if (!flag)
				uw.setAnime(2, ad.getAnimeN1(), point, point1);
			break;
		case SOME_ARROW: // '\005'
			if (!flag)
				uw.setAnime(5, ad.getAnimeN1(), point, point1);
			break;
		case ROTATE: // '\006'
			if (!flag)
				uw.setAnime(6, ad.getAnimeN1(), point, point1);
			break;

		case SINGLE: // '\001'
			if (flag)
				uw.setAnime(1, ad.getAnimeN1(), point, point1);
			break;
		case SINGLE_ARROW: // '\003'
			if (flag)
				uw.setAnime(3, ad.getAnimeN1(), point, point1);
			break;

		case LASER_ARROW_2: // '\004'
		case LASER_ARROW_3:
			if (flag)
				break;
			if (ad.getTargetType().getTargetType() == 2)
				switch (face) {
				case 0: // '\0'
					point1.x = Math.max(0, point.x - ad.getTargetType().getTargetN1());
					break;

				case 1: // '\001'
					point1.x = Math.min(19, point.x + ad.getTargetType().getTargetN1());
					break;

				case 2: // '\002'
					point1.y = Math.max(0, point.y - ad.getTargetType().getTargetN1());
					break;

				case 3: // '\003'
					point1.y = Math.min(14, point.y + ad.getTargetType().getTargetN1());
					break;
				}
			uw.setAnime(4, ad.getAnimeN1(), point, point1);
			break;
		}
	}

	public boolean isHit() {
		if (isEffect(AttackEffect.HICHU))
			return true;
		if (bb.isType(BodyAttribute.ANTI_SLEEP))
			return true;
		if (bb.isType(BodyAttribute.RIKU))
			return true;
		int i = getMeichu(bb, false);
		return i + bb.getStore() > 16;
	}

	public Body getBestTarget() {
		return bbs;
	}

	public int getBestDamage() {
		return dmax;
	}

	public void attack() {
		anime(false);
		if (bb != null) {
			Targets.remove(bb);
			Targets.add(0, bb);
		} else {
			bb = Targets.get(0);
			uw.setAPanel(this, null);
		}
		int i = 0;
		for (Body b : Targets) {
			bb = b;
			meichu = getMeichu(bb, true);
			uw.setHPanel(bb, ba, (getDamage(bb) * getRate(bb)) / 100, isHit());
			if (i > 0)
				uw.setAPanel(this, null);
			if (V.G(3, 0, bb.getX(), bb.getY()) == 2)
				V.S(3, 0, bb.getX(), bb.getY(), 0);
			if (!attackMiss() && !attackFinish() && !attackDeath()
					&& !attackMain()) {
				attackSleep();
				attackCharm();
				attackPoison();
				attackHeal();
				attackClose();
				attackOil();
				attackOffence();
				attackDefence();
				attackUpper();
				attackDowner();
				attackDance();
				uw.sleep(300L);
			}
			i++;
		}

		decrease();
	}

	public boolean isPossible(AttackEffect i) {
		return isPossible(bb, i);
	}

	private boolean isPossible(Body body, AttackEffect i) {
		if (!isEffect(i))
			return false;
		if (body.isType(BodyAttribute.ANTI_ALL))
			return false;
		switch (i) {
		case CRITICAL: // '\016'
		case DEATH: // '\017'
			if (body.isType(BodyAttribute.NKILL))
				return false;
			if (ba.getLevel() < body.getLevel())
				return false;
			if (body.isType(BodyAttribute.GUARD_UP))
				return false;
			break;

		case SLEEP: // '\020'
		case CHARM: // '\023'
			if (body.getMdf() * 2 >= ba.getMst())
				return false;
			if (body.isType(BodyAttribute.GUARD_UP))
				return false;
			break;
		default:
		}
		switch (i) {
		case NO_ATTACK: // '\026'
		case HIT_4: // '\030'
		case MISS_4: // '\031'
		case CHOP: // '\033'
		case HEAL: // '\034'
		case RIKU_0: // '\035'
		case MIZU_0: // '\036'
		case DAMAGE_150: // '\037'
		case UNDEAD_200: // ' '
		default:
			break;

		case CRITICAL: // '\016'
			if (body.isType(BodyAttribute.ANTI_CRITICAL))
				return false;
			if (!body.isType(BodyAttribute.POISON) && body.getDef() >= ba.getStr())
				return false;
			break;

		case DEATH: // '\017'
			if (body.isType(BodyAttribute.UNDEAD))
				return false;
			if (body.isType(BodyAttribute.ANTI_DEATH))
				return false;
			if (body.isType(BodyAttribute.POISON))
				break;
			if (body.getMdf() >= ba.getMst())
				return false;
			if (body.getHp() > (body.getHpMax() * 3) / 4)
				return false;
			break;

		case SLEEP: // '\020'
			if (body.isType(BodyAttribute.SLEEP))
				return false;
			if (body.isType(BodyAttribute.ANTI_SLEEP))
				return false;
			break;

		case CHARM: // '\023'
			if (body.isType(BodyAttribute.ANTI_CHARM))
				return false;
			if (body.isType(BodyAttribute.CHARM))
				return false;
			break;

		case WET: // '\022'
			if (body.isType(BodyAttribute.CLOSE))
				return false;
			break;

		case OIL: // '!'
			if (body.isType(BodyAttribute.OIL))
				return false;
			break;

		case POISON: // '\021'
			if (body.isType(BodyAttribute.ANTI_POISON))
				return false;
			if (body.isType(BodyAttribute.POISON))
				return false;
			break;

		case ATTACK_UP: // '\024'
			if (body.isType(BodyAttribute.ATTACK_UP))
				return false;
			break;

		case GUARD_UP: // '\025'
			if (body.isType(BodyAttribute.GUARD_UP))
				return false;
			break;

		case UPPER: // '\032'
			if (Walk.getTikei(body) == 1)
				return false;
			break;

		case REFRESH: // '\027'
			if (V.G(3, 0, body.getX(), body.getY()) == 0)
				return false;
			break;
		}
		return true;
	}

	private boolean attackKill(int i, int j) {
		uw.setAPanel();
		uw.hpdamage(bb.getHpMax());
		Point point = new Point(bb.getX(), bb.getY());
		uw.setAnime(1, i, point, point);
		uw.setAnime(-3, 0, point, point);
		uw.setAnime(-6, j, point, point);
		uw.setAPanel();
		uw.sleep(200L);
		uw.hphenka();
		bb.setHp(0);
		uw.dead(this);
		uw.closeNPanel();
		return true;
	}

	private boolean attackFinish() {
		if (!isPossible(AttackEffect.CRITICAL))
			return false;
		if (Luck.rnd(1, ba) != 1)
			return false;
		else
			return attackKill(-6, 3);
	}

	private boolean attackDeath() {
		if (!isPossible(AttackEffect.DEATH))
			return false;
		else
			return attackKill(-7, 5);
	}

	private void attackStatus(int i, BodyAttribute j) {
		if (bb.isType(BodyAttribute.S_LOCK)) {
			bb.setTypeState(BodyAttribute.S_LOCK, false);
			return;
		} else {
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(-7, i, point, point);
			bb.setTypeState(j, true);
			bb.setTypeState(BodyAttribute.S_LOCK, false);
			bb.setTypeState(BodyAttribute.S_WAIT, true);
			return;
		}
	}

	private void attackSleep() {
		if (!isPossible(AttackEffect.SLEEP)) {
			return;
		} else {
			attackStatus(1, BodyAttribute.ANTI_SLEEP);
			return;
		}
	}

	private void attackCharm() {
		if (!isPossible(AttackEffect.CHARM)) {
			return;
		} else {
			attackStatus(6, BodyAttribute.CHARM);
			return;
		}
	}

	private void attackClose() {
		if (!isPossible(AttackEffect.WET)) {
			return;
		} else {
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(-7, 3, point, point);
			bb.setTypeState(BodyAttribute.CLOSE, true);
			bb.setTypeState(BodyAttribute.S_WAIT, true);
			return;
		}
	}

	private void attackOil() {
		if (!isPossible(AttackEffect.OIL)) {
			return;
		} else {
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(-7, 10, point, point);
			bb.setTypeState(BodyAttribute.OIL, true);
			bb.setTypeState(BodyAttribute.S_WAIT, true);
			return;
		}
	}

	private void attackMode(BodyAttribute i, BodyAttribute j, int k) {
		Point point = new Point(bb.getX(), bb.getY());
		uw.setAnime(-7, k, point, point);
		if (bb.isType(j)) {
			bb.setTypeState(j, false);
			V.S(5, 0, bb.getX(), bb.getY(), 0);
		} else {
			bb.setTypeState(i, true);
		}
	}

	private void attackPoison() {
		if (!isPossible(AttackEffect.POISON)) {
			return;
		} else {
			attackMode(BodyAttribute.POISON, BodyAttribute.HEAL, 2);
			return;
		}
	}

	private void attackHeal() {
		if (!isPossible(AttackEffect.HEAL)) {
			return;
		} else {
			attackMode(BodyAttribute.HEAL, BodyAttribute.POISON, 7);
			return;
		}
	}

	private void attackOffence() {
		if (!isPossible(AttackEffect.ATTACK_UP)) {
			return;
		} else {
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(-7, 4, point, point);
			bb.setTypeState(BodyAttribute.ATTACK_UP, true);
			bb.setTypeState(BodyAttribute.S_WAIT, true);
			return;
		}
	}

	private void attackDefence() {
		if (!isPossible(AttackEffect.GUARD_UP)) {
			return;
		} else {
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(-7, 5, point, point);
			bb.setTypeState(BodyAttribute.GUARD_UP, true);
			bb.setTypeState(BodyAttribute.S_WAIT, true);
			return;
		}
	}

	private void attackUpper() {
		if (!isPossible(AttackEffect.UPPER)) {
			return;
		} else {
			attackMode(BodyAttribute.SORA, BodyAttribute.RIKU, 8);
			return;
		}
	}

	private void attackDowner() {
		if (!isPossible(AttackEffect.CHOP)) {
			return;
		} else {
			attackMode(BodyAttribute.RIKU, BodyAttribute.SORA, 9);
			return;
		}
	}

	private void attackDance() {
		if (!isPossible(AttackEffect.REFRESH)) {
			return;
		} else {
			V.S(3, 0, bb.getX(), bb.getY(), 0);
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(1, -1, point, point);
			return;
		}
	}

	private boolean attackMain() {
		if (isEffect(AttackEffect.NO_ATTACK)) {
			bb.setStore(bb.getStore() + meichu);
			bb.setStore(bb.getStore() % 16);
			meichu = 0;
			uw.setAPanel();
			anime(true);
			return false;
		}
		int i = 0;
		while (meichu > 0) {
			bb.setStore(bb.getStore() + 1);
			meichu--;
			if (bb.getStore() >= 16) {
				uw.setAPanel();
				anime(true);
				int j = (getDamage(bb) * getRate(bb)) / 100;
				if (j >= 0)
					i += Math.max(1, j + Luck.rnd(1, ba));
				else
					i += Math.min(-1, j - Luck.rnd(1, ba));
				uw.hpdamage(i);
				uw.setNPanel(bb, i);
				bb.setStore(bb.getStore() - 16);
			}
		}
		uw.setAPanel();
		uw.sleep(200L);
		uw.hphenka();
		bb.setHp(Mine.mid(0, bb.getHp() - i, bb.getHpMax()));
		if (bb.getHp() <= 0) {
			uw.dead(this);
			uw.closeNPanel();
			uw.sleep(300L);
			return true;
		} else {
			return false;
		}
	}

	private boolean attackMiss() {
		if (meichu + bb.getStore() >= 16) {
			return false;
		} else {
			bb.setStore(bb.getStore() + meichu);
			meichu = 0;
			uw.setAPanel();
			anime(true);
			uw.hpdamage(0);
			Point point = new Point(bb.getX(), bb.getY());
			uw.setAnime(-5, 0, point, point);
			uw.sleep(500L);
			return true;
		}
	}

	public Body getBa() {
		return ba;
	}

	public Body getBb() {
		return bb;
	}
	
	static UnitWorks uw;
	static UnitMap V;
	static Map map;
	static List<Body> Charas;
	private List<Body> Targets;
	Body ba;


	Body bb;
	Body bbs;
	Point target;
	int dmax;
	AttackData ad;
	static final int N_STR = 1;
	static final int N_MST = 2;
	static final int N_HPH = 3;
	static final int N_FUELH = 4;
	static final int N_STRA = 5;
	static final int N_MSTA = 6;
	Set<AttackEffect> ECT;
	static final int HIT = 16;
	static final int DHIT = 32;
	int meichu;
	int face;
}
