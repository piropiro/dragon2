package dragon2;





import java.util.Vector;

import dragon2.Sort;
import dragon2.Statics;
import dragon2.common.Body;
import mine.DataStream;
import mine.io.BeanIO;

public class ItemList {

	public ItemList() {
	}

	public static void main(String args[]) {
		Statics.setup();
		Vector<Body> vector = new Vector<>();
		Vector<Body> vector1 = new Vector<>();
		for (int i = 0; i < 30; i++) {
                    Vector<Body> vector2 = (Vector<Body>) BeanIO.read("data/body/E" + i + ".xml");
			if (vector2 != null) {
				for (int j = 0; j < vector2.size(); j++) {
					Body body = (Body) vector2.elementAt(j);
					switch (body.getKind()) {
					case CLASS: // '\001'
					case WEPON: // '\002'
					case ARMOR: // '\003'
					case ITEM: // '\004'
					case DOLL: // '\''
						add(vector, body);
						break;

					default:
						add(vector1, body);
						break;
					}
				}

			}
		}

		Vector vector3 = Sort.sort(vector);
		for (int k = 0; k < vector3.size(); k++) {
			Body body1 = (Body) vector3.elementAt(k);
			body1.setX(k % 20);
			body1.setY(k / 20);
		}

		for (int l = 0; l < vector1.size(); l++) {
			Body body2 = (Body) vector1.elementAt(l);
			body2.setX(l % 20);
			body2.setY(l / 20 + 8);
		}

		DataStream.write("data/E90.txt", vector3);
		DataStream.write("data/E91.txt", vector1);
	}

	private static void add(Vector vector, Body body) {
		for (int i = 0; i < vector.size(); i++) {
			Body body1 = (Body) vector.elementAt(i);
			if (body.getImg() == body1.getImg() && body1.getName().equals(body.getName()))
				return;
		}

		vector.add(body);
	}
}
