package com.Golovin.ProjectRoleGame;

public abstract class ModelWarrior implements Cloneable {

    private String type;
    private int damage;
    private int health;

    public String getType() {
        return this.type;
    }

    public void setType(String string) {
        this.type = string;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int number) {
        this.damage = number;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int number) {
        this.health = number;
    }
}