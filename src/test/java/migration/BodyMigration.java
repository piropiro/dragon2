package migration;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dragon2.OldBody;
import dragon2.OldBody2;
import dragon2.OldBody3;
import dragon2.OldBody4;
import dragon2.OldBody5;
import dragon2.attack.AttackData;
import dragon2.common.Body;
import dragon2.common.constant.BodyAttribute;
import dragon2.common.constant.BodyKind;
import dragon2.common.constant.GameColor;
import dragon2.common.constant.MoveType;
import dragon3.bean.BodyData;
import dragon3.bean.DeployData;
import dragon3.bean.DeployType;
import dragon3.common.constant.ArmorType;
import dragon3.common.constant.WeponType;
import mine.DataStream;
import mine.io.BeanIO;
import mine.io.JsonIO;
import mine.io.MatrixIO;
import net.arnx.jsonic.JSON;

/**
 *
 * @author rara
 */
public class BodyMigration {

    public BodyMigration() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void migrate_001() throws Exception {
        List<String> bodys = new ArrayList<>();
//        bodys.add("E90");
//        bodys.add("E91");
//        bodys.add("E99");
//        for (int i = 0; i < 27; i++) {
//            bodys.add("E" + i);
//        }
        bodys.add("E27");

        DataStream.setup(this);
        for (String body : bodys) {

            Vector data = (Vector) DataStream.read("data/" + body + ".txt");
            BeanIO.write("target/body/" + body + ".xml", data);
        }

    }

    @Test
    public void migrate_002() throws Exception {
        DataStream.setup(this);
        Vector vector1 = (Vector) DataStream.read("data/E" + 1 + ".txt");
        Vector vector2 = (Vector) BeanIO.read("data/body/E" + 1 + ".xml");

        for (int i = 0; i < vector1.size(); i++) {
            Body body1 = (Body) vector1.get(i);
            Body body2 = (Body) vector2.get(i);

            assertThat(body2.toString(), CoreMatchers.is(body1.toString()));
        }

    }
    
    
    
    @Test
    public void migrate_003() throws Exception {
        List<String> bodys = getFileNames();

      for (String body : bodys) {
          
          Vector vector1 = (Vector) BeanIO.read("data/body/" + body + ".xml");

          String json = JSON.encode(vector1, true);
          
          FileUtils.write(new File("target/body/" + body + ".json"), json, "UTF-8");

      }
    }
    
    @Test
    public void migrate_004() throws Exception {
        List<String> bodys = getFileNames();
        
      for (String body : bodys) {
    	  
    	  OldBody2[] oldBodys = JsonIO.read("data/body/" + body + ".json", OldBody2[].class);
          
          List<Body> newBodys = new ArrayList<>();
    	  for (OldBody2 old : oldBodys) {
    		  Body newBody = new Body();
    		  newBody.setName(old.name);
    		  newBody.setX(old.x);
    		  newBody.setY(old.y);
    		  newBody.setLevel(old.level);
    		  newBody.setExp(old.exp);
    		  newBody.setHp(old.hp);
    		  newBody.setHpMax(old.hpMax);
    		  newBody.setStr(old.str);
    		  newBody.setStrMax(old.strMax);
    		  newBody.setDef(old.def);
    		  newBody.setDefMax(old.defMax);
    		  newBody.setMst(old.mst);
    		  newBody.setMstMax(old.mstMax);
    		  newBody.setMdf(old.mdf);
    		  newBody.setMdfMax(old.mdfMax);
    		  newBody.setHit(old.hit);
    		  newBody.setHitMax(old.hitMax);
    		  newBody.setMis(old.mis);
    		  newBody.setMisMax(old.misMax);
    		  newBody.setMoveStep(old.ido);
//    		  newBody.color = old.color;
    		  newBody.setImg(old.img);
    		  newBody.setRange(old.maai);
    		  newBody.setScope(old.scope);
    		  newBody.setLimitTurn(old.moveturn);
    		  newBody.setGoalX(old.gx);
    		  newBody.setGoalY(old.gy);
    		  newBody.setStore(old.store);
    		  newBody.setAtk(old.atk);
    		  newBody.setAttrList(old.type);
    		  newBody.setKind(old.kind);
    		  newBody.setMoveType(MoveType.convert(old.itype));

    		  
    		  
    		  newBodys.add(newBody);
    	  }
 
          
          String json = JSON.encode(newBodys, true);
          
          FileUtils.write(new File("target/body/" + body + ".json"), json, "UTF-8");

      }
    }
    
