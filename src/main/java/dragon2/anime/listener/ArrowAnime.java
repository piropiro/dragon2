package dragon2.anime.listener;






import java.awt.Component;
import java.awt.Graphics;

import dragon2.UnitWorks;
import dragon2.anime.item.Arrow;
import mine.util.Point;

public class ArrowAnime implements AnimeListener {

	public ArrowAnime(UnitWorks unitworks, Point point, Point point1, int i, int ai[],
			int j) {
		uw = unitworks;
		sleep = j;
		arr = new Arrow(point, point1, i, ai, false);
	}

	public void animation(Component component) {
		for (; !arr.end(); uw.sleep(sleep)) {
			arr.move();
			component.setLocation(arr.getX(), arr.getY());
			component.repaint();
		}

		uw.sleep(sleep);
	}

	public void paint(Graphics g) {
		arr.paint(g);
	}

	UnitWorks uw;
	Arrow arr;
	int sleep;
}
