package dragon2;


// Decompiler options: packimports(3)


import java.awt.Image;
import java.util.Arrays;
import java.util.List;

import dragon2.anime.AnimeData;
import dragon2.attack.AttackData;
import dragon2.common.Body;
import dragon2.common.constant.BodyKind;
import dragon2.common.constant.GameColor;
import mine.ImageLoader;
import mine.Mine;
import mine.io.JsonIO;
import mine.io.StringIO;

public class Statics {

	Statics() {
	}

	static void setup() {
        AttackDatas = Arrays.asList(JsonIO.read("data/waza/AttackData.json", AttackData[].class));
        AnimeDatas = Arrays.asList(JsonIO.read("data/anime/AnimeData.json", AnimeData[].class));
            
		idoType = (String[]) StringIO.read("text/itype.txt");
		tokusei = (String[]) StringIO.read("text/tokusei.txt");
		effect = (String[]) StringIO.read("text/effect.txt");
		trtype = (String[]) StringIO.read("text/trtype.txt");
		atype = (String[]) StringIO.read("text/atype.txt");
		back = ImageLoader.getImage("/image/back.gif", false);
		chara = ImageLoader.getImage("/image/chara.gif", true);
		waku = ImageLoader.getImage("/image/waku.gif", true);
		anime = ImageLoader.getImage("/image/anime.gif", true);
		text = ImageLoader.getImage("/image/text.gif", true);
		card = ImageLoader.getImage("/image/card.gif", true);
		status = ImageLoader.getImage("/image/status.gif", true);
		java.awt.image.BufferedImage bufferedimage = ImageLoader.getImage(
				"/image/num.gif", true);
		num = new Image[10];
		for (int i = 0; i < num.length; i++)
			num[i] = Mine.getClipImage(bufferedimage, i * 10, 0, 10, 12, true);

	}

	public static AnimeData getAnimeData(int i) {
		return AnimeDatas.get(i);
	}

	public static AttackData getAttackData(int i) {
		return AttackDatas.get(i);
	}
	
	public static int getAttackDataSize() {
		return AttackDatas.size();
	}

	public static Body getWaza(int i) {
		AttackData attackdata = getAttackData(i);
		Body body = new Body();
		body.setImg(attackdata.getAttackN1() + 169);
		body.setName(attackdata.getName());
		body.setLevel(i);
		body.setHpMax(1);
		body.setColor(GameColor.GREEN);
		body.getAtk()[0] = i;
		body.setKind(BodyKind.WAZA);
		body.setMax();
		body.newType();
		return body;
	}

	public static int getBukiType(int i) {
		switch (i) {
		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
			return 1;

		case 4: // '\004'
		case 5: // '\005'
		case 6: // '\006'
			return 2;
		}
		return 3;
	}

	public static void setHelp(boolean flag) {
		helpFlag = flag;
	}

	public static boolean isHelp() {
		return helpFlag;
	}

	public static void debug() {
		debugFlag = true;
	}

	public static boolean isDebug() {
		return debugFlag;
	}

	public static boolean debugFlag = false;
	public static boolean helpFlag = false;
	public static final int TYPE_MAX = 100;
	private static List<AttackData> AttackDatas;
	private static List<AnimeData> AnimeDatas;
	public static String idoType[];
	public static String tokusei[];
	public static String effect[];
	public static String trtype[];
	public static String atype[];
	public static Image back;
	public static Image chara;
	public static Image waku;
	public static Image anime;
	public static Image card;
	public static Image text;
	public static Image status;
	public static Image num[];

}
