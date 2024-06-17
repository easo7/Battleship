/*
 Programmer: Eason Tu
 Program name: Input.java
 Last Modified: 04/06/2024
 Description: Program to accept input for String and Integer type, with only returning a string value
 */


import java.io.*;
import java.util.*;

public class Input {
/* String stringInput
   A method to accept a string input, with catches for exceptions. 

     Log:
    06/04/2024 - Eason Tu:
    Created the main structure and flow of the method, prompting for input, and raising exceptions in case of error

  */

    public static String stringInput(){
        String input = "";

        try{
            //variable setup
            Scanner scan = new Scanner(System.in);

            //reading  and returning variable
            input = scan.nextLine();

        }
        catch(InputMismatchException e){
            System.out.println("Error" + e);
        }
        return input;
    }


    /* integer integerInput
     A method to accept an integer input, with catches for exceptions.

     Log:
      06/04/2024 - Eason Tu:
      Created the main structure and flow of the method, prompting for input, and raising exceptions in case of error. If input is not an integer, will recursively call the method for proper input.
    */

    public static String integerInput (){
        //setup variables, taking the original string input using the method String input
        int intInput;
        String input = stringInput();

        try {
            intInput = Integer.parseInt(input);
        }
        //catch error if input was not an integer and rerun the method
        catch(NumberFormatException e){
            System.out.print("Invalid Integer. Please enter a valid Integer Input: ");
            input = integerInput();
        }
        return input;
    }
}