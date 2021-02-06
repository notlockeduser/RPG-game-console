package com.Golovin.ProjectRoleGame;

// класс бойца который используется в походовой битве
public class BattleWarrior extends ModelWarrior implements Cloneable {
    private boolean statusAlive = true;

    public BattleWarrior(String type,int damage,int health) {
        this.setType (type);
        this.setDamage (damage);
        this.setHealth (health);
    }

    public boolean getStatusAlive() {
        return this.statusAlive;
    }

    public void setStatusAlive(boolean bool) {
        this.statusAlive = bool;
    }
}