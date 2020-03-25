import java.util.*;
import java.io.*;

/**
 * Class to encode and decode files using Huffman tree coding.
 *
 * @author Javad Amin
 * @version 2017-02-24
 *
 */
public class Huffman {
   static String TREE_EXTENSION = ".ht";              // Extension for the tree file.
   static String ENCODED_EXTENSION = ".htencoded";   // Extension for the encoded file.
	
	/**
	* Encodes the given file, and saves the result in two files, a treefile
	* containing the Huffman tree associated to the file and the encoded file.
	*/
	static void HuffmanEncode(File file) throws IOException{
		// Reading the input file.
		BitFileReader in = new BitFileReader(file);
		
		// Frequency table
		HashMap<boolean[], Integer> charTable = new HashMap<boolean[], Integer>();
		Integer counter = 1;
		
		try {
			while(in.hasNextBit()) {             // Add to table as long as we can read
				boolean[] b = in.readByte();    // Array of read Bytes	
				
				if (FindSameElements(charTable, b) != null) { // if key already exists
					b = FindSameElements(charTable, b);      // Set the byte to existing array
					Integer isCounter = charTable.get(b);   // Get the value of counter
					counter = isCounter + 1; // set counter to the value and increment it            
				} else {                    // Otherwise set counter back to 1 for a new key.
					counter = 1;
				}
				charTable.put(b, counter); // Add to the table.
			} 
		}
		
		catch(IOException e) {
			throw(e);
		}
		finally {
			in.close();
		}
		
		// Now we create a Huffman tree forest tree for each key found, and store
		// those in a priority queue, which is kept sorted on the weight we get
		// from the frequency table.
		PriorityQueue<HuffmanTree> forest = new PriorityQueue<HuffmanTree>();
		for(boolean[] currentKey : charTable.keySet()){
			boolean[] currentValue = currentKey;                 // Get the key from table.
			Integer currentWeight = charTable.get(currentKey);  // Get it's weight, frequancy.
			forest.add(new HuffmanTree(currentKey, currentWeight)); // Add it to the tree forest.
		}
		
		// As long as the queue has more than two members, combine the two with
		// lowest weight into a new tree, and add it to the queue.
		while( forest.size() > 1 ) {
			HuffmanTree right = forest.poll(), left = forest.poll();
			forest.add( new HuffmanTree( left, right ) );
		}
		
		// If the queue is empty the file was empty.  Just quit.
		if( forest.size() == 0 )
			return;
		
		// Now we have the Huffman tree and we can Save it as a tree to a file.
		HuffmanTree hTree = forest.poll();
		hTree.saveToFile(file.getName() + TREE_EXTENSION);
		
		// Get the Huffman dictionary from the tree.
		HashMap<boolean[], Boolean[]> dic = hTree.getDictionary();
		
		// Count the total length that the encoded file will have.  This is the
		// sum of the product of each occurance of each character and the length
		// of its Huffman code.
		Integer totalLength = 0;
		
		// Iterate through HashMap entries(Key-Value pairs)
		Set entrySet = charTable.entrySet();
		Iterator it = entrySet.iterator();
		while(it.hasNext()){
			Map.Entry thisElement = (Map.Entry)it.next();
			
			totalLength += (Integer) thisElement.getValue() * dic.get(thisElement.getKey()).length;
		}
		
		// We need to pad the resultant file with a number of bits to make it an
		// even number of bytes.
		Integer padLength = 8 - totalLength % 8;
		
		// Open the file for reading again, now to encode each byte.
		in = new BitFileReader(file);

		// And a BitFileWriter to write the result.
		BitFileWriter codewriter;
		try {
			File encodedFile = new File(file.getName() + ENCODED_EXTENSION);
			codewriter = new BitFileWriter( encodedFile );
		}
		catch( IOException e ) {
			in.close();
			throw( e );
		}
		finally { }

		try {
			// First write the padding.
			for(Integer i = 1; i < padLength; i++)
			codewriter.writeBit(true);
			codewriter.writeBit(false);

			// Then read each byte from the input file again, and write the bits
			// it's associated with in the dictionary to the output file.
			while(in.hasNextBit()) {
				boolean[] b = in.readByte();
				// Set the boolean Array of byte to the existing array with same elements in our frequency charTable.
				b = FindSameElements(charTable, b); 
				for(Boolean bit : dic.get(b))
					codewriter.writeBit(bit);
			}
		}
		catch( IOException e ) {
			throw( e );
		}
		finally {
			codewriter.close();
		 in.close();
		}
	}
	
