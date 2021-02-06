package com.Golovin.ProjectRoleGame;

import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
    private static Scanner Value = new Scanner (System.in); // сканнер
    private static ArrayList<Warrior> listGoods = new ArrayList<> (GlobalVar.getNumShop ());

    Shop() {
        listGoods.add (0,new Warrior ("Рыцарь",(int) (Math.random () * 20 + 15),(int) (Math.random () * 50 + 50),(int) (Math.random () * 200 + 200)));
        listGoods.add (1,new Warrior ("Лучник",(int) (Math.random () * 25 + 20),(int) (Math.random () * 50 + 30),(int) (Math.random () * 200 + 200)));
        listGoods.add (2,new Warrior ("Маг   ",(int) (Math.random () * 30 + 25),(int) (Math.random () * 50 + 25),(int) (Math.random () * 200 + 200)));
    }

    public static void menuShop(Player gamer) {
        System.out.print ("\n ---Выберите действие --- ");
        System.out.print ("\n 1 - Купить помощников \n 2 - Продать помощников \n\n Ваш выбор : ");
        int value = Value.nextInt ();  // Переменная для выбора пункта меню
        switch (value) {
            case 1:
                buyHelpers (gamer);
                break;
            case 2:
                saleHelpers (gamer);
                break;
        }
    }

    private static void buyHelpers(Player gamer) {
        // если последнего помощника нет то продолжить покупку
        if (((gamer.listHelpers.get (GlobalVar.getNumHelp () - 1)) == null)) {
            System.out.println ("\nВыбор героя для покупки");
            outputListGoods (); // Вывод списка доступных помощников - героев
            System.out.println ("Ваши деньги = " + gamer.getMoney ());
            System.out.print ("Введите число = ");
            int n = Value.nextInt ();
            if ((gamer.getMoney () - listGoods.get (n - 1).getCost ()) >= 0) {
                for (int i = 0, k = 0; k == 0; i++)
                    if (gamer.listHelpers.get (i) == null) {
                        gamer.listHelpers.set (i,listGoods.get (n - 1).clone ());
                        k = 1;
                    }
                gamer.setMoney (gamer.getMoney () - listGoods.get (n - 1).getCost ());
            } else System.out.print ("\n У вас не хватает денег на данного помощника \n");
        } else {
            System.out.print ("\n //// Лист помощников полон //// \n");
            String delay = Value.nextLine (); // задержка
        }
    }

    private static void outputListGoods() {
        for (int i = 0; i < (GlobalVar.getNumShop ()); i++) {
            System.out.print (i + 1 +
                    " -  Тип = " + listGoods.get (i).getType () +
                    "\t // Урон = " + listGoods.get (i).getDamage () +
                    "\t // Здоровье = " + listGoods.get (i).getHealth () +
                    "\t // Стоймость = " + listGoods.get (i).getCost () + "\n");
        }
    }

    private static void saleHelpers(Player gamer) {
        if (gamer.listHelpers.get (0) != null) {
            Main.printListStats (gamer); // вывод помощников ГГ
            System.out.print ("Введите номер = ");
            int n = Value.nextInt ();
            gamer.setMoney (gamer.getMoney () + (int) (gamer.listHelpers.get (n - 1).getCost () / 2)); // добавление денег за продажу персонажа
            gamer.listHelpers.set (n - 1,null); // удаление помощника
            for (int i = 0; i < GlobalVar.getNumHelp () - 1; i++)
                if (gamer.listHelpers.get (i) == null && gamer.listHelpers.get (i + 1) != null) {
                    gamer.listHelpers.set (i,gamer.listHelpers.get (i + 1).clone ());
                    gamer.listHelpers.set (i + 1,null);
                }
        } else {
            System.out.println ("У вас нет помощников");
        }
    }

    public static void refreshShop() {
        listGoods.set (0,new Warrior ("Рыцарь",(int) (Math.random () * 20 + 15),(int) (Math.random () * 50 + 50),(int) (Math.random () * 200 + 200)));
        listGoods.set (1,new Warrior ("Лучник",(int) (Math.random () * 25 + 20),(int) (Math.random () * 50 + 30),(int) (Math.random () * 200 + 200)));
        listGoods.set (2,new Warrior ("Маг   ",(int) (Math.random () * 30 + 25),(int) (Math.random () * 50 + 25),(int) (Math.random () * 200 + 200)));

    }

    public static void changeHelpersBot(Player bot) {
        for (int i = 0; (i < GlobalVar.getNumHelp ()) && (i < bot.getLevel ()); i++)
            if (bot.listHelpers.get (i) == null) {
                bot.listHelpers.set (i,listGoods.get (i).clone ());
            }
    }
}