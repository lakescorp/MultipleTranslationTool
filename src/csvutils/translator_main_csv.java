package csvutils;
import com.darkprograms.speech.translator.GoogleTranslate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class translator_main_csv {
	static String[][] input; // List with the inputs
	static ArrayList<String> alllenguages = new ArrayList<String>(); // List with all lenguages that are going to be translated
	static String outputfilename = "translation-"; // Path of the filename
	static ArrayList<String> textToTranslate = new ArrayList<String>();;
	static String[][] finalText;

	
	//Common variables
	static String inputPath = "programfiles/input.txt"; // Path of the input file
	static String lenguagePath = "programfiles/LenguageCodesToBeTranslated.txt"; // Path of the input file
	static String outputPath = "programfiles/translations/"; // Path of the output path
	static int waitime = 3000; // Waittime between translations

	 public static void main() throws IOException, InterruptedException {

		/* Ask for lenguage path */

		Scanner myObj = new Scanner(System.in);  

		Boolean invalidEntry = false;
		do {
			System.out.println("The default lenguage path is \""+lenguagePath+"\"");
			System.out.println("Is your lenguage file in the default path?(Y/n)");

			String customlenguage = myObj.next().toLowerCase();  

			if(customlenguage.matches("[Nn]")) { // If they want a different path

				System.out.println("Please introduce your custom lenguage path");
				String tmp = myObj.next();

				if(fileExists(tmp)) {
					lenguagePath = tmp;
					invalidEntry = false;
					System.out.println("Your custom lenguage path is: " + lenguagePath);
				}else {
					invalidEntry = true;
				}
			}
		}while(invalidEntry);

		alllenguages(); // Add to the list all lenguages

		/* Ask for input path */

		invalidEntry = false;

		do {
			System.out.println("The default input path is \""+inputPath+"\"");
			System.out.println("Is your input file in the default path?(Y/n)");

			String custominput = myObj.next().toLowerCase();  

			if(custominput.matches("[Nn]")) { // If they want a different path

				System.out.println("Please introduce your custom input path");

				String tmp = myObj.next();	         
				if(fileExists(tmp)) {
					inputPath = tmp;
					invalidEntry = false;
					System.out.println("Your custom input path is: " + inputPath);
				}else {
					invalidEntry = true;
				}
			}
		}while(invalidEntry);
		getCSVDimensions(); // Read the input file*/

		/* Ask for output path */

		System.out.println("The default output path is \""+outputPath+"\"");
		System.out.println("Do you want this default path?(Y/n)");

		String customoutput = myObj.next().toLowerCase();  // Read user input
		if(customoutput.matches("[Nn]")) {
			System.out.println("Please introduce your custom output path");

			outputPath = myObj.next();

			System.out.println("Your custom output path is: " + outputPath);
		}

		/* Ask for outputfilename */

		System.out.println("The default namefile is \""+outputfilename+"(lenguage).txt\"");
		System.out.println("Do you want this default filename?(Y/n)");

		String customfilename = myObj.next().toLowerCase();  // Read user input
		if(customfilename.matches("[Nn]")) {
			System.out.println("Please introduce your custom filename");

			outputfilename = myObj.next();

			System.out.println("Your custom filename is: " + outputfilename);
		}

		/* Ask for waitime */

		System.out.println("The default Waittime is \""+waitime+"\"");
		System.out.println("Do you want this recommended Waittime?(Y/n)");

		String waitTime = myObj.next().toLowerCase();  // Read user input

		if(waitTime.matches("[Nn]")) {
			System.out.println("Please introduce your custom Waittime");

			waitime = myObj.nextInt();

			System.out.println("Your custom waitime is: " + waitime);
		}

		myObj.close();

		readoriginfile();
		copyInputToFinal();
		getTextToTranslate();
		translateText();

		ArrayList<String> output = new ArrayList<String>(); 
		for(int x = 0; x < finalText.length; x++) {
			String line = "";
			for(int y = 0; y < finalText[0].length; y++) {
				line+=finalText[x][y]+";";		
			}
			output.add(line);
		}
		createFileIn_NIO(output); // Create file
		System.out.println("File created");
		System.out.println("Program finished working");

	}
	
	private static void copyInputToFinal() {
		for(int x = 0; x < input.length; x++) {
			for(int y = 0; y < input[0].length; y++) {
				finalText[x][y] = input[x][y];		
			}
		}
		
	}

	private static void translateText() throws IOException, InterruptedException {
		int temp = 2;
		//Start translations
		for(String lenguage: alllenguages) {
			ArrayList<String> texto = translatearray(textToTranslate,lenguage); // Create file
			int temp2 = 1;
			finalText[0][temp] = lenguage;
			for(String elem: texto) {
				finalText[temp2][temp] = elem;
				temp2++;
			}
			Thread.sleep(waitime); // Wait for next translation
			temp++;
		}
	}
	
	private static void getTextToTranslate() {
		for(int x = 1; x < input.length;x++) {
			textToTranslate.add(input[x][1]);
		}
	}

	private static void getCSVDimensions() {
		try (FileReader reader = new FileReader(inputPath);BufferedReader br = new BufferedReader(reader)) {
			// read line by line
			String line;
			int a = 0;
			int b = 0;
			while ((line = br.readLine()) != null) {
				if(a==0) {
					b = line.split(";").length;
				}
				a++;
			}
			input = new String[a][b];
			System.out.println("Original file readed");



		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	private static void readoriginfile() { // Read the input file

		try (FileReader reader = new FileReader(inputPath);BufferedReader br = new BufferedReader(reader)) {
			// read line by line
			String line;
			int c = 0;
			while ((line = br.readLine()) != null) {
				input[c] = line.split(";");
				c++;
			}
			System.out.println("Original file readed, "+ input.length + " lines were readed");
			finalText = new String[input.length][alllenguages.size()+2];
			System.out.println("Size of finale filas:" + finalText.length + " columnas:"+finalText[0].length);

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

	}

	public static String translatorfunction(String input, String finalidioma) throws IOException { // Translate element

		return  GoogleTranslate.translate(finalidioma, input);
	}

	static ArrayList<String> translatearray(ArrayList<String> textoATraducir2,String lenguage)  throws IOException // Array to be translated
	{
		ArrayList<String> temptranslation = new ArrayList<String>();
		for(String mo : textoATraducir2) {

			String temp = translatorfunction(mo, lenguage);
			temptranslation.add(temp);
		}

		return temptranslation;
	}

	static void createFileIn_NIO(ArrayList<String> lines)  throws IOException // Create and write file
	{
		new File(outputPath).mkdirs();
		Files.write(Paths.get(outputPath+outputfilename+".csv"),
				lines,
				StandardCharsets.UTF_16);
	}

	static void alllenguages() { // Read all lenguages

		try (FileReader reader = new FileReader(lenguagePath);
				BufferedReader br = new BufferedReader(reader)) {
			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				alllenguages.add(line);
			}
			System.out.println("Lenguage file readed, "+alllenguages.size()+ " lenguages added");

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	static Boolean fileExists(String path) {
		File tmpDir = new File(path);
		Boolean output = false;
		if(!tmpDir.isFile()) {
			System.out.println("Given path is not an existing file");
			output = false;
		}else if(!tmpDir.exists()) {
			System.out.println("File doesn't exist try again");
			output = false;
		}else {
			output = true;
		}
		return output;
	}

}
