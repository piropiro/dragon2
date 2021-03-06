package dragon2.common.constant;


public enum MoveType {

	NONE("移動",  new int[]{ 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 }), // 0
	FLY("飛行",   new int[]{ 1, 1, 1, 1, 1, 99, 1, 1, 1, 1, 1 }), // 1
	HEAVY("重歩", new int[]{ 1, 3, 99, 6, 99, 99, 1, 2, 2, 2, 99 }), // 2
	LITE("軽歩",  new int[]{ 1, 1, 99, 3, 99, 99, 1, 2, 2, 2, 99 }), // 3
	SWIM("水中",  new int[]{ 99, 99, 99, 1, 1, 99, 1, 1, 1, 1, 99 }), // 4
	TWIN("水陸",  new int[]{ 2, 6, 99, 1, 1, 99, 1, 1, 1, 1, 99 }), // 5
	HOVER("浮遊", new int[]{ 1, 1, 99, 1, 1, 99, 1, 1, 1, 1, 99 }), // 6
	
	;
	
	private String text;
	
	private int[] steps;
	
	MoveType(String text, int[] steps) {
		this.text = text;
		this.steps = steps;
	}
	
	public String getText() {
		return text;
	}
	
	
	public int[] getSteps() {
		return steps;
	}

	public static MoveType convert(int n) {
		switch (n) {
		case 0: return NONE;
		case 1: return FLY;
		case 2: return HEAVY;
		case 3: return LITE;
		case 4: return SWIM;
		case 5: return TWIN;
		case 6: return HOVER;

		default:
			throw new IllegalArgumentException("MoveType unmatch: " + n);
		}
	}



}
