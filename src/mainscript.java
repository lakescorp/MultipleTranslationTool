import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import txtutils.translator_main_multiple;
import xmlutils.translationandroidstudioorientatted;

public class mainscript {
	// General variables
	static String inputPath = "programfiles/input.txt"; // Path of the input file
	static String lenguagePath = "programfiles/LenguageCodesToBeTranslated.txt"; // Path of the input file
	static String outputPath = "programfiles/translations/"; // Path of the output path
	static int waitime = 3000; // Waittime between translations

	public static void main(String[] args) throws IOException, InterruptedException, ParserConfigurationException {
		int input= -1;
		Scanner myObj = new Scanner(System.in); // Create scanner
		do {

			System.out.println("What do you want to translate?");
			System.out.println("1 - A xml");
			System.out.println("2 - A csv");
			System.out.println("3 - A txt");
			
			System.out.print("Select a number: ");


			int custominput = myObj.nextInt();
			switch(custominput) {
			
			case 1: translationandroidstudioorientatted.main(); System.out.println(""); break;
			case 2: csvutils.translator_main_csv.main(); System.out.println(""); break;
			case 3: translator_main_multiple.main(); System.out.println(""); break;
			default: input = -1; break;
			
			}


		} while (input == -1);
		
		myObj.close();
	}

}
