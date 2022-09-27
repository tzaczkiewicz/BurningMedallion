package burningEngine;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InspectPanel extends JPanel implements ActionListener{
	
	public static final int WIDTH=400;
	public static final int HEIGHT=460;//20 pixel border
	
	private Unit unit;
	private int xLocation;
	private int yLocation;
	private MapScene mapScene;
	
	private JButton exit;
	
	private JPanel content;
	
	private JLabel portrait; //An image for the unit
	private JLabel level;
	private JLabel exp;
	private JLabel name;
	private JLabel rank;
	
	private JLabel primary;//label for important, combat related stats
	private JLabel HP; //hitpoints (current and max. It's a string, after all.
	private JLabel move; //max move
	private JLabel might; //attack power
	private JLabel hit; //base hit percentage
	private JLabel evasion; //base evasion
	private JLabel crit; //base crit percentage
	private JLabel deflect; //crit avoidance percentage
	private JLabel physArmor; //def+modifiers(if any are put into game)
	private JLabel magArmor; //res+modifiers
	
	private JLabel weapon; //label for equipped weapon, goes to right of portrait
	private JLabel wepName;
	private JLabel durability; //current/max durability. Resets every mission, unusable at 0?
	private JLabel power; //weapon power
	private JLabel accuracy; //weapon accuracy
	private JLabel type; //physical/magical
	private JLabel range;
	
	private JLabel secondary;//label for full vital list
	private JLabel strength;
	private JLabel skill;
	private JLabel speed;
	private JLabel defense;
	private JLabel luck;
	private JLabel magic;
	private JLabel resistance;
	
	
	public InspectPanel(MapScene g){
		this.setLayout(null);
		this.setBackground(Color.BLUE);
		content=new JPanel();
		content.setSize(WIDTH, HEIGHT);
		content.setLayout(null);
		content.setLocation(20, 20);
		this.add(content);
		mapScene=g;
		portrait=new JLabel();
		level=new JLabel();
		exp=new JLabel();
		name=new JLabel();
		rank=new JLabel();
		
		exit=new JButton("Close");
		exit.setSize(80, 18);
		
		primary=new JLabel("Combat Abilities");
		HP=new JLabel();
		move=new JLabel();
		might=new JLabel();
		hit=new JLabel();
		evasion=new JLabel();
		crit=new JLabel();
		deflect=new JLabel();
		physArmor=new JLabel();
		magArmor=new JLabel();
		
		weapon=new JLabel("Equipped Weapon");
		wepName=new JLabel();
		durability=new JLabel();
		power=new JLabel();
		accuracy=new JLabel();
		type=new JLabel();
		range=new JLabel();
		
		secondary=new JLabel("Full Stats");
		strength=new JLabel();
		skill=new JLabel();
		speed=new JLabel();
		defense=new JLabel();
		luck=new JLabel();
		magic=new JLabel();
		resistance=new JLabel();
		
		this.setSize(WIDTH+40, HEIGHT+40);
		exit.setLocation(WIDTH+20-80, 1);
		exit.addActionListener(this);
		this.add(exit);
		
		level.setSize(200, 20);
		level.setLocation(0, 160);
		content.add(level);
		exp.setSize(200, 20);
		exp.setLocation(0, 180);
		content.add(exp);
		name.setSize(200, 20);
		name.setLocation(0, 200);
		content.add(name);
		rank.setSize(200, 20);
		rank.setLocation(0, 220);
		content.add(rank);
		primary.setSize(200, 20);
		primary.setLocation(0, 260);
		content.add(primary);
		HP.setSize(200, 20);
		HP.setLocation(0, 280);
		content.add(HP);
		move.setSize(200, 20);
		move.setLocation(0, 300);
		content.add(move);
		might.setSize(200, 20);
		might.setLocation(0, 320);
		content.add(might);
		hit.setSize(200, 20);
		hit.setLocation(0, 340);
		content.add(hit);
		evasion.setSize(200, 20);
		evasion.setLocation(0, 360);
		content.add(evasion);
		crit.setSize(200, 20);
		crit.setLocation(0, 380);
		content.add(crit);
		deflect.setSize(200, 20);
		deflect.setLocation(0, 400);
		content.add(deflect);
		physArmor.setSize(200, 20);
		physArmor.setLocation(0, 420);
		content.add(physArmor);
		magArmor.setSize(200, 20);
		magArmor.setLocation(0, 440);
		content.add(magArmor);
		
		wepName.setSize(200, 20);
		wepName.setLocation(200, 0);
		content.add(wepName);
		durability.setSize(200, 20);
		durability.setLocation(200, 20);
		content.add(durability);
		power.setSize(200, 20);
		power.setLocation(200, 40);
		content.add(power);
		accuracy.setSize(200, 20);
		accuracy.setLocation(200, 60);
		content.add(accuracy);
		type.setSize(200, 20);
		type.setLocation(200, 80);
		content.add(type);
		range.setSize(200, 20);
		range.setLocation(200, 100);
		content.add(range);
		
		secondary.setSize(200, 20);
		secondary.setLocation(200, 140);
		content.add(secondary);
		strength.setSize(200, 20);
		strength.setLocation(200, 160);
		content.add(strength);
		magic.setSize(200, 20);
		magic.setLocation(200, 180);
		content.add(magic);
		skill.setSize(200, 20);
		skill.setLocation(200, 200);
		content.add(skill);
		speed.setSize(200, 20);
		speed.setLocation(200, 220);
		content.add(speed);
		defense.setSize(200, 20);
		defense.setLocation(200, 240);
		content.add(defense);
		resistance.setSize(200, 20);
		resistance.setLocation(200, 260);
		content.add(resistance);
		luck.setSize(200, 20);
		luck.setLocation(200, 280);
		content.add(luck);
	}
	
	public void open(Unit u){
		unit=u;
		
		//unit.getPortrait()? portrait.setIcon(icon);
		level.setText("Level " + String.valueOf(unit.getLevel()));
		exp.setText("Exp: " + unit.getExp() + "/100");
		name.setText(unit.getName());
		rank.setText(unit.getRank().getClassName());
		
		//primary.setText("Combat Abilities");
		HP.setText("HP: "+unit.getHitpoints()+"/"+unit.getMaxHP());
		move.setText("Move: "+unit.getMaxMove());
		might.setText("Might: "+unit.getMight());
		hit.setText("Hit %: "+unit.getAccuracy());
		evasion.setText("Evade %: "+unit.getEvasion());
		crit.setText("Crit %: "+unit.getCrit());
		deflect.setText("Deflect %: "+unit.getDeflect());
		physArmor.setText("Armor: "+unit.getDefense());
		magArmor.setText("Ward: "+unit.getResistance());
		
		//weapon.setText("Equipped Weapon");
		wepName.setText(unit.getWeapon().name);
		durability.setText("Durability:  "+unit.getWeapon().getDurability()+"/"+unit.getWeapon().maxDurability);
		power.setText("Power: "+unit.getWeapon().power+"");
		accuracy.setText("Accuracy: "+unit.getWeapon().accuracy+"");
		type.setText("Type: "+unit.getWeapon().getType());
		range.setText("Range: "+unit.getWeapon().getRangeString());
		
		//secondary.setText("Full Stats");
		strength.setText("Strength: "+unit.getStrength());
		skill.setText("Skill: "+unit.getSkill());
		speed.setText("Speed: "+unit.getSpeed());
		defense.setText("Defense: "+unit.getDefense());
		luck.setText("Luck: "+unit.getLuck());
		magic.setText("Magic: "+unit.getMagic());
		resistance.setText("Resistance: "+unit.getResistance());
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(exit)){
			mapScene.closeInspect();
		}
		
	}
}
