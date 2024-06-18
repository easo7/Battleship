import java.util.*;
import java.io.*;

public class Battleship {
    static String [][]playerBoard, cpuBoard, playerBoardShot, cpuBoardShot;
    final static int BOARD_LENGTH = 10;

    static int difficulty;

    public static void startGame(int gameStatus){
        //variable setup
        boolean gameRunning = true;
        playerBoard = new String[BOARD_LENGTH][BOARD_LENGTH];
        cpuBoard = new String[BOARD_LENGTH][BOARD_LENGTH];
        playerBoardShot = new String[BOARD_LENGTH][BOARD_LENGTH];
        cpuBoardShot = new String[BOARD_LENGTH][BOARD_LENGTH];
        int turn = 0;
        int numMoves, numMovesCPU, result;
        String choice ="";

        //ask user for difficulty
        System.out.print("Enter Difficulty, 0 for easy, 1 for normal: ");
        difficulty = Integer.parseInt(Input.integerInput());
        //makes sure that the user will enter a valid number
        while (!(difficulty==0 || difficulty==1)) {
            System.out.println("Please enter a valid input: ");
            System.out.print("Enter Difficulty, 0 for easy, 1 for normal: ");
            difficulty = Integer.parseInt(Input.integerInput());

        }

        //setup player and cpu board
        if(gameStatus==0){
            //setup new array for player and cpu in a new game

            //fill array with "-"
            for(int i =0; i<10;i++){
                for(int k=0;k<10;k++) {
                    playerBoard[i][k] = "-";
                    playerBoardShot[i][k] = "-";
                    cpuBoard[i][k] = "-";
                    cpuBoardShot[i][k] = "-";
                }
            }

            //setup ships
            placeShips();
            placeCpuShip();
        }
        //if user wants to load existing game
        else if(gameStatus==1){
            //check for turns of each player and load board
            numMoves = loadExistingGamePlayer();
            numMovesCPU = loadExistingGameCPU();
            displayPlayerShots();
            turn = 0;
        }

        while(gameRunning){
            if(turn==0) {
                //asks for user choice on what to do on his turn
                System.out.println();
                System.out.print("Would you like to take a Shot(1), Save the Game(2), or Surrender (3): ");
                choice = Input.integerInput();
                while(!(choice.equals("1")||choice.equals("2")||choice.equals("3"))){
                    System.out.println("Enter a Valid Input");
                    System.out.print("Would you like to take a Shot(1), Save the Game(2), or Surrender (3): ");
                    choice = Input.integerInput();
                }

                if(choice.equals("1")) {
                    //take shot
                    result = takePlayerShot();
                    //informs user of result of shot
                    displayPlayerShots();
                    System.out.println();
                    switch(result){
                        case 0:
                            System.out.println("You have hit nothing ");
                            break;
                        case 1:
                            System.out.println("Bang! You have hit a Target");
                            break;
                        case 2:
                            System.out.println("BOOM! A Ship has been destroyed!");
                            gameRunning = checkWin(1);
                            break;
                    }
                    System.out.println("Press Enter to Continue");
                    Input.stringInput();
                    //switch to cpu turn
                    turn =1;
                }
                else if(choice.equals("2")) {
                    //save game option
                    saveFile();
                    System.out.println("Have a Good Day");
                    gameRunning = false;
                }
                else{
                    //surrender option
                    System.out.println("You have Lost! Here are the Statuses of the two Boards: ");
                    displayPlayerBoard();
                    displayCPUBoard();
                    gameRunning = false;
                }
            }

            if(gameRunning && turn==1) {
                result = takeCpuShot(difficulty);
                displayPlayerBoard();
                //informs user of result of shot
                switch(result){
                    case 0:
                        System.out.println("The Cpu has Missed. It is your Turn now");
                        break;
                    case 1:
                        System.out.println("The Cpu has hit your ship! It is your Turn now");

                        break;
                    case 2:
                        System.out.println("KABOOM! Your Ship has been destroyed");
                        gameRunning = checkWin(0);
                        break;
                }
                turn=0;
            }
        }
    }

    public static int loadExistingGamePlayer (){
        //varable setup
        final String SAVE_FILE = "Save.txt";
        int numMoves = 0;
        BufferedReader in;
        String line;

        try{
            //setup buffered reader and read lines
            in = new BufferedReader(new FileReader(SAVE_FILE));
            in.readLine();

            //fill playerBoard
            for(int i=0;i<10;i++){
                line = in.readLine();
                for(int k=0;k<10;k++){
                    playerBoard [i][k] = String.valueOf(line.charAt(k));
                    if(String.valueOf(line.charAt(k)).equals("X")||String.valueOf(line.charAt(k)).equals("O")){
                        numMoves++;
                    }
                }
            }
            //read empty lines
            for(int i=0;i<12;i++){
                in.readLine();
            }
            //fill playerBoardShots
            for(int i=0;i<10;i++){
                line = in.readLine();
                for(int k=0;k<10;k++){
                    playerBoardShot [i][k] = String.valueOf(line.charAt(k));

                }
            }

        }
        catch(IOException e){
            System.out.println("Error with" + e);
        }
        return numMoves;

    }

