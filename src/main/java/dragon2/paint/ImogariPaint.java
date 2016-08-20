package dragon2.paint;





import imo.ImoDialog;
import javax.swing.JFrame;

import dragon2.ActionBase;
import dragon2.common.Body;
import dragon2.common.constant.Texts;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.util.Equip;

public class ImogariPaint extends ActionBase {

	public ImogariPaint(JFrame jframe, Equip equip1) {
		frame = jframe;
		equip = equip1;
		setHelp();
		action();
	}

	private void setHelp() {
		PaintBase.uw.setHelp(Texts.help[14], 1);
	}

	public void actionMain() {
		Body body = null;
		for (int i = 1; i <= 8; i += 7) {
			for (int j = 1; j <= 10; j += 3) {
				Body body1 = equip.search(i, j);
				if (body1 == null)
					continue;
				body1.newType();
				if (!body1.isType(BodyAttribute.HERO))
					continue;
				body = body1;
				break;
			}

		}

		if (body != null) {
			ImoDialog imodialog = new ImoDialog(frame, body.getName(), body.getLimitTurn());
			imodialog.show();
			int k = imodialog.getEXP();
			if (k != 0) {
				k += 10;
				body.setLimitTurn(body.getLimitTurn() + 1);
				PaintBase.uw.setMPanel(Texts.imogari1);
				PaintBase.uw.setMPanel(Texts.expwo + k + Texts.teniireta);
				PaintBase.uw.startMPanel(body);
				for (int l = 1; l <= 8; l += 7) {
					for (int i1 = 1; i1 <= 13; i1 += 3) {
						Body body2 = equip.search(l, i1);
						if (body2 != null) {
							body2.setExp(body2.getExp() + k);
							equip.levelup(body2);
						}
					}

				}

			} else {
				PaintBase.uw.setMPanel(Texts.imogari2);
				PaintBase.uw.startMPanel(body);
			}
		}
		PaintBase.uw.backFromImogari();
	}

	public void leftPressed() {
	}

	public void rightPressed() {
	}

	Equip equip;
	JFrame frame;
}
