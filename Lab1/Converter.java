import java.io.File;
import java.io.IOException;

public class Converter {
	/**
	 * Convert between currencies.
	 * @param Takes in three string arguments.
	 * @result Returns true or false.
	 */
	public static void main(String[] args) throws IOException {
		// Opens and reads the currency file.
		Currencies currencies = CurrenciesReader.open(new File("valutor.txt"));
		
		// Check if the number of input arguments is correct.
		if(args.length != 2 && args.length != 3) {
			System.out.println("Usage: Converter <currency> <value> [<to_currency>]");
			System.exit( -1 );
		}
		
		// Try and make currency record object using the input values.
		CurrencyRecord from = currencies.get(args[0]);
		CurrencyRecord to = currencies.get(args.length == 3 ? args[2] : "SEK");
		// Errors in case the currency was not recognized from the file.
		if(from == null) {
			System.out.println(args[0] + " is not a recognized currency.");
			System.exit( -1 );		
		}
		if(to == null) {
			System.out.println(args[2] + " is not a recognized currency.");
			System.exit( -1 );
		}
		
		// Tries to convert the second input argument to a double and prints an error if it fails.
		// Otherwise prints out the result using the convert function.
		try {
			double value = Double.valueOf(args[1]);  
			System.out.print(value + " " + from.currency + " = ");
			System.out.printf("%.2f", convert(value, from, to));
			System.out.println(" " + to.currency);
		} catch(NumberFormatException exc) {
			System.out.println("Second argument must be of type 'double'.");
			System.exit( -1 );
		}
		
	}

	public static double convert(double v, CurrencyRecord from, CurrencyRecord to) {
	/**
	 * Converts between currencies.
	 * @param a value as double and two objects from and to as CurrencyRecords.
	 * @result Returns the converted value as double.
	 */
		return v * from.rate / to.rate;
	}
}
