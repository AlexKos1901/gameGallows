import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final String PATH_TO_FILE = "russian_nouns.txt";
    private static final int ERROR_MAX = 7;      // максимальное количество ошибок
    private static final Scanner inputConsole = new Scanner(System.in);
    private static int errorCount;

    public static void main(String[] args) throws IOException {
        do {
            gameLoop();
        } while (askPlayerContinueGame());
    }

    public static void gameLoop() throws IOException {
        String gameWord = getWordFromFile(PATH_TO_FILE).toUpperCase();  // получение слова из словаря
        String hiddenWord = wordEncryption(gameWord);          // Скрытие слова
        errorCount = 0;                     // обнуление ошибок
        ArrayList<Character> enteredLetters = new ArrayList<>();         //обнуление строки введенных слов
        while (checkGameEnd(gameWord, hiddenWord)) {                   // запуск цикла программы проверка не закончена ли игра
            printBoard(errorCount, hiddenWord, enteredLetters.toString());   // вывод состояния игры в консоль
            char ch = playerTurn(enteredLetters.toString());// Ход игрока
            enteredLetters.add(ch);
            if (gameWord.indexOf(ch) > -1) {       // проверка наличия буквы в слове
                hiddenWord = openHiddenLetter(ch, gameWord, hiddenWord);  // Открытие совпавших букв
            } else {
                errorCount++;
            }
        }
    }

    public static String getWordFromFile(String filePath) throws IOException {
        String word;
        List<String> words = Files.readAllLines(Paths.get(filePath));
        do {
            word = words.get(new Random().nextInt(words.size()));
        } while (word.length() < 5);
        return word;
    }

    public static String wordEncryption(String gameWord) {
        char[] buffer = gameWord.toCharArray();
        for (int i = 0; i < gameWord.length(); i++) {   // формирование закрытого слова
            buffer[i] = '*';
        }
        return String.valueOf(buffer);
    }

    public static boolean checkGameEnd(String gameWord, String hiddenWord) {
        if (errorCount == ERROR_MAX) {            // если количество попыток исчерпано
            printHuman(errorCount);
            System.out.println("загаданное слово - " + gameWord);
            System.out.println("Вы проиграли");
            return false;
        }
        if (!hiddenWord.contains("*")) {
            System.out.println();
            System.out.println("загаданное слово - " + gameWord);
            System.out.println("Вы выиграли!!!!!!!");
            return false;  // если отгаданы все символы слова
        }
        return true;  // игра не закончена
    }

    public static void printBoard(int errorCount, String hiddenWord, String enteredLetters) {
        printHuman(errorCount);
        System.out.println();
        System.out.println("Ранее введенные буквы ");
        System.out.println(enteredLetters);
        System.out.println("Загадано слово из " + hiddenWord.length() + " букв:");
        System.out.println(hiddenWord);
    }

    public static char playerTurn(String enteredLetters) {

        char ch;
        String enteredPlayerString;

        boolean correctInput = false;
        System.out.println("Введите 1 букву русского алфавита и нажмите enter");
        do {
            enteredPlayerString = inputConsole.next();
            ch = Character.toUpperCase(enteredPlayerString.charAt(0));
            if ((enteredPlayerString.length() != 1) || (Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.CYRILLIC)) {
                System.out.println("Введите 1 букву русского алфавита и нажмите enter");
                continue;
            }
            if (enteredLetters.indexOf(ch) > -1) {
                System.out.println("Вы ранее вводили эту букву");
                continue;
            }
            correctInput = true;
        } while (!correctInput);
        return ch;
    }

    public static void printHuman(int errorCount) {
        System.out.println("--------");
        int i = 1;
        switch (errorCount) {
            case 0 -> i = 5;
            case 1 -> {
                System.out.println("   |   |");
                i = 4;
            }
            case 2 -> {
                System.out.println("   |   |");
                System.out.println("   0   |");
                i = 3;
            }
            case 3 -> {
                System.out.println("   |   |");
                System.out.println("   0   |");
                System.out.println("   |   |");
                i = 2;
            }
            case 4 -> {
                System.out.println("   |   |");
                System.out.println("   0   |");
                System.out.println("  -|   |");
                i = 2;
            }
            case 5 -> {
                System.out.println("   |   |");
                System.out.println("   0   |");
                System.out.println("  -|-  |");
                i = 2;
            }
            case 6 -> {
                System.out.println("   |   |");
                System.out.println("   0   |");
                System.out.println("  -|-  |");
                System.out.println("  |    |");
            }
            case 7 -> {
                System.out.println("   |   |");
                System.out.println("   0   |");
                System.out.println("  -|-  |");
                System.out.println("  | |  |");
            }
        }
        for (int j = 0; j < i; j++) {
            System.out.println("       |");
        }
        System.out.println("_______|");

    }

    public static String openHiddenLetter(char ch, String gameWord, String hiddenWord) {
        char[] bufferHidden = hiddenWord.toCharArray();
        char[] bufferGameWord = gameWord.toCharArray();
        for (int i = gameWord.indexOf(ch); i < gameWord.length(); i++) {
            if (bufferGameWord[i] == ch) {
                bufferHidden[i] = ch;
            }
        }
        return String.valueOf(bufferHidden);
    }

    public static boolean askPlayerContinueGame() {
        System.out.println();
        System.out.println("Хотите повторить игру ?");
        String vvod;
        do {
            System.out.println("Введите ДА для повторения игры");
            System.out.println("Введите НЕТ для окончания игры");
            vvod = inputConsole.next();
        } while (!vvod.equalsIgnoreCase("ДА") && !vvod.equalsIgnoreCase("НЕТ"));
        return vvod.equalsIgnoreCase("ДА");

    }

}