import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class BinaryDataConverter {

    public static void printHashMap(HashMap<String, String> outputNumber, int rowCounter, int dataSize,
            FileWriter outpuWriter) throws IOException, FileNotFoundException {
        for (int i = 1; i <= rowCounter; i++) {
            for (int j = 1; j <= 12 / dataSize; j++) {
                System.out.print(outputNumber.get(i + ":" + j) + " ");
                try {
                    outpuWriter.write(outputNumber.get(i + ":" + j) + " ");
                } catch (FileNotFoundException e) {
                    System.out.println("Output file is not found.");
                } catch (IOException e) {
                    System.out.println("Output file is not found.");
                }
            }
            System.out.println();
            try {
                outpuWriter.write("\n");
            } catch (FileNotFoundException e) {
                System.out.println("Output file is not found.");
            } catch (IOException e) {
                System.out.println("Output file is not found.");
            }
        }
    }

    public static String hexNumbers(char hex) {
        String bin = "";
        switch (hex) {
            case '0':
                bin = "0000";
                break;
            case '1':
                bin = "0001";
                break;
            case '2':
                bin = "0010";
                break;
            case '3':
                bin = "0011";
                break;
            case '4':
                bin = "0100";
                break;
            case '5':
                bin = "0101";
                break;
            case '6':
                bin = "0110";
                break;
            case '7':
                bin = "0111";
                break;
            case '8':
                bin = "1000";
                break;
            case '9':
                bin = "1001";
                break;
            case 'a':
                bin = "1010";
                break;
            case 'b':
                bin = "1011";
                break;
            case 'c':
                bin = "1100";
                break;
            case 'd':
                bin = "1101";
                break;
            case 'e':
                bin = "1110";
                break;
            case 'f':
                bin = "1111";
                break;
            default:
                break;
        }
        return bin;
    }

    public static String hexToBin(String hex) {
        String bin = "";
        for (int i = 0; i < hex.length(); i++) {
            bin += hexNumbers(hex.charAt(i));
        }
        return bin;
    }

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

        int rowCounter = 0;
        HashMap<String, String> inputNumber = new HashMap<String, String>();
        HashMap<String, String> outputNumber = new HashMap<String, String>();
        String[] dataInCurrentLine = new String[12];

        while (splitter.hasNextLine()) {
            // get the current line
            String currentLine = splitter.nextLine();
            dataInCurrentLine = currentLine.split(" ");

            String currentString = "";
            rowCounter++;

            for (int i = 0; i < dataInCurrentLine.length; i++) {
                currentString += dataInCurrentLine[i];

                if (currentString != null && currentString != "" && (i + 1) % dataSize == 0) {
                    inputNumber.put(rowCounter + ":" + (i + 1) / dataSize, currentString);
                    currentString = "";
                }

            }

        }

        // convert from hexadecimal to decimal
        switch (dataType) {

            // nael ve said
            case "float":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= 12 / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);

                    }
                }
                break;

            // karagul
            case "int":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= 12 / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);

                    }
                }

                break;

            // kadir
            case "unsigned":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= 12 / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);

                    }
                }
                break;

            default:
                break;
        }
        // print the output
        // printHashMap(outputNumber, rowCounter, dataSize, outputWriter);

        // close scanners
        inputReader.close();
        lineCounter.close();
        outputWriter.close();
    }

}