    @Test
    public void migrate_005() throws Exception {
        List<String> bodys = getFileNames();

      for (String body : bodys) {
    	  
    	  OldBody[] oldBodys = JsonIO.read("data/body/" + body + ".json", OldBody[].class);
          
          List<Body> newBodys = new ArrayList<>();
    	  for (OldBody old : oldBodys) {
    		  Body newBody = new Body();
    		  newBody.setName(old.name);
    		  newBody.setX(old.x);
    		  newBody.setY(old.y);
    		  newBody.setLevel(old.level);
    		  newBody.setExp(old.exp);
    		  newBody.setHp(old.hp);
    		  newBody.setHpMax(old.hpMax);
    		  newBody.setStr(old.str);
    		  newBody.setStrMax(old.strMax);
    		  newBody.setDef(old.def);
    		  newBody.setDefMax(old.defMax);
    		  newBody.setMst(old.mst);
    		  newBody.setMstMax(old.mstMax);
    		  newBody.setMdf(old.mdf);
    		  newBody.setMdfMax(old.mdfMax);
    		  newBody.setHit(old.hit);
    		  newBody.setHitMax(old.hitMax);
    		  newBody.setMis(old.mis);
    		  newBody.setMisMax(old.misMax);
    		  newBody.setMoveStep(old.ido);
//    		  newBody.moveType = old.itype;
//    		  newBody.color = old.color;
    		  newBody.setImg(old.img);
    		  newBody.setRange(old.maai);
    		  newBody.setScope(old.scope);
    		  newBody.setLimitTurn(old.moveturn);
    		  newBody.setGoalX(old.gx);
    		  newBody.setGoalY(old.gy);
    		  newBody.setStore(old.store);
    		  newBody.setAtk(old.atk);

    		  for (int i : old.type) {
    			  switch (i) {
    			  case 0:
    				  if (newBody.getKind() == null) {
    					  newBody.setKind(BodyKind.CHARA);
    				  }
    				  break;
    			  case 1:
    				  newBody.setKind(BodyKind.CLASS);
    				  break;
    			  case 2:
    				  newBody.setKind(BodyKind.WEPON);
    				  break;
    			  case 3:
    				  newBody.setKind(BodyKind.ARMOR);
    				  break;
    			  case 4:
    				  newBody.setKind(BodyKind.ITEM);
    				  break;
    			  case 39:
    				  newBody.setKind(BodyKind.DOLL);
    				  break;
    			  case 52:
    				  newBody.setKind(BodyKind.WAZA);
    				  break;
    			  default:
    				  newBody.getAttrList().add(BodyAttribute.convert(i));
    			  }
    		  }
    		  
    		  newBodys.add(newBody);
    	  }
 
          
          String json = JSON.encode(newBodys, true);
          
          FileUtils.write(new File("target/body/" + body + ".json"), json, "UTF-8");

      }
    }
    
