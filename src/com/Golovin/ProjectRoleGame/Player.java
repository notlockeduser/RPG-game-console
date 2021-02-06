package com.Golovin.ProjectRoleGame;

import java.util.ArrayList;

public class Player extends ModelWarrior implements Cloneable {
    public Player() {
        money = 200;
        level = 0;
        for (int i = 0; i < GlobalVar.getNumHelp (); i++)
            listHelpers.add (null);
    }

    // для копирование характеристик ГГ игрока
    public BattleWarrior clone() {
        return new BattleWarrior (this.getType (),this.getDamage (),this.getHealth ());
    }

    private String name;
    private int money;
    private int level;

    protected ArrayList<Warrior> listHelpers = new ArrayList<> (GlobalVar.getNumHelp ());

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int number) {
        this.money = number;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int number) {
        this.level = number;
    }
}