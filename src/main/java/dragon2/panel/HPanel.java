package dragon2.panel;





import java.awt.*;

import dragon2.UnitWorks;
import dragon2.common.Body;
import mine.JCanvas;
import mine.Mine;

public class HPanel extends JCanvas {

	public HPanel(UnitWorks unitworks, boolean flag) {
		super(96, 14);
		setFont(Mine.getFont(14));
		uw = unitworks;
		high = flag;
		hpb = new HPBar();
	}

	public void setLocate(Body body, Body body1) {
		int i = body.getX();
		int j = body.getY();
		int k;
		if (body.getX() >= body1.getX())
			k = i * 32 + 32;
		else
			k = i * 32 - getSize().width;
		if (i < 3) {
			k = i * 32 + 32;
			if (body.getY() == body1.getY() && Math.abs(body.getX() - body1.getX()) < 5)
				k = Math.max(body.getX(), body1.getX()) * 32 + 32;
		}
		if (i > 16) {
			k = i * 32 - getSize().width;
			if (body.getY() == body1.getY() && Math.abs(body.getX() - body1.getX()) < 5)
				k = Math.min(body.getX(), body1.getX()) * 32 - getSize().width;
		}
		int l;
		if (high)
			l = j * 32 + 1;
		else
			l = j * 32 + 17;
		setLocation(k, l);
	}

	public void display(Body body, Body body1, int i, boolean flag) {
		if (body == null) {
			dispose();
			return;
		}
		ba = body;
		bb = body1;
		hpb.setup(flag, body.getHp(), body.getHpMax());
		hpb.setMin(body.getHp() - i, false);
		switch (body.getColor()) {
		case BLUE: // '\001'
			setBackground(new Color(0, 0, 150));
			break;

		case RED: // '\003'
			setBackground(new Color(150, 0, 0));
			break;

		case GREEN: // '\002'
			setBackground(new Color(0, 100, 0));
			break;
		}
		setLocate(body, body1);
		setVisible(true);
		repaint();
	}

	public void dispose() {
		setVisible(false);
	}

	public void paint(Graphics g) {
		if (ba == null) {
			return;
		} else {
			Dimension dimension = getSize();
			g.setColor(getBackground());
			g.fillRect(0, 0, dimension.width, dimension.height);
			g.setColor(Color.black);
			g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
			hpb.paint(2, 12, g);
			return;
		}
	}

	public void damage(int i) {
		hpb.setMin(ba.getHp() - i, true);
		repaint();
	}

	public void henka() {
		int i = hpb.getSleepTime();
		for (; hpb.henka(); uw.sleep(i))
			repaint();

		repaint();
	}

	UnitWorks uw;
	Body ba;
	Body bb;
	boolean high;
	HPBar hpb;
}
