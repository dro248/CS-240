package spell;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector 
{
	// Fields
	ITrie myTrie = new Trie();
	Set<String> edit_distance_1 = new TreeSet<String>();
	Set<String> edit_distance_2 = new TreeSet<String>();
	
	
	
	// Methods
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException 
	{
		/*
		 *  What we are going to do here is scan the words from the Dictionary file one-by-one
		 *  until every word has been input into the trie.
		 *  
		 *  1) While there are words in Dictionary, add word to Trie
		 *  2) Close Scanner object
		 */
		
		
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File(dictionaryFileName))));
			
			while(scanner.hasNext())
			{
				myTrie.add(scanner.next());
			}			
			scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException 
	{
		// IF word IS FOUND in trie...
		if(myTrie.find(inputWord) != null)
		{
			return inputWord.toLowerCase();
		}
		
		// STEP 1:
		// GENERATE the Set of all words Edit Distance 1 away from the 'inputWord'
		edit_distance_1 = generate_ED1_set(inputWord);
		
		//System.out.println(edit_distance_1.toString());
		
		// IF 'edit_distance_1' is a valid set...
		if(valid_set(edit_distance_1))
		{
			return get_similar_word(edit_distance_1);
		}

		// STEP 2:
		// GENERATE the Set of all words Edit Distance 2 away from the 'inputWord'
		edit_distance_2 = generate_ED2_set(edit_distance_1);
		
		// IF 'edit_distance_2' is a valid set...
		if(valid_set(edit_distance_2))
		{
			return get_similar_word(edit_distance_2);
		}
		
		// IF after the second iteration, we still cannot find a similar word, throw exception
		throw new NoSimilarWordFoundException();
	}
	
	
	
	
	
	
	
	
	
	/*
	 * PRIVATE FUNCTIONS
	 */
	
	
	// Generates a set of all of the similar words Edit Distance 1 from a word
	private Set<String> generate_ED1_set(String word)
	{
		 Set<String> tmpSet = new TreeSet<String>();
		
		// DELETION:
		if(word.length() > 1)
		{
			for(int i = 0; i < (word.length()); i++)
			{
				StringBuilder sb = new StringBuilder(word);
				sb.deleteCharAt(i);
				
				tmpSet.add(sb.toString());
			}
		}
		
		// TRANSPOSITION:
		if(word.length() > 1)
		{
			for(int i = 0; i < (word.length()-1); i++)
			{
				char[] myWord = word.toCharArray();
				char tmp = myWord[i];
				myWord[i] = myWord[i+1];
				myWord[i+1] = tmp;
				String str = new String(myWord);
				tmpSet.add(str);
			}
		}
		
		// ALTERATION:
		{
			for(int i = 0; i < word.length(); i++)
			{
				char[] myWord = word.toCharArray();
				for(char c = 'a'; c <= 'z'; c++)
				{
					myWord[i] = c;
					String str = new String(myWord);
					tmpSet.add(str);
				}
			}
		}
		
		// INSERTION:
		{			
			for(int i = 0; i < (word.length()+1); i++)
			{
				StringBuilder sb = new StringBuilder(word);
				
				for(char c = 'a'; c <= 'z'; c++)
				{
					sb.insert(i, c);
					tmpSet.add(sb.toString());
					sb.deleteCharAt(i);
				}
			}
		}
		return tmpSet;
	}
	
	// GENERATE a set containing ALL of ED2(word) values
	private Set<String> generate_ED2_set(Set<String> ED1_set)
	{
		for( String s : ED1_set )
		{
			edit_distance_2.addAll(generate_ED1_set(s));
		}
		
		//System.out.println("ED1 Size: " + edit_distance_1.size());
		//System.out.println("ED2 Size: " + edit_distance_2.size());
		
		if(edit_distance_1.equals(edit_distance_2))
		{
			System.out.println("ERROR!!!");
		}
		
		return edit_distance_2;
	}
	
	// RUNS a find on EVERY element in a given set... RETURNS word with HIGHEST frequencyCount
	private String get_similar_word(Set<String> my_set)
	{
		String bestWord = "";
		int highest_freq = 0;
		
		// ITERATE thru all of the strings our set to find the highest freq
		for( String s : my_set )
		{
			// IF word is found in trie AND that word's frequency is higher than the current, UPGRADE...
			if(myTrie.find(s) != null && myTrie.find(s).getValue() > highest_freq)
			{
				bestWord = s;
				highest_freq = myTrie.find(s).getValue();
			}
		}

		return bestWord;
	}
	
	//  VALID SET: A valid set is a non-empty set where SOME VALUE is found in trie
	private boolean valid_set(Set<String> mySet)
	{
		// No point running FIND on empty set...RETURN false
		if(mySet.size() < 1)
		{
			return false;
		}
		
		for( String s : mySet )
		{
			//System.out.println(s);
			
			if(myTrie.find(s) != null)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString()
	{
		System.out.println(myTrie.toString());
		
		return null;
	}
}
