package spell;

import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	public static void main(String[] args) throws NoSimilarWordFoundException, IOException {
		
		String dictionaryFileName = args[0];
		String inputWord = args[1];
		
		ISpellCorrector corrector = new SpellCorrector();
		
		corrector.useDictionary(dictionaryFileName);
		String suggestion = corrector.suggestSimilarWord(inputWord);
		
		System.out.println("Suggestion is: " + suggestion);
	}

}
