import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;


class CurrenciesReader {
	/**
	 * Reads a file contaciting currency code, rate and description.
	 * @param Takes in a file as parameter.
	 * @result Returns an object of the class Currencies.
	 */
	public static Currencies open(File file) throws IOException {
		// Reads numbers (in English format) from a UTF-8 encoded file.
		Scanner sc = new Scanner(file, "UTF-8");
		sc.useLocale(Locale.ENGLISH);
		Currencies currencyMap = new Currencies();
		while (sc.hasNextLine()) {
			String currency = sc.next();
			double rate = sc.nextDouble();
			String description = sc.nextLine();
			CurrencyRecord rec = new CurrencyRecord();
			rec.currency = currency;
			rec.rate = rate;
			rec.description = description;
			currencyMap.put(currency, rec);
			// System.out.println(currency + " " + rate + " " + description);
		}
		return currencyMap;
	}
}
