package dragon2;





import java.util.Vector;

import dragon2.attack.AttackData;
import dragon2.common.Body;

public class Sort {

	public Sort() {
	}

	public static Vector sort(Vector vector) {
		if (vector.size() <= 1)
			return vector;
		Vector vector1 = new Vector();
		Body body;
		for (; vector.size() > 1; vector.remove(body)) {
			body = (Body) vector.firstElement();
			int i = getScore(body);
			for (int j = 1; j < vector.size(); j++) {
				Body body1 = (Body) vector.elementAt(j);
				int k = getScore(body1);
				if (i < k) {
					i = k;
					body = body1;
				}
			}

			vector1.add(body);
		}

		vector1.add(vector.firstElement());
		return vector1;
	}

	private static int getScore(Body body) {
		int i = 0;
		switch (body.getKind()) {
		case WAZA: // '4'
			return -body.getLevel() - body.getImg() * 100;

		case WEPON: // '\002'
			i = 0x2faf080;
			AttackData attackdata = Statics.getAttackData(body.getAtk()[0]);
			i -= attackdata.getAttackN1() * 0xf4240;
			break;

		case ARMOR: // '\003'
			i = 0x2625a00;
			break;

		case ITEM: // '\004'
			i = 0x1c9c380;
			switch (body.getImg()) {
			case 59: // ';'
				i -= 0xf4240;
				break;

			case 58: // ':'
				i -= 0x1e8480;
				break;

			case 60: // '<'
				i -= 0x2dc6c0;
				break;
			}
			break;

		case CLASS: // '\001'
			i = 0x3938700;
			AttackData attackdata1 = Statics.getAttackData(body.getAtk()[0]);
			i -= attackdata1.getAttackN1() * 0xf4240;
			break;

		case DOLL: // '\''
			i = 0x42c1d80;
			break;

		default:
			i = 0x4c4b400;
			break;
		}
		i += body.getLevel() * 10000;
		i += body.getStrMax();
		i += body.getDefMax();
		i += body.getMstMax();
		i += body.getMdfMax();
		i += body.getHitMax();
		i += body.getMisMax();
		return i;
	}
}
