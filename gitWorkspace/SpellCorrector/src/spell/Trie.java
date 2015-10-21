package spell;

public class Trie implements ITrie 
{
	// Fields
	int wordCount;
	int nodeCount;
	Node root;
	
	StringBuilder result;
	
	// Constructor
	public Trie()
	{
		wordCount = 0;
		nodeCount = 1;
		root = new Node();
	}
	
	// Methods
	@Override
	public void add(String word) 
	{
		word = word.toLowerCase();
		root.recAdd(word, 0);
		
		// TEST
		//System.out.println("nodeCount: " + getNodeCount());
	}
	
	@Override
	public INode find(String word) 
	{
		word = word.toLowerCase();
		return root.recFind(word, 0);
	}
	
	@Override
	public int getWordCount() 
	{
		return wordCount;
	}

	@Override
	public int getNodeCount() 
	{
		return nodeCount;
	}

	@Override
	public String toString()
	{
		result = new StringBuilder();
		StringBuilder current_word = new StringBuilder();
		root.word_finder(root, current_word);
		return result.toString();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeCount;
		result = prime * result + wordCount;
		return result;
	}
	
	@Override
	public boolean equals(Object o)
	{
		// IF 'o' is me...
		if(o == this)
		{
			return true;
		}
		// IF 'o' is null..
		else if(o == null)
		{
			return false;
		}
		//IF 'o' is NOT the same type as me...
		else if(o.getClass() != this.getClass())
		{
			return false;
		}
		
		Trie t = (Trie) o;
		
		// IF nodeCounts are NOT equal...
		if(nodeCount != t.nodeCount)
		{
			return false;
		}
		// IF wordCounts are NOT equal...
		else if(wordCount != t.wordCount)
		{
			return false;
		}
		
		// RECURSIVELY compare each node in both Tries
		return root.recEquals(t.root);
	}
	
	/**
	 *	NODE class
	 */
	
	public class Node implements ITrie.INode
	{
		// Fields
		int frequencyCount = 0;
		
		Node[] myChildren = new Node[26];

		
		// Class Methods
		
		@Override		
		public int getValue()
		{
			return frequencyCount;
		}

		public void recAdd(String word, int currentLetter)
		{
			//System.out.println(getNodeCount());
			
			char c = word.charAt(currentLetter);
			
			// General Case:
			if(myChildren[c-'a'] == null)
			{
				myChildren[c-'a'] = new Node();
				nodeCount++;
				//System.out.println(getNodeCount());
			}
			
			// Base Case: hit last character in word
			if(currentLetter == word.length()-1)
			{
				// IF the word has never before been added to the trie... 
				// THEN...INCREMENT the number of UNIQUE words within the trie
				if(frequencyCount == 0)
				{
					wordCount++;
				}
				// Also, INCREMENT the number of times that this word occurs in the trie 
				//frequencyCount++;
				
				//myChildren[c-'a'] = new Node();
				myChildren[c-'a'].frequencyCount++;
				//nodeCount++;
				//System.out.println(getNodeCount());
			}
			
			// IF this isn't the last character in the word... RECURSE
			else
			{
				myChildren[c-'a'].recAdd(word, 1+currentLetter);
			}
			return;
		}

		public Node recFind(String word, int currentLetter)
		{
			Node n = null;
			
			
			// IF you HAVE reached the end of the word...
			if(currentLetter == word.length()/*-1*/ )
			{
				// IF the word EXISTS...
				// (we're supposed to return the last node of the existing word)
				if(frequencyCount > 0)
				{
					n = this;
				}
				
				// IF the word does NOT EXISTS... 
				// RETURN null
				else
				{
					n = null;
				}
				
				return n;
			}
			
			char c = word.charAt(currentLetter);
			
			// FIRST, test that char c is a valid ascii character
			if(c < 97 || c > 122)
			{
				System.out.println("ERROR: Invalid char!");
				System.exit(-1);
			}
			
			// IF the location for the letter that you are trying find is null,
			// RETURN null (per the program specifications)
			if( myChildren[c-'a'] == null)
			{
				return n;
			}
			
			// IF you have NOT reached the end of the word...
			// RECUSIVELY look for the next letter of the word
			else if(currentLetter < word.length()/*-1*/)
			{
				n = myChildren[c-'a'].recFind(word, ++currentLetter);
			}
			
			return n;
		}

		public void word_finder(Node current_node, StringBuilder current_word)
		{
			/*  MID node: 				( O--> )
			 *  MID-END-WORD node:		( X--> )
			 *  LEAF node: 				( -->X )
			 */
			
			for(int i = 0; i < myChildren.length; i++)
			{	
				// if current index is NOT empty...
				if(current_node.myChildren[i] != null)
				{
					// MID node case:
					// IF the current_node has a freq_count of 0...
					if(current_node.myChildren[i].getValue() == 0)
					{
						// ...then APPEND the current_node letter and RECURSE
						char c = (char) (97 + i);
						current_word.append(c);
						word_finder(current_node.myChildren[i], current_word);
					}
					
					
					// MID-END-WORD node case:
					// IF the current_node has a freq_count > 0...
					if(current_node.myChildren[i].getValue() > 0)
					{
						char c = (char) (97 + i);
						current_word.append(c);
						
						//System.out.println("current_word: " + current_word.toString());
						
						result.append(current_word.toString() + "\n");
						word_finder(current_node.myChildren[i], current_word);
					}
				}
			}
			// EVERY TIME we RETURN, we need to REMOVE A LETTER from current_word
			if(current_word.length() > 0)
			{
				current_word.deleteCharAt(current_word.length()-1);
			}
		}

		// PENDING
		public boolean recEquals(Node n)
		{
			for(int i = 0; i < myChildren.length; i++)
			{
				if(n.myChildren[i] == null && myChildren[i] == null)
				{
					return true;
				}
				else if(n.myChildren[i].frequencyCount != myChildren[i].frequencyCount)
				{
					return false;
				}
				else
				{
					// recursive section
					return myChildren[i].recEquals(n.myChildren[i]);
				}
			}
			return true;
		}
	}
}
