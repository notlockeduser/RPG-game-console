package com.Golovin.ProjectRoleGame;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static Scanner Value = new Scanner (System.in); // сканер

    public static void main(String[] args) throws CloneNotSupportedException {
        mainMenu (); // главное меню игры
    }

    private static void mainMenu() throws CloneNotSupportedException {
        System.out.println ("1 - Новая игра");
        System.out.println ("2 - Загрузить игру");
        System.out.println ("3 - Выйти");
        System.out.print ("Ваш выбор = ");
        Player gamer = new Player (); // создание объекта главного героя (ГГ)
        Player bot = new Player (); // создание обьекта бота
        Shop store = new Shop (); // создание объекта магазина
        int value = Value.nextInt (); // Переменная для выбора пункта меню
        switch (value) {
            case 1:
                NewGame (gamer,bot,store);
                break;
            case 2:
                loadGame (gamer,bot);
                break;
        }
    }

    private static void NewGame(Player gamer,Player bot,Shop store) throws CloneNotSupportedException {
        settingsGamer (gamer); // первоначальная настройка ГГ
        settingsBot (bot); // настройка бота
        printMenu (); // Вывод меню выбора дальнейших действий ГГ
        funcMenu (gamer,bot); // Функциональное меню ( switch )
    }

    private static void funcMenu(Player gamer,Player bot) throws CloneNotSupportedException {
        int value = Value.nextInt ();  // Переменная для выбора пункта меню
        while (value != 5) {
            switch (value) {
                case 1:
                    Shop.menuShop (gamer); // пойти в магазин
                    break;
                case 2:
                    printListStats (gamer); // показать текущие показатели помощников и ГГ
                    break;
                case 3:
                    Battle.menuBattle (gamer,bot); // пойти в битву
                    break;
                case 4:
                    saveGame (gamer,bot);
                    break;
            }
            printMenu ();
            value = Value.nextInt ();
        }
    }

    private static void saveGame(Player gamer,Player bot) {
        try (BufferedWriter bw = new BufferedWriter (new FileWriter ("save.txt"))) {
            saveGamePlayer (gamer,bw);
            saveGamePlayer (bot,bw);
        } catch (IOException ex) {
            System.out.println (ex.getMessage ());
        }
        System.out.println ("\nИгра сохранена");
    }

    private static void saveGamePlayer(Player Object,BufferedWriter bw) throws IOException {
        bw.write (Object.getMoney () + "\n");
        bw.write (Object.getLevel () + "\n");
        bw.write (Object.getName () + "\n");
        bw.write (Object.getType () + "\n");
        bw.write (Object.getDamage () + "\n");
        bw.write (Object.getHealth () + "\n");

        int n = 0;
        while (Object.listHelpers.get (n) != null) {
            if (n + 1 == GlobalVar.getNumHelp ()) {
                n++;
                break;
            } else n++;
        }

        bw.write (n + "\n");
        for (int i = 0; i < n; i++) {
            bw.write (Object.listHelpers.get (i).getType () + "\n");
            bw.write (Object.listHelpers.get (i).getDamage () + "\n");
            bw.write (Object.listHelpers.get (i).getHealth () + "\n");
            bw.write (Object.listHelpers.get (i).getCost () + "\n");

        }
    }

    private static void loadGame(Player gamer,Player bot) throws CloneNotSupportedException {
        try (BufferedReader br = new BufferedReader (new FileReader ("save.txt"))) {
            loadGamePlayer (gamer,br);
            loadGamePlayer (bot,br);
        } catch (IOException ex) {

            System.out.println (ex.getMessage ());
        }
        System.out.println ("\n Игра загружена с сохранения");
        printMenu (); // Вывод меню выбора дальнейших действий ГГ
        funcMenu (gamer,bot); // Функциональное меню ( switch )
    }

    private static void loadGamePlayer(Player Object,BufferedReader br) throws IOException {
        Object.setMoney (Integer.parseInt (br.readLine ()));
        Object.setLevel (Integer.parseInt (br.readLine ()));
        Object.setName (br.readLine ());
        Object.setType (br.readLine ());
        Object.setDamage (Integer.parseInt (br.readLine ()));
        Object.setHealth (Integer.parseInt (br.readLine ()));

        int n = Integer.parseInt (br.readLine ());
        for (int i = 0; i < n; i++) {
            String type = br.readLine ();
            int damage = Integer.parseInt (br.readLine ());
            int health = Integer.parseInt (br.readLine ());
            int cost = Integer.parseInt (br.readLine ());
            Warrior temp = new Warrior (type,damage,health,cost);
            Object.listHelpers.set (i,temp.clone ());
        }
    }


    private static void printMenu() {
        System.out.print ("\n---Меню---" +
                "\n 1 - Пойти в таверну " +
                "\n 2 - Посмотреть характеристики ГГ и его помощников + Состояние казны" +
                "\n 3 - Пойти в битву " +
                "\n 4 - Сохранить игру " +
                "\n 5 - Выход\n");
        System.out.print ("Введите значение : ");
        Shop.refreshShop ();
    }

    public static void printListStats(Player gamer) {
        printHeroStats (gamer);
        System.out.println ("\n/*/ Ваши персонажи");
        if (gamer.listHelpers.get (0) != null) {
            for (int i = 0; i < GlobalVar.getNumHelp (); i++)
                if (gamer.listHelpers.get (i) != null) {
                    System.out.println (i + 1 +
                            " -  Тип = " + gamer.listHelpers.get (i).getType () +
                            " // Урон = " + gamer.listHelpers.get (i).getDamage () +
                            " // Здоровье = " + gamer.listHelpers.get (i).getHealth ());
                }
        } else {
            System.out.print ("-- У вас нет помощников\n");
        }
        System.out.println ("\n/*/ Баланс золотых = " + gamer.getMoney () +
                " ** Ваш уровень = " + gamer.getLevel ());
    }

    private static void printHeroStats(Player gamer) {
        System.out.print ("\n/*/ Ваш герой ->  ");
        System.out.println ("Имя = " + gamer.getName () +
                " // Тип = " + gamer.getType () +
                " // Урон = " + gamer.getDamage () +
                " // Здоровье = " + gamer.getHealth ());
    }

    private static void settingsGamer(Player gamer) {
        Scanner Value = new Scanner (System.in);
        System.out.print ("Введите имя = ");
        gamer.setName (Value.next ());
        System.out.print ("\nВыберите тип человека \n 1 - Рыцарь \n 2 - Лучник \n 3 - Маг");
        System.out.print ("\nВведите значение = ");
        int n = Value.nextInt ();
        chooseHero (gamer,n);
        printHeroStats (gamer);
    }

    private static void settingsBot(Player bot) {
        bot.setName ("Bot");
        chooseHero (bot,(int) (Math.random () * 2 + 1));
    }

    private static void chooseHero(Player Object,int n) {
        switch (n) {
            case 1:
                Object.setType ("Рыцарь");
                Object.setDamage ((int) (Math.random () * 35 + 30));
                Object.setHealth ((int) (Math.random () * 30 + 100));
                break;
            case 2:
                Object.setType ("Лучник");
                Object.setDamage ((int) (Math.random () * 10 + 45));
                Object.setHealth ((int) (Math.random () * 25 + 80));
                break;
            case 3:
                Object.setType ("Маг");
                Object.setDamage ((int) (Math.random () * 15 + 50));
                Object.setHealth ((int) (Math.random () * 20 + 60));
                break;
        }
    }
}