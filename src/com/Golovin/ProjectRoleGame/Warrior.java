package com.Golovin.ProjectRoleGame;

public class Warrior extends ModelWarrior implements Cloneable {
    private int cost;

    Warrior(String type,int damage,int health,int cost) {
        this.setType (type);
        this.setDamage (damage);
        this.setHealth (health);
        this.cost = cost;
    }

    // для копирование характеристик помощников ГГ игрока
    public BattleWarrior clone(boolean i) {
        return new BattleWarrior (this.getType (),this.getDamage (),this.getHealth ());
    }

    // для копирования всех характеристик помощников ГГ игрока
    public Warrior clone() {
        return new Warrior (this.getType (),this.getDamage (),this.getHealth (), this.getCost ());
    }
    public int getCost() {
        return this.cost;
    }
}