import java.io.*;
import java.util.*;

/**
 * Huffman tree class
 *
 * @author Javad Amin
 * @version 2017-02-24
 *
 */
class HuffmanTree implements Comparable {
	private boolean[] value;
	private Integer weight;
	private HuffmanTree left, right;

	/**
	* Constructor:
	* Tree(value) -> new tree with no children (no left and right subtree).
	* Argument `value` must be an array of booleans.
	*/
	public HuffmanTree(boolean[] value, Integer weight) {
		this.value = value;
		this.weight = weight;
		this.left = null;
		this.right = null;
	}
	
	/**
	* Creates a tree with weight of zero.
	*/
	public HuffmanTree(boolean[] value) {
		this(value, 0);
	}

	/**
	* Creates a tree from two subtrees.
	*/
	public HuffmanTree(HuffmanTree left, HuffmanTree right) {
		value = null;
		weight = left.getWeight() + right.getWeight();   // The weight is sum of the subtrees.
		this.left = left;
		this.right = right;
	}
	
	/**
	* Creates a tree by reconstructing it from a savefile, opened with a
	* BitFileReader.
	*/
	protected HuffmanTree( BitFileReader bfr ) throws IOException {
		// Read a bit, it it's true then it's a parent node.
		if( bfr.readBit() ) {
			left = new HuffmanTree( bfr );         // Read the left subtree.
			right = new HuffmanTree( bfr );       // Read the right subtree.
			value = null;                        // Parent node has no value.
			weight = 0;                         // Weight is irrelevant now.
		} else {                               // Else it's a leaf.
			left = null;
			right = null;
			value = bfr.readByte();         // Read an entire byte as the value.
			weight = 0;
      }
	}
	
	/**
	* Traverse the tree depending on a boolean value.  
	* True returns the right subtree, false the left.
	*/
	public HuffmanTree pickSubtree( Boolean b ) {
		return ( b ? right : left );
	}

	/**
	* Retrieve the value of this node.
	*/
	boolean[] getValue() {
		return this.value;
	}
	
	/**
	* Retrieve the weight of this node.
	*/
	Integer getWeight() {
		return this.weight;
	}
	
	/**
	* Whether this node has a value.
	*/
	public Boolean hasValue() {
		return this.value != null;
	}
	
	/**
	* Saves this tree to an opened BitFileWriter. 
	*/
	public void saveToBuffer( BitFileWriter bfw ) throws IOException {
	  if( this.hasValue() ) {        	           // If it is a leaf (has a value)
			bfw.writeBit(false);                  // then write a 0
			boolean[] myByte = this.getValue();  // then write the Byte representing
			bfw.writeByte(myByte);              // the value, character.
			
		} else {
			bfw.writeBit(true);             // otherwise it's a parent node, write 1
			left.saveToBuffer(bfw);          // then the left subtree
			right.saveToBuffer(bfw);        // and right subtree.
		}
	}

	/**
	* Opens a file and Saves this tree to a it. Not used anymore.
	*/
	public void saveToBuffer( String filename ) throws IOException {
		File htFile = new File(filename);
		BitFileWriter bfw = new BitFileWriter( htFile );

		saveToBuffer( bfw );
		bfw.close();
	}
	
	/**
	* Compares the weight of this tree to another.
	*/
	public int compareTo( Object t ) {
		HuffmanTree tr = (HuffmanTree) t;
		return this.getWeight().compareTo( tr.getWeight() );
	}
	

	/**
	* Builds a dictionary from huffman code, adding it to dic.
	* dicBlocks is the code that's been built so far block by block.
	*/
	private void buildDictionary( HashMap<boolean[], Boolean[]> dic, ArrayList<Boolean> dicBlocks) {
		Boolean[] helper = new Boolean[0];  // empty Boolean array, to make toArray() work.
		if( this.isLeaf() ) {              // If this is a leaf, add the value to the dictioanry.
			dic.put( getValue(), dicBlocks.toArray( helper ) );
			return;
		}

		Integer last = dicBlocks.size();

		// Else add 0 and do it for the left branch.
		dicBlocks.add( false );
		left.buildDictionary( dic, dicBlocks);

		// Then change the last to 1 and do it for the right.
		dicBlocks.set( last, true );
		right.buildDictionary( dic, dicBlocks);

		// Lastly remove the new entry to make the recursion work.
		dicBlocks.remove( last.intValue() );
	}

