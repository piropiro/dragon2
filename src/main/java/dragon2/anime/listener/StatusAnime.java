package dragon2.anime.listener;



import java.awt.Component;
import java.awt.Graphics;

import dragon2.Statics;
import dragon2.UnitWorks;
import dragon2.anime.item.Number;
import mine.Mine;
import mine.UnitMap;
import mine.util.Point;

public class StatusAnime implements AnimeListener {

	public StatusAnime(UnitWorks unitworks, UnitMap unitmap, Point point, int i) {
		uw = unitworks;
		V = unitmap;
		bb = point;
		img = i;
		n = new Number(30, 1, 0, 0);
	}

	public void animation(Component component) {
		component.setSize(32, 48);
		component.setLocation(bb.x * 32, bb.y * 32 - 16);
		V.S(5, 0, bb.x, bb.y, 0);
		for (; !n.end(); uw.sleep(25L)) {
			n.move();
			component.repaint();
		}

		V.S(5, 0, bb.x, bb.y, img);
		uw.sleep(100L);
	}

	public void paint(Graphics g) {
		int i = n.getX();
		int j = n.getY() - 4;
		int k = (img % 15) * 32 + 1;
		int l = (img / 15) * 32 + 1;
		Mine.drawImage(Statics.status, i, j, k, l, 30, 30, g);
	}

	private UnitWorks uw;
	private UnitMap V;
	private Point bb;
	private int img;
	Number n;
	static final int SLEEP = 1;
	static final int POISON = 2;
	static final int CLOSE = 3;
	static final int OFFENCE = 4;
	static final int DEFENCE = 5;
	static final int CHARM = 6;
	static final int HEAL = 7;
	static final int FLY = 8;
	static final int WALK = 9;
	static final int OIL = 10;
	static final int HAMMER = 11;
	static final int BERSEK = 12;
}
