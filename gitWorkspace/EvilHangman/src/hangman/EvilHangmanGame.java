package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.*;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

	public String ANSWER;
	private int GUESSES; 																		// Number of guesses (given by the PLAYER)
	private int LENGTH; 																		// Length of word (given by the PLAYER)
	private String currentLetter;
	public TreeSet<String> myDictionary = new TreeSet<String>();								// Contains all the words in the dictionary of length LENGTH
	private TreeSet<String> previousGuesses = new TreeSet<String>();							// Contains all PREVIOUS GUESSES (made by PLAYER)
	private Map<String, TreeSet<String>> wordGroups = new TreeMap<String, TreeSet<String>>();	// Contains all of the different types of word groups after partition
	
	// GETTERS AND SETTERS
	public int getGuesses() 								{ return GUESSES;}
	public void setGuesses(int g) 							{ GUESSES = g;	 }
	public int getLength() 									{ return LENGTH; }
	public void setLength(int s) 							{ LENGTH = s; }
	public String get_currentLetter()						{ return currentLetter;	}
	public void set_currentLetter(String s)					{ currentLetter = s; 	}
	public TreeSet<String> get_previousGuesses() 			{ return previousGuesses; }
	public int getDictSize()								{ return myDictionary.size(); }

	//////////////////////////////////
	//								//
	//		INTERFACE METHODS 		//
	//								//
	//////////////////////////////////	
	
	// SETUP phase
	@Override
	public void startGame(File dictionary, int wordLength) 
	{
		/*	"set up the game... don't start it"
		 * 
		 * - TEST: dictionary is valid file
		 * - Scan Dictionary for words of wordLength into TreeSet
		 * 		- make sure that word in Dictionary matches [a-zA-Z]+  (scanner.hasNext("[a-zA-Z]+"))
		 * 			- if bad (escape)...ERROR: bad dictionary
		 * 			- if GOOD... add to TreeSet
		 */
		setLength(wordLength);

		if(dictionary.isFile() && dictionary.canRead())
		{
			parse(dictionary);
		}
		
		ANSWER = generate_empty_pattern();
	}

	// GAMEPLAY phase
	// Returns the new best partition for 'myDictionary'
	@Override
	public TreeSet<String> makeGuess(char guess) throws GuessAlreadyMadeException 
	{		
		// clear MAP
		wordGroups.clear();
		String myGuess = "";

		myGuess = "" + guess;
		myGuess = myGuess.toLowerCase();
		set_currentLetter(myGuess);
		
			
		// 1) if 'guess' has already been guessed... throw exception
		if(already_guessed(myGuess))
		{
			throw new GuessAlreadyMadeException();
		}
		previousGuesses.add(myGuess);
		
		// 2) Partition!		
		partition(myGuess);
//		System.out.println(wordGroups.keySet());
		
		

		// 3) Choose Set
		String biggestSetKey = get_biggest_set_key();
		myDictionary = wordGroups.get(biggestSetKey);
		
		// 4) Generate ANSWER
		generate_answer(biggestSetKey);
		
		// 5) Decrement GUESSES	
		if(get_occurrencesOf(currentLetter, biggestSetKey) < 1)
		{
			GUESSES--;
		}
		
		return myDictionary;
	}
	
	
	
	//////////////////////////////////
	//								//
	//		PERSONAL METHODS 		//
	//								//
	//////////////////////////////////


	public void partition(String myGuess)
	{
		//////////////////////////
		//		PARTITION 		//
		//////////////////////////
		
		// For every string in 'myDictionary'
		for(String s : myDictionary)
		{
			
			if(s.contains(myGuess))
			{
				// Generate a pattern containing the location of char 'myGuess' in 's'  (e.g. myGuess = 'a', s = "tart" | pattern = "-a--")
				String my_pattern = generate_word_pattern(myGuess, s);
				// Add word to the wordGroup it belongs to... OR - if this pattern has not yet been seen, create new key and add this word to that list
				if(!wordGroups.containsKey(my_pattern))
				{
					// Create Key
					TreeSet<String> myWords = new TreeSet<String>();
					wordGroups.put(my_pattern, myWords);
				}
				// Add word 's' to respective pattern
				wordGroups.get(my_pattern).add(s);
			}
			else
			{
				String empty_pattern = generate_empty_pattern();
				
				// If this is the first occurrence of a word NOT containing 'myGuess', create key and add to list
				if(!wordGroups.containsKey(empty_pattern))
				{
					// Create Key
					TreeSet<String> myWords = new TreeSet<String>();
					wordGroups.put(empty_pattern, myWords);
				}
				// Add word 's' to Zero-Occurrence wordGroup
				wordGroups.get(empty_pattern).add(s);
			}
		}
	}
	
	@SuppressWarnings("resource")
	public boolean parse(File d)
	{
		/* PURPOSE:
		 * 	- IF a given dictinary d is parsable, then it will be parsed into a set.
		 *  - ELSE, this function will stop and the program will terminate.
		 */
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(d)));

			while(scanner.hasNext("[a-zA-Z]+"))
			{
				String myWord = scanner.next("[a-zA-Z]+");
				if(myWord.length() == LENGTH)
				{
					myDictionary.add(myWord);
//					System.out.println(myWord);
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean already_guessed(String myGuess)
	{
		for(String s: previousGuesses)
		{
			if(s.equals(myGuess))
			{
				return true;
			}
		}
		return false;
	}
		
	public String generate_empty_pattern()
	{
		StringBuilder sb = new StringBuilder();
		// Add to list of No-Occurrence group
		for(int i = 0; i < LENGTH; i++)
		{
			sb.append('-');
		}
		return sb.toString();
	}

	public String generate_word_pattern(String guess, String myWord)
	{
		StringBuilder sb = new StringBuilder(myWord);
		
		for(int i = 0; i < sb.length(); i++)
		{
			if(sb.charAt(i) != guess.charAt(0))
			{
				sb.setCharAt(i, '-');
			}
		}
		
		return sb.toString();
	}
	
	public String get_biggest_set_key()
	{
		int biggest_group = 0;
		String biggest_key = "";
		
		for(String key : wordGroups.keySet() )
		{
			int groupSize = wordGroups.get(key).size();
			
			if(biggest_group < groupSize)
			{
				biggest_group = groupSize;
				biggest_key = key;
			}
			else if(biggest_group == groupSize)
			{
				biggest_key = get_best_set_key(biggest_key, key);
			}
		}
		
		return biggest_key;
	}
	
	// This returns the key to the Better of 2 sets
	public String get_best_set_key(String key_a, String key_b)
	{
		// TEST 1: IF key is the "No-Occurrence" set... return it
		if(key_a.equals(generate_empty_pattern()))
		{
			return key_a;
		}
		if(key_b.equals(generate_empty_pattern()))
		{
			return key_b;
		}
		
		// TEST 2: Choose the key with fewest occurrences of myGuess in it 
		if(get_occurrencesOf(currentLetter, key_a) < get_occurrencesOf(currentLetter, key_b))
		{
			return key_a;
		}
		if(get_occurrencesOf(currentLetter, key_b) < get_occurrencesOf(currentLetter, key_a))
		{
			return key_b;
		}
		
		// Choose key with Rightmost Letters
		return get_rightmost_key(key_a, key_b);
	}

	public int get_occurrencesOf(String c, String word)
	{
		int counter = 0;
		for( int i = 0; i < word.length(); i++ ) 
		{
		    if( word.charAt(i) == c.charAt(0) ) 
		    {
		        counter++;
		    }
		}
		
		return counter;
	}
	
	private String get_rightmost_key(String key_a, String key_b)
	{
//		char[] a = key_a.toCharArray();
//		char[] b = key_b.toCharArray();
		
		for(int i = key_a.length()-1; i >= 0; --i)
		{
			if(key_a.charAt(i) == currentLetter.charAt(0) && key_b.charAt(i) != currentLetter.charAt(0))
			{
				return key_a;
			}
			else if(key_b.charAt(i) == currentLetter.charAt(0) && key_a.charAt(i) != currentLetter.charAt(0))
			{
				return key_b;
			}
		}

		return key_a;
	}

	private void generate_answer(String key)
	{
		StringBuilder sb = new StringBuilder(ANSWER);
		
//		System.out.println("key: " + ANSWER);
//		System.out.println("old ANSWER: " + ANSWER);
		
		for(int i = 0; i < ANSWER.length(); i++)
		{
			if(key.charAt(i) != '-')
			{
				sb.setCharAt(i, key.charAt(i));
			}
		}
		
		ANSWER = sb.toString();
//		System.out.println("new Answer: " + ANSWER);
	}
}