    @Test
    public void migrate_006() throws Exception {
        List<String> bodys = getFileNames();

      for (String body : bodys) {
    	  
    	  OldBody3[] oldBodys = JsonIO.read("data/body/" + body + ".json", OldBody3[].class);
          
          List<Body> newBodys = new ArrayList<>();
    	  for (OldBody3 old : oldBodys) {
    		  Body newBody = new Body();
    		  newBody.setName(old.name);
    		  newBody.setX(old.x);
    		  newBody.setY(old.y);
    		  newBody.setLevel(old.level);
    		  newBody.setExp(old.exp);
    		  newBody.setHp(old.hp);
    		  newBody.setHpMax(old.hpMax);
    		  newBody.setStr(old.str);
    		  newBody.setStrMax(old.strMax);
    		  newBody.setDef(old.def);
    		  newBody.setDefMax(old.defMax);
    		  newBody.setMst(old.mst);
    		  newBody.setMstMax(old.mstMax);
    		  newBody.setMdf(old.mdf);
    		  newBody.setMdfMax(old.mdfMax);
    		  newBody.setHit(old.hit);
    		  newBody.setHitMax(old.hitMax);
    		  newBody.setMis(old.mis);
    		  newBody.setMisMax(old.misMax);
    		  newBody.setMoveStep(old.moveStep);
    		  newBody.setMoveType(old.moveType);
//    		  newBody.color = old.color;
    		  newBody.setImg(old.img);
    		  newBody.setRange(old.maai);
    		  newBody.setScope(old.scope);
    		  newBody.setLimitTurn(old.moveturn);
    		  newBody.setGoalX(old.gx);
    		  newBody.setGoalY(old.gy);
    		  newBody.setStore(old.store);
    		  newBody.setAtk(old.atk);
    		  newBody.setAttrList(old.type);
    		  newBody.setKind(old.kind);
    		  
    		  newBody.setColor(GameColor.Companion.convert(old.color));
    		  
    		  newBodys.add(newBody);
    	  }
 
          
          String json = JSON.encode(newBodys, true);
          
          FileUtils.write(new File("target/body/" + body + ".json"), json, "UTF-8");

      }
    }
    
    @Test
    public void migrate_007() throws Exception {
        List<String> bodys = getFileNames();

      for (String body : bodys) {
    	  
    	  OldBody4[] oldBodys = JsonIO.read("data/body/" + body + ".json", OldBody4[].class);
          
          List<Body> newBodys = new ArrayList<>();
    	  for (OldBody4 old : oldBodys) {
    		  Body newBody = new Body();
    		  BeanUtils.copyProperties(newBody, old);
    		  newBody.setAttrList(old.type);
    		  newBody.setRange(old.maai);
    		  newBody.setGoalX(old.gx);
    		  newBody.setGoalY(old.gy);
    		  
    		  newBodys.add(newBody);
    	  }
 
          
          String json = JSON.encode(newBodys, true);
          
          FileUtils.write(new File("target/body/" + body + ".json"), json, "UTF-8");

      }
    }
    
    @Test
    public void migrate_008() throws Exception {
        List<String> bodys = getFileNames();

      for (String body : bodys) {
    	  
    	  OldBody5[] oldBodys = JsonIO.read("data/body/" + body + ".json", OldBody5[].class);
          
          List<Body> newBodys = new ArrayList<>();
    	  for (OldBody5 old : oldBodys) {
    		  Body newBody = new Body();
    		  BeanUtils.copyProperties(newBody, old);
    		  newBody.setLimitTurn(old.moveturn);
    		  
    		  newBodys.add(newBody);
    	  }
 
          
          String json = JSON.encode(newBodys, true);
          
          FileUtils.write(new File("target/body/" + body + ".json"), json, "UTF-8");

      }
    }
    