	/**
	* Returns a dictionary from huffman code.
	*/
	public HashMap<boolean[], Boolean[]> getDictionary() {
		HashMap<boolean[], Boolean[]> dic = new HashMap<boolean[], Boolean[]>();
		ArrayList<Boolean> dicBlocks = new ArrayList<Boolean>();

		buildDictionary( dic, dicBlocks);

		return dic;
	}

	public String toString() {
		HashMap<boolean[], Boolean[]> dic  = getDictionary();
		String ret = "";

		for( Map.Entry<boolean[], Boolean[]> entry : dic.entrySet() ) {
			BoolByteToInt bbti = new BoolByteToInt();
			int ByteDigit = bbti.convert(entry.getKey());
			
			ret += (char)ByteDigit + ": ";
			for( Boolean b : entry.getValue() )
				ret += b ? "1" : "0";
			ret += "\n";
		}
		return ret;
	}
	

	
	
	

	// Extra functions which might come handy sometimes! (Not used currently)

	
	/**
	* Set the left subtree to `child` (must be a HuffmanTree).
	*/
	void setLeft(HuffmanTree child) {
		this.left = child;
	}
	
	/**
	* Set the right subtree to `child` (must be a HuffmanTree).
	*/
	void setRight(HuffmanTree child) {
		this.right = child;
	}

	
	/**
	* Returns `True` if this node has a left child/subtree, `False` otherwise.
	*/
	boolean hasLeft() {
		return this.left != null;
	}
	
	/**
	* Returns `True` if this node has a right child/subtree, `False` otherwise.
	*/
	boolean hasRight() {
		return this.right != null;
	}

	/**
	* Retrieve the left subtree of this node.
	*/
	HuffmanTree getLeft() {
		return this.left;
	}

	/**
	* Retrieve the right subtree of this node.
	*/
	HuffmanTree getRight() {
		return this.right;
	}	
	
	/**
	* Returns the depth height of the binary tree.
	*/
	int treeHeight() {
		int leftHeight = 0;
		int rightHeight = 0;
		if (this.hasLeft()) {
			leftHeight = 1 + this.getLeft().treeHeight();
		}
		
		if (this.hasRight()) {
			rightHeight = 1 + this.getRight().treeHeight();
		}
		
		return  Math.max(leftHeight, rightHeight);
	}
	
	/**
	* Returns if a node is a leaf or not.
	*/
	boolean isLeaf() {
		if (!this.hasLeft() & !this.hasRight()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	* Returns the numbers of leafs a tree contains.
	*/
	int leafCounter() {
		int leftLeafs = 0;
		int rightLeafs = 0;
		
		if (this.hasLeft()) {
			if (this.getLeft().isLeaf()) {
				leftLeafs = 1 + this.getLeft().leafCounter();
			} else {
				leftLeafs = 0 + this.getLeft().leafCounter();
			}
		}
		
		if (this.hasRight()) {
			if (this.getRight().isLeaf()) {
				rightLeafs = 1 + this.getRight().leafCounter();
			} else {
				rightLeafs = 0 + this.getRight().leafCounter();
			}
		}
		
		return  leftLeafs + rightLeafs;
	}

	/**
	* Checks if two binary trees have the same content.
	*/
	boolean isSame(HuffmanTree cTree) {
		
		if ( this.getValue().equals(cTree.getValue())) {
			
			if (this.hasLeft() & cTree.hasLeft() ) {
				if ( !this.getLeft().isSame( cTree.getLeft() ) ) {
					return false;
				}
			} else if ( this.hasLeft() & !cTree.hasLeft() || !this.hasLeft() & cTree.hasLeft() ) {
					return false;
				}

			
			if ( this.hasRight() & cTree.hasRight() ) {
				if ( !this.getRight().isSame( cTree.getRight() ) ) {
					return false;
				}
			} else if ( this.hasRight() & !cTree.hasRight()  ||  !this.hasRight() & cTree.hasRight() ) {
					return false;
				}
				
			return true;
		} else {
			return false;
		}
	}
	
	/**
	* Makes a deep copy of a tree returning a new copy of it.
	*/
	HuffmanTree copyTree() {
		HuffmanTree newHuffmanTree = new HuffmanTree(this.getValue(), this.getWeight());
		
		if (this.hasLeft()) {
			newHuffmanTree.setLeft( this.getLeft().copyTree() );
		}
		
		if (this.hasRight()) {
			newHuffmanTree.setRight( this.getRight().copyTree() );
		}
		return newHuffmanTree;
	}

}
