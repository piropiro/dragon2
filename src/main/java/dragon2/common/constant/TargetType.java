package dragon2.common.constant;

public enum TargetType {
	SINGLE_1("単体１", 0, 1, 0, 0, 0, 0), // 0
	SINGLE_2("単体２", 0, 2, 0, 0, 0, 0), // 1
	SINGLE_3("単体３", 0, 3, 0, 0, 0, 0), // 2
	ALL_1("全体１", 1, 1, 0, 1, 0, 0), // 3
	ALL_2("全体２", 1, 2, 0, 1, 0, 0), // 4
	ALL_3("全体３", 1, 3, 0, 1, 0, 0), // 5
	ALL_4("全体４", 1, 4, 0, 1, 0, 0), // 6
	LONG_2("遠距離２", 1, 2, 1, 0, 0, 0), // 7
	LONG_3("遠距離３", 1, 3, 1, 0, 0, 0), // 8
	RING_2("リング２", 1, 2, 1, 1, 0, 0), // 9
	RING_3("リング３", 1, 3, 1, 1, 0, 0), // 10
	LASER_2("直線２", 3, 2, 0, 2, 2, 0), // 11
	LASER_3("直線３", 3, 3, 0, 2, 3, 0), // 12
	CROSS_2("十字２", 3, 2, 0, 1, 0, 0), // 13
	CROSS_3("十字３", 3, 3, 0, 1, 0, 0), // 14
	BREATH("ブレス", 4, 2, 0, 4, 2, 0), // 15
	HLINE("ライン", 4, 2, 1, 4, 2, 1), // 16
	SQUARE_1("回転", 2, 1, 0, 1, 0, 0), // 17
	SPREAD("拡散", 1, 2, 1, 3, 1, 0), // 18
	;
	
	private String text;
	private int rangeType; 
	private int rangeN1; 
	private int rangeN2;
	private int targetType; 
	private int targetN1; 
	private int targetN2;

		
	TargetType(String text, int rangeType, int rangeN1, int rangeN2, int targetType, int targetN1, int targetN2) {
		this.text = text;
		this.rangeType = rangeType;
		this.rangeN1 = rangeN1;
		this.rangeN2 = rangeN2;
		this.targetType = targetType;
		this.targetN1 = targetN1;
		this.targetN2 = targetN2;
	}
	
	public String getText() {
		return text;
	}

	public int getRangeType() {
		return rangeType;
	}

	public int getRangeN1() {
		return rangeN1;
	}

	public int getRangeN2() {
		return rangeN2;
	}

	public int getTargetType() {
		return targetType;
	}

	public int getTargetN1() {
		return targetN1;
	}

	public int getTargetN2() {
		return targetN2;
	}
	
	public static TargetType convert(int n) {
		switch (n) {
		case 0: return SINGLE_1;
		case 1: return SINGLE_2;
		case 2: return SINGLE_3;
		case 3: return ALL_1;
		case 4: return ALL_2;
		case 5: return ALL_3;
		case 6: return ALL_4;
		case 7: return LONG_2;
		case 8: return LONG_3;
		case 9: return RING_2;
		case 10: return RING_3;
		case 11: return LASER_2;
		case 12: return LASER_3;
		case 13: return CROSS_2;
		case 14: return CROSS_3;
		case 15: return BREATH;
		case 16: return HLINE;
		case 17: return SQUARE_1;
		case 18: return SPREAD;		
		default:
			throw new IllegalArgumentException("TargetType unmatch: " + n);
		}
	}
}
