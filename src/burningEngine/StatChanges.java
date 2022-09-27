package burningEngine;

public class StatChanges {
	private int level;
	private int exp;
	private int maxMove;
	private int maxHP;
	private int strength;
	private int defense;
	private int speed;
	private int skill;
	private int luck;
	private int magic;
	private int resistance;
	
	public StatChanges(){
		setLevel(0);
		setExp(0);
		setMaxMove(0);
		setMaxHP(0);
		setStrength(0);
		setDefense(0);
		setSpeed(0);
		setSkill(0);
		setLuck(0);
		setMagic(0);
		setResistance(0);
	}
	
	public void reset(){
		setLevel(0);
		setExp(0);
		setMaxMove(0);
		setMaxHP(0);
		setStrength(0);
		setDefense(0);
		setSpeed(0);
		setSkill(0);
		setLuck(0);
		setMagic(0);
		setResistance(0);
	}
	
	public void addLevel(){
		level++;
	}
	public void addExp(){
		exp++;
	}
	public void addMaxMove(){
		maxMove++;
	}
	public void addMaxHP(){
		maxHP++;
	}
	public void addStrength(){
		strength++;
	}
	public void addDefense(){
		defense++;
	}
	public void addSpeed(){
		speed++;
	}
	public void addSkill(){
		skill++;
	}
	public void addLuck(){
		luck++;
	}
	public void addMagic(){
		magic++;
	}
	public void addResistance(){
		resistance++;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public int getMaxMove() {
		return maxMove;
	}

	public void setMaxMove(int maxMove) {
		this.maxMove = maxMove;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
	
	
}