	/**
	* Decodes a Huffman encoded file codefile with regards to the Huffman treefile,
	* and writing the decoded file to outputfile.
	*/
	static void HuffmanDecode(File treefile, File encodedfile, String outputfile) throws IOException {
		// First open the tree file in a BitFileReader.
		BitFileReader bfr = new BitFileReader(treefile);
		HuffmanTree hTree;

		try {
			hTree = new HuffmanTree(bfr);  // Reconstruct the tree using the BFR constructor.
		}
		catch( IOException e ) {
			throw(e);
		}
		finally {
			bfr.close();
		}

		// Next, open the encoded file for reading.
		bfr = new BitFileReader(encodedfile);

		try {
			// Read the padding: keep reading bits until the first 0 is encountered.
			while( bfr.hasNextBit() && bfr.readBit() );
		}
		catch( IOException e ) {
			bfr.close();
			throw( e );
		}

		// Open the output file for writing.
		BufferedOutputStream outp;

		try {
			outp = new BufferedOutputStream(new FileOutputStream(outputfile));
		}
		catch(SecurityException e) {
			bfr.close();
			throw(e);
		}
		catch(IOException e) {
			bfr.close();
			throw(e);
		}
		finally { }

		HuffmanTree currrentTree = hTree;

		try {
			while( bfr.hasNextBit() ) {
				// Keep reading bits from the encoded file, and traverse the tree
				// according to the value.
				currrentTree = currrentTree.pickSubtree(bfr.readBit());

				// If we get to a leaf, output the value of the leaf, and start from
				// the beginning.
				if(currrentTree.hasValue()) {
					// Convert the array of boolean to int
					BoolByteToInt bbti = new BoolByteToInt();
					int ByteDigit = bbti.convert(currrentTree.getValue());
					
					outp.write(ByteDigit);
					currrentTree = hTree;
				}
			}
		}
		catch( IOException e ) {
			throw(e);
		}
		finally {
			outp.close();
			bfr.close();
		}
	}
	
	/*
	* Function which checks if if a given boolean array has same elements as
	* a boolean array saved as a key in a Hashmap returning the existing element.
	*/
	static boolean[] FindSameElements(HashMap<boolean[], Integer> hMap, boolean[] b) {
		// Getting a Set of Key-value pairs
		Set entrySet = hMap.entrySet();
		
		// Obtaining an iterator for the entry set
		Iterator it = entrySet.iterator();
		
		// Iterate through HashMap entries(Key-Value pairs)
		outerloop:
		while(it.hasNext()){
			Map.Entry me = (Map.Entry)it.next();
			
			boolean[] currentKeyElement = new boolean[8];
			currentKeyElement = (boolean[]) me.getKey();
			
			// If any of the elements are not the same then we continue from the top. 
			for (int i = 0; i < 8; i++) {
				if ( b[i] != currentKeyElement[i]) {
					continue outerloop;
				}
			}
			// Otherwise we return the Boolean array as it has the same elements.
			return currentKeyElement;
		}
		// If comparision gave no hit we return null
		return null;
	}
		/**
		* main()
		* If one argument given we encode the file provided.
		* If three arguments given we decode a file from tree file and encoded file, into
		* an output file.
		*/
		public static void main( String[] args ) {
		if( args.length != 1 && args.length != 3 ) {
			System.out.println( "Usage - encoding: java Huffman <filename>" );
			System.out.println( "Usage - decoding: java Huffman <tree file name> <encoded file name> <output filename>" );
			System.exit( 0 );
			return;
		}

		if( args.length == 1 ) {            // Encoding.
			try {
				File enFile = new File(args[0]);
				HuffmanEncode( enFile );
				System.out.println( "Encoding done." );
			}
			catch( FileNotFoundException e ) {
				System.out.println( "Couldn't find file." );
			}
			catch( SecurityException e ) {
				System.out.println( "Permission denied." );
			}
			catch( IOException e ) {
				System.out.println( "IO error." );
			}
			finally { }
		} else {  	                            // Decoding.
			try {
				File treefile = new File(args[0]);
				File encodedfile = new File(args[1]);
				HuffmanDecode( treefile, encodedfile, args[2] );
				System.out.println( "Decoding done." );
			}
			catch( FileNotFoundException e ) {
				System.out.println( "Couldn't find file." );
			}
			catch( SecurityException e ) {
				System.out.println( "Permission denied." );
			}
			catch( IOException e ) {
				System.out.println( "IO error." );
			}
			finally { }
		}
	}
}
