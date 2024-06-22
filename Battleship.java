import java.util.*;
import java.io.*;

public class Battleship {
    //static and final variable setup
    static String [][]playerBoard, cpuBoard, playerBoardShot, cpuBoardShot;
    final static int BOARD_LENGTH = 10;

    static int difficulty;
    static boolean hit = false;
    static int hitX=0, hitY=0;
    static String file_Name;

    public static void startGame(int gameStatus){
        //variable setup
        boolean gameRunning = true;
        playerBoard = new String[BOARD_LENGTH][BOARD_LENGTH];
        cpuBoard = new String[BOARD_LENGTH][BOARD_LENGTH];
        playerBoardShot = new String[BOARD_LENGTH][BOARD_LENGTH];
        cpuBoardShot = new String[BOARD_LENGTH][BOARD_LENGTH];
        String filename;

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
            file_Name = "";
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
            //check for input to load board
            System.out.print("Enter filename to load the game: ");
            filename = Input.stringInput() + ".txt";
            file_Name = filename;
            numMoves = loadExistingGamePlayer(filename);
            numMovesCPU = loadExistingGameCPU(filename);
            displayPlayerShots();
        }

        while(gameRunning){
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
                System.out.println();
                switch(result){
                    case 0:
                        System.out.println("You have hit nothing ");
                        break;
                    case 1:
                        System.out.println("Bang! You have hit a Target");
                        break;
                    case 2:
                        System.out.println("BOOM! The Ship has been destroyed!");
                        gameRunning = !checkWin(1);
                        break;
                }
                System.out.println("Press Enter to Continue");
                Input.stringInput();

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
                System.out.println("Press Enter to continue: ");
                Input.stringInput();
                gameRunning = false;
            }


