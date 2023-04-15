import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BinaryDataConverter {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner inputReader = new Scanner(System.in);
        Scanner lineCounter = null;
        Scanner splitter = null;
        FileWriter outputWriter = null;
        File input = null;

        System.out.println("Enter your input file name (input): ");
        String inputFileName = inputReader.nextLine();

        try {
            input = new File("./" + inputFileName + ".txt");
            lineCounter = new Scanner(input);
            splitter = new Scanner(input);
        } catch (FileNotFoundException e) {
            System.out.println("Input file is not found.");
        }

        try {
            outputWriter = new FileWriter("./output.txt");
        } catch (IOException e) {
            System.out.println("Output file is not found.");
        }

        System.out.println("Enter your byte ordering type (l or b): ");
        String byteOrdering = inputReader.nextLine();

        System.out.println("Enter your data type (float, int or unsigned): ");
        String dataType = inputReader.nextLine();

        System.out.println("Enter the size of data (1, 2, 3 or 4):");
        int dataSize = inputReader.nextInt();

        int lineCount = 0;

        while (lineCounter.hasNextLine()) {
            lineCount++;
            if (lineCounter.nextLine() == null) {
                break;
            }
        }

        String[] dataInCurrentLine = new String[lineCount * 12];
        while (splitter.hasNextLine()) {
            //get the current line
            String currentLine = splitter.nextLine();
            dataInCurrentLine = currentLine.split(" ");

            // convert from hexadecimal to decimal
            switch (dataType) {

                // nael ve said
                case "float":
    
                    break;
    
                // karagul
                case "int":
    
                    break;
    
                // kadir
                case "unsigned":
    
                    break;
    
                default:
                    break;
            }
        }

        inputReader.close();
        lineCounter.close();
        outputWriter.close();
    }
}