    @Test
    public void calc_test() throws Exception {
    	int n = 17;
    	int level = 2;
    	
    	double rate = Math.pow(1.1, 10 - level);
    	int m = (int)(n * rate + 0.5);
    	
    	double rate2 = Math.pow(1.1, level - 10);
    	int m2 = (int)(m * rate2 + 0.5);
    	
    	System.out.println("m=" + m);
    	System.out.println("m2=" + m2);
    }
	@Test
	public void migrate_009() throws Exception {
		AttackData[] attacks = JsonIO.read("data/waza/AttackData.json", AttackData[].class);


		List<String> files = getFileNames();
        files.remove("E90");
        files.remove("E91");
        
		Map<String, BodyData> bodyMap = new HashMap<>();
		Map<BodyKind, List<BodyData>> newBodyMap = new HashMap<>();
		for (BodyKind kind : BodyKind.values()) {
			newBodyMap.put(kind,  new ArrayList<>());
		}
		
		for (String file : files) {

			Body[] oldBodys = JsonIO.read("data/body/" + file + ".json", Body[].class);
			int[][] map = MatrixIO.read("data/map/" + file.replace("E", "D") + ".txt");

			List<DeployData> newDeploys = new ArrayList<>();
			for (Body oldBody : oldBodys) {

				DeployData deploy = new DeployData();
				BeanUtils.copyProperties(deploy, oldBody);

				String oldKey = oldBody.getKind().name() + oldBody.getName();
				BodyData newBody = bodyMap.get(oldKey);
				

				if (newBody == null) {
					newBody = new BodyData();
					BeanUtils.copyProperties(newBody, oldBody);
					
					double rate = Math.pow(1.1, 10 - oldBody.getLevel());
					newBody.setHp((int)(oldBody.getHpMax() * rate + 0.5));
					newBody.setStr((int)(oldBody.getStr() * rate + 0.5));
					newBody.setDef((int)(oldBody.getDef() * rate + 0.5));
					newBody.setMst((int)(oldBody.getMst() * rate + 0.5));
					newBody.setMdf((int)(oldBody.getMdf() * rate + 0.5));
					newBody.setHit((int)(oldBody.getHit() * rate + 0.5));
					newBody.setMis((int)(oldBody.getMis() * rate + 0.5));

					// WeponType
					switch (newBody.getKind()) {
					case CLASS:
						switch (oldBody.getImg()) {
						case 154:
							newBody.setWeponType(WeponType.SWORD);
							newBody.setImage("class_sword.png");
							break;
						case 155:
							newBody.setWeponType(WeponType.AX);
							newBody.setImage("class_ax.png");
							break;
						case 156:
							newBody.setWeponType(WeponType.BODY);
							newBody.setImage("class_body.png");
							break;
						case 157:
							newBody.setWeponType(WeponType.MAGIC);
							newBody.setImage("class_magic.png");
							break;
						case 158:
							newBody.setWeponType(WeponType.SPEAR);
							newBody.setImage("class_spear.png");
							break;
						case 159:
							newBody.setWeponType(WeponType.BOW);
							newBody.setImage("class_bow.png");
							break;
						case 160:
							newBody.setWeponType(WeponType.KNIFE);
							newBody.setImage("class_knife.png");
							break;
						default:
						}
						break;
					case WEPON:
					case ARMOR:
					case CHARA:
					case DOLL:
						newBody.setImage(String.format("chara_%03d.png", oldBody.getImg()));
						for (int n : oldBody.getAtk()) {
							if (n == 0) continue;
							newBody.setWeponType(convertAttackN1(attacks[n].getAttackN1()));
							break;
						}
						break;
					default:
						newBody.setImage(String.format("chara_%03d.png", oldBody.getImg()));
					}
					
					// WazaList
					List<String> wazaList = new ArrayList<>();
					for (int i = 0; i < oldBody.getAtk().length; i++) {
						if (oldBody.getAtk()[i] == 0) {
							wazaList.add("none");
						} else {
							wazaList.add(String.format("waza_%03d", oldBody.getAtk()[i]));
						}
					}
					newBody.setWazaList(wazaList);
					
					// AttrList
					List<BodyAttribute> attrList = newBody.getAttrList();
					for (int i = 0; i < attrList.size(); i++) {
						switch(attrList.get(i)) {
						case ASK: 
							attrList.set(i, BodyAttribute.TALKABLE); 
							break;
						case HEAL: 
							attrList.set(i, BodyAttribute.REGENE);
							break;
						case NKILL: 
							attrList.set(i, BodyAttribute.ANTI_DEATH);
							break;
						default:
						}
					}
					attrList.remove(BodyAttribute.SUMMON);
					
					if (newBody.getKind() == BodyKind.DOLL) {
						newBody.setKind(BodyKind.CHARA);
					}
					
					// ArmorType
					switch (newBody.getKind()) {
					case ARMOR:
					case CHARA:
						newBody.setArmorType(getArmorType(oldBody));
					default:
					}
					

					
					List<BodyData> newBodys = newBodyMap.get(newBody.getKind());
					
					newBody.setId(String.format("%s_%03d", newBody.getKind().name().toLowerCase(), newBodys.size() + 1));
					newBodys.add(newBody);
					bodyMap.put(oldKey, newBody);
				}
				deploy.setBodyId(newBody.getId());
				deploy.setDeployType(getDeployType(oldBody, map));
				
				newDeploys.add(deploy);
			}

			String deployJson = JSON.encode(newDeploys, true);
			FileUtils.write(new File("target/deploy/deploy_" + file.replace("E", "D") + ".json"), deployJson, "UTF-8");

		}

		for (BodyKind kind : BodyKind.values()) {
			List<BodyData> newBodys = newBodyMap.get(kind);
			
			String json = JSON.encode(newBodys, true);

			String capKind = kind.name().substring(0, 1).toUpperCase() + kind.name().substring(1).toLowerCase(); 
			FileUtils.write(new File("target/body/" + capKind + "Data.json"), json, "UTF-8");
		}
		

	}
	
