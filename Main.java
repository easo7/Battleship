/*
 Programmer: Eason Tu
 Program name: Main.java
 Last Modified: 04/06/2024
 Description: Main program for user main menu and intialize game
 */

public class Main {


    /* void main
  A method to call all needed methods and programs

    Log:
   06/04/2024 - Eason Tu:
   Created the main structure and flow of the method, implemented and tested

 */
    public static void main(String[] args){
        boolean run=true;
        //run menu options until user wants to quit
        while(run) {
            run = mainMenu(Integer.parseInt(mainMenuInput()));
        }
    }


    /* void mainMenu
      A method to start new game, old game, read instructions, or quit program based on user Input

        Log:
       06/04/2024 - Eason Tu:
       Created the main structure and flow of the method, prompting for input, and raising exceptions in case of error

     */
    public static boolean mainMenu(int returnValue){
        boolean run = true;
        //runs option based on userinput
        switch(returnValue){
            case 1:
                Battleship.startGame(0);
                break;
            case 2:
                Battleship.startGame(1);
                break;
            case 3:
                instructions();
                break;
            case 4:
                System.out.println("Have a good Day!");
                run = false;
                break;
        }
        return run;
    }




    /* String mainMenuInput
   A method to accept a string input for user to select option for main menu

     Log:
    06/04/2024 - Eason Tu:
    Created the main structure and flow of the method, prompting for input, and raising exceptions in case of error

  */
    public static String mainMenuInput(){
        //variable setup
        String returnValue = "";
        boolean validNum =false;

        System.out.println("Welcome to Battleship!");
        do {
            //receive user Input for menu options
            System.out.print("Type 1 to start a new game, 2 to load previous game, 3 to see Instructions, or 4 to quit the program: ");
            returnValue = Input.integerInput();

            //cancels loop after proper input
            if (returnValue.equals("1") ||returnValue.equals("2")||returnValue.equals("3")||returnValue.equals("4")){
                validNum = true;
            }
        }while(!validNum);

        //return menu option
        return returnValue;
    }



    /* void instructions
   A method to inform user of instructions

     Log:
    06/04/2024 - Eason Tu:
    Created the main structure and flow of the method, prompting for input

  */
    public static void instructions(){

        //Lots of text, broken up into chunks that the user has to press a button to continue
        System.out.println(
                """
                        
                        The game board consists of two grids:
                        Ships Board: Shows your ship placements.
                        Shots Board: Displays your shots (hits, misses, and opponent’s ships).
                        Rows and columns are labeled 1 to 10.
                        """
        );
        System.out.print("Press Enter to continue: ");
        Input.stringInput();

        System.out.println(
                """
                        
                        Placing Your Ships:
                        Before the game begins, you’ll place your ships on the board.
                        Each ship can be oriented either horizontally or vertically.
                        Ensure that your ship placements are valid.
                        """
        );
        System.out.print("Press Enter to continue: ");
        Input.stringInput();

        System.out.println(
                """
                        
                        Taking Your Turn:
                        On your turn, select a row and column to target.
                        If your selection is invalid (e.g., outside the board or already tried), you’ll receive an error message.
                        Your shot will reveal if you have hit a ship or nothing. Hit all parts of a ship to sink it.                        
                        """
        );
        System.out.print("Press Enter to continue: ");
        Input.stringInput();

        System.out.println(
                """
                        
                        Computer’s Turn:
                        The computer will take random shots if you choose “easy” difficulty.
                        For “normal” difficulty, it aims around the last hit until it sinks a ship or exhausts nearby squares.
                        When a ship is sunk, you’ll see messages like “Computer has sunk your Battleship!”
                        """
        );
        System.out.print("Press Enter to continue: ");
        Input.stringInput();

        System.out.println(
                """
                        
                        The game ends when either all your ships or the computer’s ships are sunk.
                        The winner (you or the computer) will be declared.
                        If you lose, the computer’s ships board will be revealed.
                        """
        );
        System.out.println("Press Enter to continue: ");
        Input.stringInput();

    }

}