    public static int loadExistingGameCPU (){
        //varable setup
        final String SAVE_FILE = "Save.txt";
        int numMovesCPU = 0;
        BufferedReader in;
        String line;

        try{
            //setup buffered reader and read lines
            in = new BufferedReader(new FileReader(SAVE_FILE));

            //read Empty Lines
            for (int i =0; i<12;i++){
                in.readLine();
            }

            //fill cpu Board
            for(int i=0;i<10;i++){
                line = in.readLine();
                for(int k=0;k<10;k++){
                    cpuBoard [i][k] = String.valueOf(line.charAt(k));
                    if((String.valueOf(line.charAt(k)).equals("X"))||String.valueOf(line.charAt(k)).equals("O")){
                        numMovesCPU++;
                    }
                }
            }
            //read empty lines
            for(int i=0;i<12;i++){
                in.readLine();
            }

            //fill cpuBoard Shots
            for(int i=0;i<10;i++){
                line = in.readLine();
                for(int k=0;k<10;k++){
                    cpuBoardShot [i][k] = String.valueOf(line.charAt(k));
                }
            }
        }
        catch(IOException e){
            System.out.println("Error with" + e);
        }

        return numMovesCPU;
    }

    public static int takeShot(int x, int y, int board){
        int row, column, result =0;
        boolean cantakeShot = false;
        String ship;

        row = y;
        column = x;

        if(board==1) {
            //if player misses his shot
            if (cpuBoard[row][column].equals("-")) {
                cpuBoard[row][column] = "O";
                cpuBoardShot[row][column] = "O";
               result = 0;
            } else {
                //when player hits a ship
                ship = cpuBoard[row][column];
                //if player finishes a specific ship
                if (!shipExists(ship,1)) {
                    result = 2;
                }
                //if player hits but doesn't destory the ship
                else{
                    result = 1;
                }
                cpuBoard[row][column] = "X";
                cpuBoardShot[row][column] = "X";

            }
        }
        else{
            //if cpu misses its shot
            if (playerBoard[row][column].equals("-")) {
                playerBoard[row][column] = "O";
                playerBoardShot[row][column] = "O";

                result = 0;
            } else {
                //if cpu hits a ship
                ship = playerBoard[row][column];
                //if CPu finishes a specific ship

                if (!shipExists(ship,0)) {
                    result = 2;
                }
                //when cpu doesn't finish the ship
                else {
                    result = 1;
                }
                playerBoard[row][column] = "X";
                playerBoardShot[row][column] = "X";
            }
        }
        return result;
    }

    public static int takePlayerShot(){
        //varable setup
        int row, column;
        int result;
        boolean cantakeShot=false;
        do {
            displayPlayerShots();
            System.out.println("It is your turn to take a shot");
            //etner valid x input
            System.out.print("Enter the x Coordinate: ");
            column = Integer.parseInt(Input.integerInput())-1;

            while (!(column <= 10 && column >= 0)) {
                System.out.print("Please enter a valid input: ");
                column = Integer.parseInt(Input.integerInput())-1;
            }

            //input valid y input
            System.out.print("Enter the y Coordinate: ");
            row = Integer.parseInt(Input.integerInput())-1;
            while (!(row <= 10 && row >= 0)) {
                System.out.print("Please enter a valid input: ");
                row = Integer.parseInt(Input.integerInput())-1;
            }
            //check for valid shot
            if(!(cpuBoard[row][column].equals("O"))||!(cpuBoard[row][column].equals("X"))){
                cantakeShot = true;
            }
            else{
                System.out.println("Invalid Shot");
            }

        }while(!cantakeShot);

        //take shot
        result = takeShot(column, row  , 1);


        return result;
    }
    public static int takeCpuShot(int difficulty){
        //varable setup
        int result =0;
        int row = 0, column =0;
        boolean canTakeShot = false;

        do {
            //take shot for ai easy difficulty
            if(difficulty==0){
                row = (int)(Math.random()*10);
                column = (int)(Math.random()*10);
            }

            //check for valid shot
            if(!(playerBoard[column][row].equals("O"))||!(playerBoard[column][row].equals("X"))){
                canTakeShot = true;
            }
            else{
                System.out.println("Invalid Shot");
            }

        }while(!canTakeShot);

        result = takeShot(column,row,0);

        return result;
    }

