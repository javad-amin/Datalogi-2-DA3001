import java.io.*;

/**
 * Writes one bit at a time to a file.
 *
 * @author Javad Amin
 * @version 2017-02-24
 */
public class BitFileWriter {
	private BufferedOutputStream out;
	private int currentByte; // -1 if no more data
	private int bitPos;      // position in currentByte

	/**
	* Creates a BitFileWriter by opening a connection to an actual file,
	* the file named by the File object file in the file system.
	*/
	public BitFileWriter(File file) throws IOException {
		out = new BufferedOutputStream(new FileOutputStream(file));
		currentByte = 0;
		bitPos = 7;
	}

	/**
	* Creates a BitFileWriter by opening a connection a stream buffer.
	*/
	public BitFileWriter(OutputStream outs) throws IOException {
		out = new BufferedOutputStream(outs);
		currentByte = 0;
		bitPos = 7;
	}

	/** Writes a single bit. */
	public void writeBit(Boolean bit) throws IOException {
		currentByte <<= 1; // Shift to the left
		--bitPos;
		// Add 1 or 0 depending on the argument.
		
		
		if(bit) {
			currentByte |= 1;
		} else {
			currentByte |= 0;
		}
		
		// reset our internal values.
		if (bitPos < 0) {
			out.write(currentByte);
			currentByte = 0; 
			bitPos = 7;
		}
	}
	
	/** Writes a Byte boolean bit by bit. */
	public void writeByte(boolean[] b) throws IOException {

		for (int i = 0; i < 8; i++) {
				this.writeBit(b[i]);
		}
	}


	/**
	* Closes the file writer.  Always do this when you're done!
	*/
	public void close() throws IOException {
		if( bitPos != 7 ) {    // If there's anything left to write, write it.
			currentByte <<= (bitPos + 1);

			out.write(currentByte);
		}

		out.close();
	}
   
	/**
	 * Unit test.
	 * Usage: java -ea BitFileWriter
	 */
	public static void main(String[] args) throws IOException {
		File temp;
		BitFileReader reader;
		BitFileWriter writer;

	
		// Empty file
		temp = File.createTempFile("testBitFileWriterEmpty", "txt");
		temp.deleteOnExit();
		
		// AB = 0100 0001 0100 0010
		writer = new BitFileWriter(temp);
		writer.writeBit(false);
		writer.writeBit(true); 		
		writer.writeBit(false);
		writer.writeBit(false);
		
		writer.writeBit(false);
		writer.writeBit(false); 		
		writer.writeBit(false);
		writer.writeBit(true);
		
		writer.writeBit(false);
		writer.writeBit(true); 		
		writer.writeBit(false);
		writer.writeBit(false);
		
		writer.writeBit(false);
		writer.writeBit(false); 		
		writer.writeBit(true);
		writer.writeBit(false);
		writer.close();
		
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
