import java.io.*;

/**
 * Reads one bit at a time from a file.
 *
 * @author Steffan Nillson
 * @Edited Javad Amin
 * @version 2017-02-24
 */
public class BitFileReader {
	private BufferedInputStream in;
	private int currentByte; // -1 if no more data
	private int bitPos;      // position in currentByte

	/**
	* Creates a BitFileReader by opening a connection to an actual file,
	* the file named by the File object file in the file system.
	*/
	public BitFileReader(File file) throws IOException {
		in = new BufferedInputStream(new FileInputStream(file));
		currentByte = in.read();
		bitPos = 7;
	}

	/** Returns true if this reader has another bit in its input. */
	public boolean hasNextBit() {
		return currentByte != -1 && in != null;
	}

	/** Reads a single bit. */
	public Boolean readBit() throws IOException {
		int res = (currentByte>>bitPos) & 1;
		--bitPos;
		if (bitPos < 0) {
			currentByte = in.read(); // -1 if end of file has been reached
			bitPos = 7;
		}
		if (res == 1) {
			return true;
		} else { 
			return false;
		}
	}
	
	/** Reads a whole byte and returns it as an array of booleans. */
	public boolean[] readByte() throws IOException {
		boolean[] b = new boolean[8];	             // Array of read Bytes
				
		for (int i = 0; i < 8; i++) {
			b[i] = this.readBit();
		}
		return b;
	}

	/** Closes this reader. */
	public void close() throws IOException {
		if (in != null) {
			in.close();
		}
	}

	/**
	 * Unit test.
	 * Usage: java -ea BitFileReader
	 */
	public static void main(String[] args) throws IOException {
		File temp;
		BitFileReader reader;
	
		// Empty file
		temp = File.createTempFile("testBitFileReaderEmpty", "txt");
		temp.deleteOnExit();

		reader = new BitFileReader(temp);
		assert !reader.hasNextBit();       
		reader.close();

		// File containing "AB"
		temp = File.createTempFile("testBitFileReaderAB", "txt");
		temp.deleteOnExit();
		BufferedWriter out = new BufferedWriter(new FileWriter(temp));  
		out.write("AB"); // AB = 0100 0001 0100 0010
		out.close();

		reader = new BitFileReader(temp);
		assert reader.hasNextBit();
		assert reader.readBit() == false;
		assert reader.hasNextBit();
		assert reader.readBit() == true;
		assert reader.readBit() == false;
		assert reader.readBit() == false;

		assert reader.readBit() == false;
		assert reader.readBit() == false;
		assert reader.readBit() == false;
		assert reader.hasNextBit();
		assert reader.readBit() == true;

		assert reader.hasNextBit();
		assert reader.readBit() == false;
		assert reader.readBit() == true;
		assert reader.readBit() == false;
		assert reader.readBit() == false;

		assert reader.readBit() == false;
		assert reader.readBit() == false;
		assert reader.readBit() == true;
		assert reader.hasNextBit();
		assert reader.readBit() == false;
		assert !reader.hasNextBit();
		reader.close();
	}
}
