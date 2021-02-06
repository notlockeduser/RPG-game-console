package com.Golovin.ProjectRoleGame;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Battle {
    private static Scanner Value = new Scanner (System.in); // сканер

    public static void menuBattle(Player gamer,Player bot) throws CloneNotSupportedException {
        System.out.println ("\n--Выберите тип битвы--");
        System.out.print (" 1 - Обычная походовая битва\n 2 - Быстрая битва\n Ваш выбор : ");
        int value = Value.nextInt ();  // Переменная для выбора пункта меню
        switch (value) {
            case 1:
                simpleBattle (gamer,bot); // походовая битва
                break;
            case 2:
                fastBattle (gamer,bot);  // битва которая не принимает в расчёт последовательность ходов
                break;
        }
    }

    private static void simpleBattle(Player gamer,Player bot) throws CloneNotSupportedException {
        // создание двух массивов для битвы и заполнение копиями их -> герой + помощники
        ArrayList<BattleWarrior> listWarriorsGamer = new ArrayList<> (GlobalVar.getNumHelp () + 1);
        ArrayList<BattleWarrior> listWarriorsBot = new ArrayList<> (GlobalVar.getNumHelp () + 1);
        // создание стеков для проверки жив ли кто то в команде
        Stack<Object> statusAliveTeamGamer = new Stack<> ();
        Stack<Object> statusAliveTeamBot = new Stack<> ();
        // создание массивов со всеми героями для каждого игрока и заполнение стеков
        preparationToBattle (gamer,listWarriorsGamer,statusAliveTeamGamer);
        preparationToBattle (bot,listWarriorsBot,statusAliveTeamBot);
        // алгоритм битвы
        gameBattle (listWarriorsGamer,listWarriorsBot,gamer,bot,statusAliveTeamGamer,statusAliveTeamBot);
    }

    private static void preparationToBattle(Player player,ArrayList<BattleWarrior> listWarriors,
                                            Stack<Object> statusAliveTeam) {
        listWarriors.add (player.clone());
        statusAliveTeam.push (true);
        for (int i = 0; i < player.listHelpers.size () && player.listHelpers.get (i) != null; i++) {
            listWarriors.add (player.listHelpers.get (i).clone (true));
            statusAliveTeam.push (true);
        }

    }

    private static void gameBattle(ArrayList<BattleWarrior> WarriorsGamer,ArrayList<BattleWarrior> WarriorsBot,
                                   Player gamer,Player bot,Stack<Object> statusAliveTeamGamer,
                                   Stack<Object> statusAliveTeamBot) throws CloneNotSupportedException {
        int countStep = 1; // счетчик ходов
        while (!statusAliveTeamBot.empty () && !statusAliveTeamGamer.empty ()) {
            printForce (WarriorsGamer,WarriorsBot);  // выводит характеристики бойцов с каждой стороны
            stepGamer (WarriorsGamer,WarriorsBot,countStep,statusAliveTeamGamer,statusAliveTeamBot); // ход Игрока
            stepBot (WarriorsGamer,WarriorsBot,statusAliveTeamGamer,statusAliveTeamBot); // ход Бота
            countStep++;
        }
        // показывает результат битвы и расчитывает данные для следущей битвы
        resultBattle (gamer,bot,statusAliveTeamGamer,statusAliveTeamBot);
    }

    private static void stepGamer(ArrayList<BattleWarrior> warriorsGamer,ArrayList<BattleWarrior> warriorsBot,
                                  int countStep,Stack<Object> statusAliveTeamGamer,Stack<Object> statusAliveTeamBot) {
        System.out.println ("\n*********************\n\t\t Ход " + countStep + "\n*********************");
        for (BattleWarrior warrior : warriorsGamer) { //каждый боец нанесет урон, если он живой
            if (warrior.getStatusAlive () && !statusAliveTeamBot.empty ()) {
                int i = 0;
                System.out.println ("\n ---Урон наносит---\n" +
                        (i + 1) +
                        ".   Тип = " + warrior.getType () +
                        "\n\t Урон = " + warrior.getDamage () +
                        "\n\t Здоровье = " + warrior.getHealth () +
                        "\n\t Статус = " + printAliveOrDead (warrior.getStatusAlive ()));
                System.out.println ("\n --- Bot войска ---");
                printForceArray (warriorsBot);
                System.out.print ("\n Выберете кого вы хотите ударить : ");
                int value = Value.nextInt () - 1;
                while (!warriorsBot.get (value).getStatusAlive ()) {
                    System.out.print (" Этот войн уже мертв\n Выберете кого вы хотите ударить : ");
                    value = Value.nextInt () - 1;
                }
                makeDamage (warrior,warriorsBot.get (value),statusAliveTeamBot,statusAliveTeamGamer);
            }
        }
    }

    private static void stepBot(ArrayList<BattleWarrior> warriorsGamer,ArrayList<BattleWarrior> warriorsBot,
                                Stack<Object> statusAliveTeamGamer,Stack<Object> statusAliveTeamBot) {
        if (!statusAliveTeamBot.empty ()) { // если команда Бота не мертва
            for (BattleWarrior warrior : warriorsBot) { //каждый боец нанесет урон, если он живой
                if (warrior.getStatusAlive () && !statusAliveTeamGamer.empty ()) {
                    int value = (int) (Math.random () * warriorsGamer.size ());
                    while (!warriorsBot.get (value).getStatusAlive ()) {
                        value = (int) (Math.random () * warriorsGamer.size ());
                    }
                    makeDamage (warrior,warriorsGamer.get (value),statusAliveTeamGamer,statusAliveTeamBot);
                }
            }
        }
    }

    private static void makeDamage(BattleWarrior warrior,BattleWarrior victim,
                                   Stack<Object> statusAliveTeam1,Stack<Object> statusAliveTeam2) {
        // боец наносит урон жертве
        victim.setHealth (victim.getHealth () - warrior.getDamage ());
        // жертва отбивается 40% собственным уроном
        warrior.setHealth (warrior.getHealth () - (int) (0.4 * victim.getDamage ()));

        // проверка живы ли бойцы после нанесения урона
        if (victim.getHealth () <= 0) {
            victim.setStatusAlive (false);
            victim.setHealth (0);
            statusAliveTeam1.pop ();
        }
        if (warrior.getHealth () <= 0) {
            warrior.setStatusAlive (false);
            warrior.setHealth (0);
            statusAliveTeam2.pop ();
        }
    }

    private static void printForce(ArrayList<BattleWarrior> warriorsGamer,ArrayList<BattleWarrior> warriorsBot) {
        System.out.println ("\n--------------------\n > Ваши войска");
        printForceArray (warriorsGamer);
        System.out.println ("***********************\n > Bot войска");
        printForceArray (warriorsBot);
        System.out.println ("----------------------");
    }

    private static void printForceArray(ArrayList<BattleWarrior> warriors) {
        int i = 0;
        for (BattleWarrior warrior : warriors) {
            System.out.println ((i++ + 1) +
                    ".   Тип = " + warrior.getType () +
                    "\n\t Урон = " + warrior.getDamage () +
                    "\n\t Здоровье = " + warrior.getHealth () +
                    "\n\t Статус = " + printAliveOrDead (warrior.getStatusAlive ()));

        }
    }

    private static String printAliveOrDead(boolean statusAlive) {
        if (statusAlive) return ("Жив");
        else return ("Мёртв");
    }

    private static void resultBattle
            (Player gamer,Player bot,Stack<Object> statusAliveTeamGamer,
             Stack<Object> statusAliveTeamBot) throws CloneNotSupportedException {

        if ((!statusAliveTeamGamer.empty ()) && (statusAliveTeamBot.empty ())) {
            System.out.print ("\n --- Победа --- \n");
            // с увеличением уровня увеличивается урон пресонажа и помощниов
            recalculationForce (gamer);
            recalculationForce (bot);
            gamer.setMoney (gamer.getMoney () + 100);
        } else if ((statusAliveTeamGamer.empty ()) && (statusAliveTeamBot.empty ())) {
            System.out.print ("\n --- Ничья ---\n");
            gamer.setMoney (gamer.getMoney () + 30);
        } else if ((statusAliveTeamGamer.empty ()) && (!statusAliveTeamBot.empty ())) {
            System.out.print ("\n --- Проигрыш --- \n");
            gamer.setMoney (gamer.getMoney () + 15);
        }
        Shop.changeHelpersBot (bot);
    }

    private static void recalculationForce(Player Object) {
        Object.setLevel (Object.getLevel () + 1);
        for (int i = 0; i < GlobalVar.getNumHelp (); i++)
            if (Object.listHelpers.get (i) != null) {
                Object.listHelpers.get (i).setDamage ((int) (Object.listHelpers.get (i).getDamage () + (1 + 0.05 * Object.getLevel ())));
                Object.listHelpers.get (i).setHealth ((int) (Object.listHelpers.get (i).getHealth () + (1 + 0.05 * Object.getLevel ())));
                Object.setDamage ((int) (Object.getDamage () + (1 + 0.05 * Object.getLevel ())));
                Object.setHealth ((int) (Object.getHealth () + (1 + 0.05 * Object.getLevel ())));
            }
    }

    private static void fastBattle(Player gamer,Player bot) {
        int allDamagePlayer = gamer.getDamage ();
        int allHealthPlayer = gamer.getHealth ();
        int allDamageBot = bot.getDamage ();
        int allHealthBot = bot.getHealth ();
        for (int i = 0; i < GlobalVar.getNumHelp (); i++) {
            if (gamer.listHelpers.get (i) != null) {
                allDamagePlayer += gamer.listHelpers.get (i).getDamage ();
                allHealthPlayer += gamer.listHelpers.get (i).getHealth ();
            }
            if (bot.listHelpers.get (i) != null) {
                allDamageBot += bot.listHelpers.get (i).getDamage ();
                allHealthBot += bot.listHelpers.get (i).getHealth ();
            }
        }
        System.out.print ("\nОбщий урон игрока = " + allDamagePlayer +
                " /// Общее здоровье игрока = " + allHealthPlayer);
        System.out.print ("\nОбщий урон бота = " + allDamageBot +
                " /// Общее здоровье бота = " + allHealthBot);
        while (allHealthPlayer > 0 || allHealthBot > 0) {
            allHealthPlayer -= allDamageBot;
            allHealthBot -= allDamagePlayer;
        }
        if ((allHealthPlayer - allDamageBot) > (allHealthBot - allDamagePlayer)) {
            System.out.print ("\n --- Победа --- \n");
            recalculationForce (gamer);
            recalculationForce (bot);
            gamer.setMoney (gamer.getMoney () + 100);
        } else if ((allHealthPlayer - allDamageBot) == (allHealthBot - allDamagePlayer)) {
            System.out.print ("\n --- Ничья --- \n");
            gamer.setMoney (gamer.getMoney () + 30);
        } else if ((allHealthPlayer - allDamageBot) < (allHealthBot - allDamagePlayer)) {
            System.out.print ("\n --- Проигрыш ---\n");
            gamer.setMoney (gamer.getMoney () + 10);
        }
        Shop.changeHelpersBot (bot);
    }
}