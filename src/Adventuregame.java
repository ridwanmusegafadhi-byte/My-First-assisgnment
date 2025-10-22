import java.util.Scanner;
import java.util.Random;

abstract class Character {
    protected String name;
    protected int hp;
    protected int maxHp;

    public Character(String name, int maxHp) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public abstract void attack(Character target);

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }
}
class Weapon {
    private String name;
    private int damage;

    public Weapon(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }
}
class Hero extends Character {
    private int level;
    private int gold;
    private Weapon weapon;
    private int xp = 0;

    public Hero(String name) {
        super(name, 120);
        this.level = 1;
        this.gold = 20;
        this.weapon = new Weapon("AK-47", 15);
    }

    public int getXp() { return xp; }
    public int getMaxHp() { return maxHp; }
    public int getGold() { return gold; }
    public int getLevel() { return level; }
    public Weapon getWeapon() { return weapon; }
    public void setWeapon(Weapon weapon) { this.weapon = weapon; }
    public void addGold(int gold) { this.gold += gold; }

    public void heal() {
        hp = maxHp;
        System.out.println(name + " har hittat viloplats. HP återställs till " + maxHp);
    }

    @Override
    public void attack(Character target) {
        int damage = weapon.getDamage();
        System.out.println(name + " attackerar " + target.getName() + " med " + weapon.getName() + " för " + damage + " skada");
        target.takeDamage(damage);
    }

    public void gainXp(int amount) {
        xp += amount;
        while (xp >= level * 30) {
            int xpThreshold = level * 50;
            levelUp();
            xp -= xpThreshold;
        }
    }

    public void levelUp() {
        level++;
        hp = maxHp;
        System.out.println(name + " gick upp till level " + level + "! HP återställs");
    }
}
abstract class Monster extends Character {
    protected int damage;

    public Monster(String name, int hp, int damage) {
        super(name, hp);
        this.damage = damage;
    }

    @Override
    public void attack(Character target) {
        target.takeDamage(damage);
        System.out.println(name + " attackerar " + target.getName() + " för " + damage + " skada");
    }
}

class Goblin extends Monster {
    public Goblin() {
        super("Goblin", 40, 10);
    }
}

class Boss extends Monster {
    private Random rand = new Random();

    public Boss() {
        super("Boss", 50, 20);
    }

    @Override
    public void attack(Character target) {
        boolean special = rand.nextInt(100) < 30;
        int actualDamage = special ? damage : 20;
        target.takeDamage(actualDamage);
        System.out.println(name + " attackerar " + target.getName() + " för " + actualDamage + " skada " + (special ? "Specialattack!" : ""));
    }
}
public class Adventuregame {
    private static Hero hero = new Hero("Ridwan");
    private static Scanner scanner = new Scanner(System.in);
    private static Random rand = new Random();

    public static void main(String[] args) {
        while (true) {
            showMenu();
            String input = scanner.nextLine();
            switch (input) {
                case "1": startAdventure(); break;
                case "2": showStatus(); break;
                case "3": System.out.println("Tack för du spelade!"); return;
                default: System.out.println("Ogiltigt val.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("-----Meny----");
        System.out.println("1. Start");
        System.out.println("2. Visa Status");
        System.out.println("3. Avsluta");
        System.out.println("Välj ett alternativ: ");
    }

    private static void startAdventure() {
        int chance = rand.nextInt(100);
        if (chance < 70) {
            battle(new Goblin());
        } else if (chance < 80) {
            battle(new Boss());
        } else {
            System.out.println("Du har hittat viloplats. HP återställs");
            hero.heal();
            showStatus();
        }
    }

    private static void battle(Monster monster) {
        System.out.println("Du möter " + monster.getName() + "!");
        while (hero.isAlive() && monster.isAlive()) {
            hero.attack(monster);
            if (!monster.isAlive()) break;
            monster.attack(hero);
            System.out.println("Din HP efter striden: " + hero.getHp());
        }

        if (hero.isAlive()) {
            System.out.println("Du besegrade " + monster.getName() + "!");
            rewardPlayer(monster);
            if (hero.getLevel() > 10) {
                System.out.println("Du har nått level 10! Du vann spelet!");
                System.exit(0);
            }
        } else {
            System.out.println("Du dog i striden. Spelet är över!");
            System.exit(0);
        }
    }

    private static void rewardPlayer(Monster monster) {
        if (monster instanceof Boss) {
            hero.gainXp(30);
            hero.addGold(30);
        } else {
            hero.gainXp(20);
            hero.addGold(10);
        }
    }

    private static void showStatus() {
        System.out.println("Status");
        System.out.println("Name: " + hero.getName());
        System.out.println("HP: " + hero.getHp());
        System.out.println("Level: " + hero.getLevel());
        System.out.println("Gold: " + hero.getGold());
        System.out.println("XP: " + hero.getXp());
        System.out.println("Max HP: " + hero.getMaxHp());
    }
}

