import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        do {
            gameLoop();
        } while (askPlayerContinueGame());
    }
    private static final String PATH_TO_FILE  = "russian_nouns.txt";
    private static int errorCount;
    private static final int ERROR_MAX = 7;      // максимальное количество ошибок
    private static final int BOARD_COL = 8;  // длина поля
    private static final int BOARD_ROW = 7;  // Высота поля
    public static void gameLoop () throws IOException {
        String gameWord = getWordFromFile(PATH_TO_FILE).toUpperCase();  // получение слова из словаря
        String hiddenWord = wordEncryption(gameWord);          // Скрытие слова
        String[][] board = createBoard(); // Создание игрового поля
        errorCount = 0;                     // обнуление ошибок
        String enteredLetters = "";         //обнуление строки введенных слов
        while (CheckGameEnd(gameWord,hiddenWord)){                   // запуск цикла программы проверка не закончена ли игра
            printBoard(board,hiddenWord,enteredLetters);   // отрисовка поля в консоле
            char ch = playerTurn(enteredLetters);// Ход игрока
            enteredLetters = enteredLetters +ch;
            if (gameWord.indexOf(ch)>-1) {       // проверка наличия буквы в слове
                hiddenWord = openHiddenLetter(ch,gameWord,hiddenWord);  // Открытие совпавших букв
            } else {
                errorCount++;
                addHumanPartOnBoard(errorCount, board);
            }
        }
    }

    public static String getWordFromFile (String filePath) throws IOException{
        //try {
        String word ;
        List<String> words = Files.readAllLines(Paths.get(filePath));
        do {
                /*File file = new File(filePath);
                FileReader fr = new FileReader(file);
                BufferedReader bf = new BufferedReader(fr);
                int j = (int) (Math.random()*51300);
                for (int i = 0; i < j; i++) {
                    bf.readLine();
                }
                word= bf.readLine();*/
            word = words.get(new Random().nextInt(words.size()) );
        } while (word.length()<5);
        return word;
            /*}
        catch (FileNotFoundException e){
            e.printStackTrace();
            return "Даздраперма";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Владилен";
        } */

    }
    public static String wordEncryption(String gameWord) {
        char [] buffer = gameWord.toCharArray();
        for (int i = 0; i < gameWord.length(); i++) {   // формирование закрытого слова
            buffer[i]='*';
        }
        return String.valueOf(buffer);
    }
    public static String[][] createBoard(){                 // Создание поля
        String[][] board = new String[BOARD_ROW][BOARD_COL]; //создаем матрицу внутри метода по размерам из класса
        for (int row = 0; row < BOARD_ROW ; row++) {         // рисуем в матрице столб
            board [row][BOARD_COL-1] = "|";
        }
        for (int col = 0; col < BOARD_COL; col++) {          // рисуем в матрице штангу и землю
            board[0][col] = "_";
            board[BOARD_ROW-1][col]="_";
        }
        for (int row = 1; row <BOARD_ROW-1 ; row++) {          // Остальное поле в матрице заполняем пустотой
            for (int col = 0; col < BOARD_COL-1 ; col++) {
                board[row][col]=" ";
            }
        }
        return board;
    }
    public static boolean CheckGameEnd(String gameWord,String hiddenWord){
        if (errorCount == ERROR_MAX) {            // если количество попыток исчерпано
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
    public static void printBoard(String[][] board,String hiddenWord,String enteredLetters){
        for (int row = 0; row <BOARD_ROW ; row++) {
            System.out.println();
            for (int col = 0; col <BOARD_COL; col++) {
                System.out.print(board[row][col]);
            }
        }
        System.out.println();
        System.out.println("Ранее введенные буквы " );
        StringBuilder buf = new StringBuilder();
        for (char x:enteredLetters.toCharArray()) {
            buf.append(x).append(' ') ;
        }

        System.out.println(buf);
        System.out.println("Загадано слово из " +hiddenWord.length() +" букв:");
        System.out.println(hiddenWord);
    }
    public static char playerTurn(String enteredLetters) {
        Scanner input = new Scanner(System.in);
        char ch ;
        String enteredPlayerString ;
        do {
            do {
                System.out.println("Введите 1 букву русского алфавита и нажмите enter");
                enteredPlayerString =  input.next();
            } while (enteredPlayerString.length()!=1);
            ch = Character.toUpperCase(enteredPlayerString.charAt(0));
            if (enteredLetters.indexOf(ch)>-1) {
                System.out.println("Вы ранее вводили эту букву");
                ch = 0;
            }
        } while ( Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.CYRILLIC);
        return  Character.toUpperCase(ch);
    }
    public static void addHumanPartOnBoard(int errorCount,String[][] board){               // Добавление элементов туловища через switch в доску
        switch (errorCount) {
            case 7:
                board[4][4] = "|"; // нарисовать правую ногу
            case 6:
                board[4][2] = "|"; // нарисовать левую ногу
            case 5:
                board[3][4] = "-"; // нарисовать правую руку
            case 4:
                board[3][2] = "-"; // нарисовать левую руку
            case 3:
                board[3][3] = "|"; // нарисовать туловище
            case 2:
                board[2][3] = "0"; // нарисовать голову
            case 1:
                board[1][3] = "|"; // Нарисовать веревку
        }
    }
    public static String openHiddenLetter(char ch,String gameWord,String hiddenWord){
        char[] bufferHidden = hiddenWord.toCharArray();
        char[] bufferGameWord = gameWord.toCharArray();
        for (int i = gameWord.indexOf(ch); i < gameWord.length(); i++) {
            if (bufferGameWord[i]==ch){
                bufferHidden[i] = ch;
            }
        }
        return String.valueOf(bufferHidden);
    }
    public static boolean askPlayerContinueGame(){
        System.out.println();
        System.out.println("Хотите повторить игру ?");
        Scanner input = new Scanner(System.in);
        String vvod = "" ;
        while (!vvod.equalsIgnoreCase("ДА")  && !vvod.equalsIgnoreCase("НЕТ")){
            System.out.println();
            System.out.println("Введите ДА для повторения игры");
            System.out.println();
            System.out.println("Введите НЕТ для окончания игры");
            vvod = input.nextLine().toUpperCase();
            System.out.println(vvod);
        }
        return vvod.equalsIgnoreCase("ДА");

    }

}