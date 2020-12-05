package com.javafrm;

import java.util.Scanner;

public class Main {

    public static byte[][] field = new byte[3][3];
    public static char[] conformity = new char[]{' ', 'X', 'O'};

    public static boolean onlyDigits(String str) {
        int i = 0;
        if (str.charAt(0) == '-' && str.length() > 1) ++i;
        for (; i < str.length(); ++i) {
            if (str.charAt(i) > '9' || str.charAt(i) < '0') return false;
        }
        return true;
    }

    public static int convertToInt(String str) {
        int ret = 0;
        int minus = 1;
        int i = 0;
        if (str.charAt(0) == '-') {
            ++i;
            minus = -1;
        }
        for (; i < str.length(); ++i) {
            ret *= 10;
            ret += str.charAt(i) - '0';
        }
        return ret * minus;
    }

    public static void defaultValues() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter cells: ");
        String start = scan.next();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (start.charAt(i * 3 + j) == '_') {
                    field[i][j] = 0;
                } else {
                    field[i][j] = (byte) (start.charAt(i * 3 + j) == 'X' ? 1 : 2);
                }
            }
        }
    }

    public static void printField() {
        System.out.println("---------");
        for (int i = 0; i < 3; ++i) {
            System.out.print("| ");
            for (int j = 0; j < 3; ++j) {
                System.out.printf("%c ", conformity[field[i][j]]);
            }
            System.out.println('|');
        }
        System.out.println("---------");
    }

    public static void turn() {
        Scanner scan = new Scanner(System.in);
        String[] xy;
        String x, y;
        int xInt, yInt;
        boolean ok = false;
        while (!ok) {
            System.out.print("Enter the coordinates: ");
            xy = scan.nextLine().split(" ");
            if (xy.length < 2) {
                xy = new String[]{"abra", "cadabra"};
            }
            x = xy[0]; y = xy[1];
            boolean bothInt = onlyDigits(x) & onlyDigits(y);
            if (bothInt && xy.length == 2) {
                xInt = convertToInt(x);
                yInt = convertToInt(y);
                if (xInt < 1 || yInt < 1 || xInt > 3 || yInt > 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else {
                    if (field[3 - yInt][xInt - 1] != 0) {
                        System.out.println("This cell is occupied! Choose another one!");
                    } else {
                        int[] c = new int[3];
                        for (int i = 0; i < 3; ++i) {
                            for (int j = 0 ; j  < 3; ++j) {
                                c[field[i][j]] += 1;
                            }
                        }
                        if (c[1] == c[2]) {
                            field[3 - yInt][xInt - 1] = 1;
                        } else {
                            field[3 - yInt][xInt - 1] = 2;
                        }
                        ok = true;
                    }
                }
            } else {
                System.out.println("You should enter numbers!");
            }
        }
    }

    public static boolean oneLine(int line) {
        boolean ret = true;
        for (int j = 1; j < 3; ++j) {
            ret = ret & field[line][j] == field[line][j - 1];
            ret = ret & !(field[line][j] == 0);
        }
        return ret;
    }
    public static boolean oneColumn(int column) {
        boolean ret = true;
        for (int j = 1; j < 3; ++j) {
            ret = ret & field[j][column] == field[j - 1][column];
            ret = ret & !(field[j][column] == 0);
        }
        return ret;
    }

    public static boolean diagonals() {
        boolean upDown = true, downUp = true;
        for (int i = 1; i < 3; ++i) {
            upDown = upDown & field[i - 1][i - 1] == field[i][i];
            upDown = upDown & !(field[i][i] == 0);
        }
        for (int i = 1; i < 3; ++i) {
            downUp = downUp & field[2 - i + 1][i - 1] == field[2 - i][i];
            downUp = downUp & !(field[2 - i][i] == 0);
        }
        return upDown | downUp;
    }

    public static int whoWin() {
        printField();
        int ret = 0;
        for (int i = 0; i < 3; ++i) {
            boolean thisLine = oneLine(i) | oneColumn(i);
            if (thisLine) {
                System.out.printf("%c wins", conformity[field[i][0]]);
                return field[i][0];
            }
        }
        if (diagonals()) {
            System.out.printf("%c wins", conformity[field[1][1]]);
            return field[1][1];
        }
        boolean empty = false;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                empty = empty | field[i][j] == 0;
            }
        }
        if (! empty) {
            System.out.println("Draw");
        } else {
            System.out.println("Game not finished");
        }

        return 0;
    }

    public static void main(String[] args) {
        defaultValues();
        printField();
        turn();
        whoWin();
    }
}