            if(gameRunning) {
                result = takeCpuShot(difficulty);
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
                        gameRunning = !checkWin(0);
                        break;
                }
            }
        }
    }

    public static int loadExistingGamePlayer (String saveFile){
        //varable setup
        String save = saveFile;
        int numMoves = 0;
        BufferedReader in;
        String line,filename;

        try{
            //setup buffered reader and read lines
            in = new BufferedReader(new FileReader(save));
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
            System.out.println("An error occurred while loading the game.");
            System.out.print("Enter filename again to load the game: ");
            filename = Input.stringInput() + ".txt";
        }
        return numMoves;

    }

    public static int loadExistingGameCPU (String saveFile){
        //varable setup
        int numMovesCPU = 0;
        BufferedReader in;
        String line;

        try{
            //setup buffered reader and read lines
            in = new BufferedReader(new FileReader(saveFile));

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
                displayPlayerShots();

                result = 0;
            } else {
                //when player hits a ship
                ship = cpuBoard[row][column];
                cpuBoard[row][column] = "X";
                cpuBoardShot[row][column] = "X";
                displayPlayerShots();

                //if player finishes a specific ship
                if (!shipExists(ship,1)) {
                    switch(ship){
                        case"A":
                            System.out.println("The Aircraft Carrier has been Struck Down");
                            break;
                        case"D":
                            System.out.println("The Destroyer has been Struck Down");
                            break;
                        case"C":
                            System.out.println("The Cruiser has been Struck Down");
                            break;
                        case"B":
                            System.out.println("The Battleship has been Struck Down");
                            break;
                        case"S":
                            System.out.println("The Submarine has been Struck Down");
                            break;
                    }
                    result = 2;
                }
                //if player hits but doesn't destroy the ship
                else{
                    result = 1;
                }

            }
        }
        else if (board==0){
            //if cpu misses its shot
            if (playerBoard[row][column].equals("-")) {
                playerBoard[row][column] = "O";
                playerBoardShot[row][column] = "O";
                displayPlayerBoard();
                result = 0;
            } else {
                //if cpu hits a ship
                ship = playerBoard[row][column];
                playerBoard[row][column] = "X";
                playerBoardShot[row][column] = "X";

                displayPlayerBoard();
                //if CPu finishes a specific ship
                if (!shipExists(ship,0)) {
                    result = 2;
                    switch(ship){
                        case"A":
                            System.out.println("The Aircraft Carrier has been Struck Down");
                            break;
                        case"D":
                            System.out.println("The Destroyer has been Struck Down");
                            break;
                        case"C":
                            System.out.println("The Cruiser has been Struck Down");
                            break;
                        case"B":
                            System.out.println("The Battleship has been Struck Down");
                            break;
                        case"S":
                            System.out.println("The Submarine has been Struck Down");
                            break;
                    }
                }
                //when cpu doesn't finish the ship
                else {
                    result = 1;
                }

            }
        }
        return result;
    }

    public static int takePlayerShot(){
        //varable setup
        int row, column;
        int result;
        boolean cantakeShot=false;
        boolean invalidShot = false;
        do {
            displayPlayerShots();
            //if user has already chosen invalid shot
            if (invalidShot){
                System.out.println("You have chosen an invalid shot coordinate.");
            }
            else{
                System.out.println("It is your turn to take a shot");
            }
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
            if(!(cpuBoard[row][column].equals("O")||cpuBoard[row][column].equals("X"))){
                cantakeShot = true;
            }
            else{
                invalidShot = true;

            }

        }while(!cantakeShot);

        //take shot
        System.out.println("I can take shot :" + cantakeShot);
        result = takeShot(column, row  , 1);

        return result;
    }
    public static int takeCpuShot(int difficulty){
        //varable setup
        int result =0;
        int row = 0, column =0;
        boolean canTakeShot = false, validShot = false;
        int shotDistance =1;
        int hitOrientation = 0;
        int hitCounter =0;


        if(difficulty==0) {
            do {
                //take shot for ai easy difficulty

                row = (int) (Math.random() * 10);
                column = (int) (Math.random() * 10);


                //check for valid shot
                if (!(playerBoard[column][row].equals("O")) || !(playerBoard[column][row].equals("X"))) {
                    canTakeShot = true;
                } else {
                    System.out.println("Invalid Shot");
                }

            } while (!canTakeShot);
        }
        else if(difficulty==1){
            if(hit){
                //get coordinates of last hit
                column  = hitX;
                row = hitY;
                while(!validShot) {
                    do {
                        //search for area around last hit coordinate
                        hitOrientation = (int) (Math.random() * 4) + 1;
                        if (hitOrientation == 1) {
                            row-=shotDistance;
                        } else if (hitOrientation == 2) {
                            column+=shotDistance;
                        } else if (hitOrientation == 3) {
                            row+=shotDistance;
                        } else {
                            column-=shotDistance;
                        }
                        hitCounter++;
                        //upgrade distance changed if enough shots are taken
                        if(hitCounter>20){
                            shotDistance++;
                        }
                    } while (!(row >= 1 && row <= 10 && column >= 1 && column <= 10));
                    if (!(playerBoard[row][column]=="X"||playerBoard[row][column]=="-"||playerBoard[row][column]=="O")){
                        validShot=true;
                    }
                }

            }
            else{
                do {
                    //take shot for ai normal difficulty when ai hasn't found a ship yet

                    row = (int) (Math.random() * 10);
                    column = (int) (Math.random() * 10);


                    //check for valid shot
                    if (!(playerBoard[column][row].equals("O")) || !(playerBoard[column][row].equals("X"))) {
                        canTakeShot = true;
                    } else {
                        System.out.println("Invalid Shot");
                    }

                } while (!canTakeShot);
            }
        }

        result = takeShot(column,row,0);
        if(result==1){
            hit = true;
            hitX = column;
            hitY = row;
        }
        else if (result==2) {
            hit=false;
        }
        return result;
    }

    public static boolean shipExists(String ship, int player){
        //variable setup
        boolean exists = false;

        //for player
        if(player==1){
            //searches for ship of the same type
            for (int i = 0; i < 10; i++) {
                for (int j= 0; j < 10; j++) {
                    if(cpuBoard[i][j].equals(ship)){
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
                    if (playerBoard[i][j].equals(ship)) {
                        exists = true;
                        break;
                    }
                }
            }
        }
        return exists;
    }


    public static boolean checkWin(int player){
        boolean win = true;

        if (player==0){
            for (int i = 0; i < 10; i++) {
                for (int j= 0; j < 10; j++) {
                    if(!((cpuBoard[i][j].equals("-"))||cpuBoard[i][j].equals("X")||cpuBoard[i][j].equals("O"))){
                        //if CPu wins
                        win=false;
                    }
                }
            }
            //displays only if cpu wins
            if(win){
                displayPlayerBoard();
                displayCPUBoard();
                System.out.println("You have Lost! Here are the Statuses of the two Boards: ");
            }
        }
        else if (player==1){
            for (int i = 0; i < 10; i++) {
                for (int j= 0; j < 10; j++) {
                    if(!((playerBoard[i][j].equals("-"))||playerBoard[i][j].equals("X")||playerBoard[i][j].equals("O"))){
                        //if player wins
                        win=false;

                    }
                }
            }
            //displays only if player wins
            if(win){
                displayPlayerBoard();
                displayCPUBoard();
                System.out.println("You Won!!! Here was the Status of both Boards: ");
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
                    if(!(playerBoard[y1][x1].equals("-"))) {
                        canPlace = false;
                    }

                }
                else{
                    cpuBoard[y1][x1] = cpuBoard[y1][x1];
                    //check if value isn't empty
                    if(!(cpuBoard[y1][x1].equals("-"))) {
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
            System.out.println();
            System.out.println("Invalid placement. Try Again.\n" );
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
        File CurrFile, temp_list;

        if(file_Name.equals("")) {
            //when user created his own new game
            System.out.print("Enter filename to save the game: ");
            file_Name = Input.stringInput() + ".txt";
        }

        CurrFile = new File(file_Name);
        temp_list = new File("temp.txt");

        try{
            out = new BufferedWriter(new FileWriter(temp_list));
            out.write("Player ships:" + "\n");
            //print out value of board
            for(int i =0;i<10;i++){
                for (int j = 0; j < 10; j++) {
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