		private WeponType convertAttackN1(int n) {
			switch (n) {
			case 0: return WeponType.NONE;
			case 1: return WeponType.SWORD;
			case 2: return WeponType.AX;
			case 3: return WeponType.BODY;
			case 4: return WeponType.KNIFE;
			case 5: return WeponType.SPEAR;
			case 6: return WeponType.BOW;
			case 7: return WeponType.MAGIC;
			case 8: return WeponType.BREATH;
			default:
				throw new IllegalArgumentException("AttackN1 unmatch:" + n);
			}
		}
			
	private ArmorType getArmorType(Body b) {
		switch (b.getMoveType()) {
		case HEAVY: return ArmorType.HEAVY;
		default: return ArmorType.LITE;
		}
	}
    
	private DeployType getDeployType(Body b, int[][] map) {
		switch (b.getKind()) {
		case ARMOR:
		case CLASS:
		case DOLL:
		case ITEM:
		case WEPON:
			if (b.getGoalX() == 0 && b.getGoalY() == 0) {
				if (map[b.getY()][b.getX()] == 17) {
					return DeployType.CLEAR_ITEM;
				} else {
					return DeployType.BOX_ITEM;
				}
			} else {
				if (b.getGoalX() == b.getX() && b.getGoalY() == b.getY()) {
					return DeployType.SECRET_ITEM;
				} else {
					return DeployType.ENEMY_ITEM;
				}
			}
		case CHARA:
			switch (b.getColor()) {
			case BLUE: return DeployType.PLAYER_CHARA;
			case RED: 
				if (b.getAttrList().contains(BodyAttribute.SUMMON)) {
					return DeployType.SUMMON;
				} else {
					return DeployType.ENEMY_CHARA;
				}
			case GREEN: return DeployType.NEUTRAL_CHARA;
			default:
				throw new IllegalArgumentException("DeployType undecided:" + b);
			}
		
		default:
			throw new IllegalArgumentException("DeployType undecided:" + b);
		}
	}
	
    private List<String> getFileNames() {
        List<String> bodys = new ArrayList<>();
        bodys.add("E90");
        bodys.add("E91");
        bodys.add("init");
        bodys.add("kakusei");
        for (int i = 1; i <= 27; i++) {
            bodys.add(String.format("E%02d", i));
        }
        
        return bodys;
    }
}
