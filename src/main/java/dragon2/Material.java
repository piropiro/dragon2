package dragon2;





import dragon2.attack.AttackBase;
import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.BodyKind;
import dragon2.common.constant.GameColor;
import dragon2.common.constant.Texts;

public class Material {

	public Material(UnitWorks unitworks) {
		uw = unitworks;
		count = 0;
		material = null;
	}

	public int getCount() {
		return count;
	}

	public Body getMaterial(AttackBase attackbase) {
		count++;
		if (count % 6 != 0)
			return null;
		Body body = attackbase.getBb();
		if (GameColor.Companion.isPlayer(body))
			return null;
		material = body.copy();
		material.getAtk()[0] = body.getAtk()[3];
		material.setKind(BodyKind.ITEM);
		material.setTypeState(BodyAttribute.M_RED, false);
		material.setTypeState(BodyAttribute.M_GREEN, false);
		material.setTypeState(BodyAttribute.M_BLUE, false);
		for (int i = 1; i < material.getAtk().length; i++)
			material.getAtk()[i] = 0;

		material.setHpMax(Math.max(1, body.getHpMax() / 6));
		material.setStrMax(0);
		material.setDefMax(0);
		material.setMstMax(0);
		material.setMdfMax(0);
		material.setHitMax(0);
		material.setMisMax(0);
		switch (Statics.getBukiType(attackbase.getBuki())) {
		case 1: // '\001'
			material.setImg(59);
			material.setStrMax(body.getStrMax() / 6);
			break;

		case 2: // '\002'
			material.setImg(60);
			material.setHitMax(body.getHitMax() / 6);
			break;

		case 3: // '\003'
			material.setImg(58);
			material.setMstMax(body.getMstMax() / 6);
			break;
		}
		switch (uw.getTurn() % 3) {
		case 0: // '\0'
			material.setDefMax(body.getDefMax() / 6);
			break;

		case 1: // '\001'
			material.setMdfMax(body.getMdfMax() / 6);
			break;

		case 2: // '\002'
			material.setMisMax(body.getMisMax() / 6);
			break;
		}
		if (uw.have(material))
			material = null;
		return material;
	}

	public void message() {
		if (material == null)
			return;
		uw.setMPanel(material.getName() + Texts.ha);
		uw.setMPanel(Texts.material1);
		switch (material.getImg()) {
		case 58: // ':'
			uw.setMPanel(Texts.material2);
			break;

		case 59: // ';'
			uw.setMPanel(Texts.material3);
			break;

		case 60: // '<'
			uw.setMPanel(Texts.material4);
			break;
		}
		uw.setMPanel(Texts.material5);
		uw.startMPanel(material);
		material = null;
	}

	private UnitWorks uw;
	private int count;
	private Body material;
	static final int RED = 59;
	static final int BLUE = 58;
	static final int GREEN = 60;
}
