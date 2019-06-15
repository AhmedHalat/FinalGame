/**
 * Stats given to all characters
 */
public class Stats{
  private int luck;
  private int defense;
  private int damage;
  private int health;
  private int healthLeft;
  private int speed;
  private int exp;
  private int randomizer;

	public Stats(int luck, int defense, int damage, int health, int speed) {
		this.luck = luck;
		this.defense = defense;
		this.damage = damage;
		this.health = health;
		this.healthLeft = health;
		this.speed = speed;
	}

  //getters and setters
	public int getLuck() {
		return luck;}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealthLeft() {
		return healthLeft;
	}

	public void setHealthLeft(int healthLeft) {
		this.healthLeft = healthLeft;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getRandomizer() {
		return randomizer;
	}

	public void setRandomizer(int randomizer) {
		this.randomizer = randomizer;
	}
}
