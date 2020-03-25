import java.io.File;
import java.io.IOException;

/**
 * Convert a boolean type Byte to int.
 */
public class BoolByteToInt {

	public int convert(boolean[] b) {
		int ByteDigit;
		String ByteDigitString = "";
			for (int i = 0; i < b.length; i++) {
				if (b[i] == true) {	
					ByteDigitString += "1"; 
				} else {
					ByteDigitString += "0"; 
				}
			}
		return ByteDigit = Integer.parseInt(ByteDigitString, 2);
	}
}
