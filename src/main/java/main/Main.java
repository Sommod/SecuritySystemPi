package main;

import java.util.Scanner;

import com.pi4j.Pi4J;

import main.one.ProjectOneA;
import main.one.ProjectOneB;
import main.three.Manager;

/**
 * Main class of the Project
 * @author Josh Moore
 * @author Ben Kanter
 *
 */
public class Main {
	// Numbers of the Programs
	private static final int programs[] = {-1, 1, 2, 3};
	
	public static void main(String[] args) {
		
		// Main Class for Entire Pi4J usage. This must be the first object created
		var context = Pi4J.newAutoContext();
		
		// Variables
		int userInput = -1;
		String getter = "";
		Scanner scan = new Scanner(System.in);
		
		// Loop for getting user input of program or exit number
		while(userInput == -1) {
			System.out.print("Project 1-1: 1\nProject 1-2: 2\nFinal Project: 3\nExit: -1\nEnter in Project to run: ");
			getter = scan.next();
			
			try {
				userInput = Integer.parseInt(getter);
				
				if(!isProgram(userInput)) {
					System.out.println("Notice: The given number is NOT supported, please enter a program number");
					userInput = -1;
				}
				
			} catch(ArithmeticException  e) {
				System.out.println("Notice: The inputted value is NaN. Please enter a number");
			}
		}
		
		// Runs the Appropriate program based on user input
		switch(userInput) {
			case 1:
				new ProjectOneA(context);
				break;
			case 2:
				new ProjectOneB(context);
				break;
			case 3:
				new Manager(context);
				break;
			case -1:
				System.out.println("Exiting Program...");
				break;
			default:
				System.err.println("Unknown Error occurred");
				break;
		}
		
		scan.close();
		context.shutdown();
	}
	
	/**
	 * Method for checking if the inputed value is a program number.
	 * 
	 * @param input - User Inputed value
	 * @return True - If program number exits
	 */
	private static boolean isProgram(int input) {
		if(input == -1)
			return true;
		
		for(int i : programs)
			if(input == i)
				return true;
		
		return false;
	}
}