    public static boolean shipExists(String ship, int player){
        //variable setup
        boolean exists = false;
        System.out.println("ship" + ship);

        //for cpu
        if(player==0){
            //searches for ship of the same type
            for (int i = 0; i < 10; i++) {
                for (int j= 0; j < 10; j++) {
                    if(playerBoard[i][j].equals(ship)){
                        exists=true;
                        break;
                    }
                }
            }
        }
        else {
            //searches for ship of the same type
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (cpuBoard[i][j].equals(ship)) {
                        exists = true;
                        break;
                    }
                }
            }
        }
        return exists;
    }


    public static boolean checkWin(int player){
        boolean win = false;

        if (player==1){
            for (int i = 0; i < 10; i++) {
                for (int j= 0; j < 10; j++) {
                    if(!((cpuBoard[i][j].equals("-"))||cpuBoard[i][j].equals("X")||cpuBoard[i][j].equals("O"))){
                        win=true;
                    }
                }
            }
        }
        else if (player==0){
            for (int i = 0; i < 10; i++) {
                for (int j= 0; j < 10; j++) {
                    if(!((playerBoard[i][j].equals("-"))||playerBoard[i][j].equals("X")||playerBoard[i][j].equals("O"))){
                        win=true;
                    }
                }
            }
        }


        return win;
    }

    public static void displayPlayerBoard(){

        //print player Board
            //print row
            System.out.println();
            System.out.println("            \\\\Your Board//");
            System.out.print("     ");
            for (int i =1;i<=10;i++){
                System.out.printf("%-3d",i);
            }
             System.out.println();

            //print board
            for (int i=0;i<10;i++){
                System.out.printf("%3d",i+1);
                for(int k =0;k<10;k++){
                    System.out.printf("%3s",playerBoard[i][k]);
                }
                System.out.println();
            }
    }

    public static void displayPlayerShots(){
        System.out.println();
        System.out.println("           \\\\Your Shots//");
        System.out.print("     ");
        for (int i =1;i<=10;i++){
            System.out.printf("%-3d",i);
        }
        System.out.println();

        //print board
        for (int i=0;i<10;i++){
            System.out.printf("%3d",i+1);
            for(int k =0;k<10;k++){
                System.out.printf("%3s",cpuBoardShot[i][k]);
            }
            System.out.println();
        }
    }
    public static void displayCPUBoard(){
        //print row
        System.out.println();
        System.out.println("            \\\\CPU Board//");
        System.out.print("     ");
        for (int i =1;i<=10;i++){
            System.out.printf("%-3d",i);
        }
        System.out.println();

        //print board
        for (int i=0;i<10;i++){
            System.out.printf("%3d",i+1);
            for(int k =0;k<10;k++){
                System.out.printf("%3s",cpuBoard[i][k]);
            }
            System.out.println();
        }
    }

    public static void placeShips(){
        //variable setup
        int column, row, direction,length = 0;

        displayPlayerBoard();
        for(int j=0;j<5;j++) {
            //empty line
            System.out.println();
            do {
                //tell user what ship they will be placing
                switch (j) {
                    case 0:
                        System.out.println("You will be placing your Aircraft Carrier (5 units Long)");
                        length =5;
                        break;
                    case 1:
                        System.out.println("You will be placing your Battleship (4 units Long)");
                        length =4;
                        break;
                    case 2:
                        System.out.println("You will be placing your Submarine (3 units Long)");
                        length =3;
                        break;
                    case 3:
                        System.out.println("You will be placing your Cruiser (3 units Long)");
                        length =3;
                        break;
                    case 4:
                        System.out.println("You will be placing your Destroyer (2 units Long)");
                        length =2;
                        break;
                }
                //etner valid x input
                System.out.print("Enter the x Coordinate: ");
                column = Integer.parseInt(Input.integerInput())-1;

                while (!(column<=9&&column>=0)) {
                    System.out.print("Please enter a valid input: ");
                    column = Integer.parseInt(Input.integerInput())-1;
                }

                //input valid y input
                System.out.print("Enter the y Coordinate: ");
                row = Integer.parseInt(Input.integerInput())-1;
                while (!(row<=9&&row>=0)) {
                    System.out.print("Please enter a valid input: ");
                    row = Integer.parseInt(Input.integerInput())-1;
                }
                System.out.print("Enter the Direction; Up(1), Right(2), Down(3), Left(4): ");
                direction = Integer.parseInt(Input.integerInput());
                //makes sure that the user will enter a valid number
                while (!(direction == 1 || direction == 2 || direction == 3 || direction == 4)) {
                    System.out.println("Please enter a valid input: ");
                    System.out.print("Enter the Direction; Up(1), Right(2), Down(3), Left(4): ");
                    direction = Integer.parseInt(Input.integerInput());

                }
            }while (!canPlaceShip(length, column, row, direction, true)) ;

                //place ships on array
                for (int i = 0; i < length; i++) {
                    if(i!=0) {
                        if (direction == 1) {
                            row--;
                        } else if (direction == 2) {
                            column++;
                        } else if (direction == 3) {
                            row++;
                        } else {
                            column--;
                        }
                    }
                    switch (j) {
                        case 0:
                            playerBoard[row][column] = "A";
                            break;
                        case 1:
                            playerBoard[row][column] = "B";
                            break;
                        case 2:
                            playerBoard[row][column] = "S";
                            break;
                        case 3:
                            playerBoard[row][column] = "C";
                            break;
                        case 4:
                            playerBoard[row][column] = "D";
                            break;
                    }
                }
                displayPlayerBoard();
            }



    }
    public static boolean canPlaceShip(int length, int x , int y, int direction, boolean player){
        //variable setup
        boolean canPlace =true;
        String num;
        int y1,x1;
        int way = direction;
        x1 = x;
        y1 = y;

        try {
            //look for values of ship of that length for that direction
            for (int i = 0; i < length; i++) {
                if(i!=0) {
                    if (way == 1) {
                        y1--;
                    } else if (way == 2) {
                        x1++;
                    } else if (way == 3) {
                        y1++;
                    } else {
                        x1--;
                    }
                }
                if(player) {
                    //check for array out of bounds
                    playerBoard[y1][x1] = playerBoard[y1][x1];
                    //check if value isn't empty
                    if(playerBoard[y1][x1].equals("-")) {
                        canPlace = true;
                    }
                    else{
                        canPlace = false;
                    }

                }
                else{
                    cpuBoard[y1][x1] = cpuBoard[y1][x1];
                    //check if value isn't empty
                    if(cpuBoard[y1][x1].equals("-")) {
                        canPlace = true;
                    }
                    else{
                        canPlace = false;
                    }


                }


            }

        } catch(ArrayIndexOutOfBoundsException e){
            //if value does not exist
            canPlace = false;

        }

        //inform player
        if(player && !canPlace){
            System.out.println("Invalid placement. Try Again.");
        }

        return canPlace;
    }
    public static void placeCpuShip(){
        int row=0, column=0, direction =0, length = 0;
        String ship ="";
        boolean canPlace = false;

        for(int j=0;j<5;j++) {
            canPlace = false;
            //tell user what ship they will be placing
            switch (j) {
                case 0:
                    ship = "A";
                    length =5;
                    break;
                case 1:
                    ship = "B";
                    length =4;
                    break;
                case 2:
                    ship = "S";
                    length =3;
                    break;
                case 3:
                    ship = "C";
                    length =3;
                    break;
                case 4:
                    ship = "D";
                    length =2;
                    break;
            }

            while(!canPlace){
                //randomize variables
                row = (int)(Math.random()*10);
                column = (int)(Math.random()*10);
                direction = (int)(Math.random()*4)+1;
                canPlace = canPlaceShip(length, column, row, direction, false);
            }
            //adjust ship placement
            for (int i = 0; i< length; i++) {
                if (i != 0) {
                    if (direction == 1) {
                        row--;
                    } else if (direction == 2) {
                        column++;
                    } else if (direction == 3) {
                        row++;
                    } else {
                        column--;
                    }
                }
                cpuBoard[row][column] = ship;

                displayCPUBoard();
            }
        }
    }
    public static void saveFile(){
        //variable setup
        BufferedWriter out;
        File CurrFile = new File("Save.txt");
        File temp_list = new File("TempSave.txt");

        try{
            out = new BufferedWriter(new FileWriter(temp_list));
            out.write("Player ships:" + "\n");
            //print out value of board
            for(int i =0;i<10;i++){
                for (int j = 0; j < 10; j++) {
                    System.out.println(playerBoard[i][j]);
                    out.write(playerBoard[i][j]);
                }
                out.write("\n");
            }

            out.write("CPU ships:" + "\n");
            //print out value of board
            for(int i =0;i<10;i++){
                for (int j = 0; j < 10; j++) {
                    out.write(cpuBoard[i][j]);
                }
                out.write("\n");
            }

            out.write("Player shots:" + "\n");
            //print out value of board
            for(int i =0;i<10;i++){
                for (int j = 0; j < 10; j++) {
                    out.write(playerBoardShot[i][j]);
                }
                out.write("\n");
            }

            out.write("CPU shots:" + "\n");
            //print out value of board
            for(int i =0;i<10;i++){
                for (int j = 0; j < 10; j++) {
                    out.write(cpuBoardShot[i][j]);
                }
                out.write("\n");
            }

            //delete and rename files
            out.close();
            CurrFile.delete();
            temp_list.renameTo(CurrFile);
        }
        catch(IOException e){
            System.out.println("error with " + e);
        }

    }


}
