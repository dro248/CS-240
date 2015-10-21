package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		
		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		
		// TEST INPUTS
		if(wordLength < 2)
		{
			System.out.println("Usage: java className dictionary wordLength guesses");
			System.out.println("ERROR: wordLength cannot be less than 2.");
			System.exit(0);
		}
		else if(guesses < 1)
		{
			System.out.println("Usage: java className dictionary wordLength guesses");
			System.out.println("ERROR: guesses cannot be less than 1.");
			System.exit(0);
		}

		// CREATE INSTANCE OF GAME & SET GAME VARIABLES
		EvilHangmanGame myGame = new EvilHangmanGame();
		myGame.setGuesses(guesses);
		myGame.startGame(dictionary, wordLength);
		System.out.println("myDictionary initial size: " + myGame.getDictSize());			// TESTING
	
		// CREATE Scanner for taking in user input
		Scanner scanner = new Scanner(System.in);
		
		
		
		
		
		// PLAY GAME until out of guesses
		while(myGame.getGuesses() > 0)
		{
			/*  - Prompt User with game prompt
			 *  - make sure that user input is valid
			 *  - call makeGuess()
			 */
			
			System.out.println();
			System.out.println();
			System.out.println("Chances left: " + myGame.getGuesses());
			System.out.println("Used letters: " + myGame.get_previousGuesses());
			System.out.println("Word: " + myGame.ANSWER);
			System.out.print("Enter guess: ");
			
			String line = scanner.nextLine();
			
			if(line.length() > 1 || line.length() < 1 )
			{
				reject();
				continue;
			}
			
			char guess =line.charAt(0);
			
			
			try {
				
				// Invalid Input Test
				if(!valid_char(guess))
				{
					reject();
					continue;
				}
				
				// ...VALID input. Carry on!
				myGame.makeGuess(guess);
			} 
			catch (GuessAlreadyMadeException e) 
			{
				// Reprompt
				System.out.println("You already guessed that!");
			}
			
			if(myGame.getDictSize() < 2 && myGame.get_occurrencesOf("-", myGame.ANSWER) == 0)
			{
				win(myGame.ANSWER);
				break;
			}
		}
		
		
		if(myGame.getDictSize() > 1)
		{
			lose(myGame.ANSWER);
		}
		System.out.println("myDictionary size: " + myGame.getDictSize());
		scanner.close();
	}
	
	public static void reject()
	{
		System.out.println("Invalid Input!");
	}
	
	public static boolean valid_char(char c)
	{
		if(!Character.isLetter(c))
		{
			return false;
		}
		
		return true;
	}
	
	public static void win(String word)
	{
		System.out.println("You WIN! The word was " + word);
	}
	
	public static void lose(String word)
	{
		System.out.println("You lose! The word was " + word);
	}

}
