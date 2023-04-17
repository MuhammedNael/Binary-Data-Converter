import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class BinaryDataConverter {
    final static int HEX_NUMBER = 12;

    public static void printHashMap(HashMap<String, String> outputNumber, int rowCounter, int dataSize,
            FileWriter outpuWriter) throws IOException, FileNotFoundException {
        for (int i = 1; i <= rowCounter; i++) {
            for (int j = 1; j <= HEX_NUMBER / dataSize; j++) {
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

    public static String littleEndianHex(String hex) {
        String littleEndian = "";
        for (int i = hex.length() - 1; i >= 0; i--) {
            littleEndian += hex.charAt(i);
        }
        return littleEndian;
    }

    public static String unsigned(String currentBin) {
        long currrentInt = 0;
        for (int i = 0; i < currentBin.length(); i++) {
            char c = currentBin.charAt(i);
            int digit = Character.getNumericValue(c);
            currrentInt += digit * Math.pow(2, currentBin.length() - 1 - i);
        }
        return Long.toString(currrentInt);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner inputReader = new Scanner(System.in);
        Scanner splitter = null;
        FileWriter outputWriter = null;
        File input = null;

        System.out.println("Enter your input file name (input): ");
        String inputFileName = inputReader.nextLine();

        try {
            input = new File("./" + inputFileName + ".txt");
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
        String[] dataInCurrentLine = new String[HEX_NUMBER];

        while (splitter.hasNextLine()) {
            // get the current line
            String currentLine = splitter.nextLine();
            dataInCurrentLine = currentLine.split(" ");
            // if the byte ordering is little endian, reverse the order of the hex numbers
            if (byteOrdering.equalsIgnoreCase("l")) {
                for (int i = 0; i < HEX_NUMBER; i += dataSize) {
                    int k = dataSize + i;
                    for (int j = i; j < i + dataSize / 2; j++) {
                        String temp = dataInCurrentLine[j];
                        dataInCurrentLine[j] = dataInCurrentLine[k - 1];
                        dataInCurrentLine[k - 1] = temp;
                        k--;
                    }
                }
            }

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
                    for (int j = 1; j <= HEX_NUMBER / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);
                        // convert bin to float

                        // put the output to the outputNumber hashmap

                    }
                }
                break;

            // karagul
            case "int":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= HEX_NUMBER / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);
                        String testNaN = "10002000000000000000000000000000";
                        // convert bin to int

                        // put the output to the outputNumber hashmap
                        // Convert binary string to unsigned integer
                        char signBit = testNaN.charAt(0);
                        long tMin = 0;
                        long positivePart = 0;
                        long twos;
                        // most significant bit için negatiflik kısmı

                        if (signBit == '1') {
                            tMin = -(long) Math.pow(2, testNaN.length() - 1);
                            // düzgün değer veriyor mu dene bakalım
                        } else if (signBit == '0') {
                            tMin = 0;
                        } else {
                            outputNumber.put(i + ":" + j, "NaN");
                            System.out.println(
                                    "NAN " + testNaN + " cevabımız NaN: " + positivePart);
                            break;
                        }

                        for (int k = 1; k < testNaN.length(); k++) {
                            char c = testNaN.charAt(k);
                            if (c == '1') {
                                positivePart += Math.pow(2, testNaN.length() - 1 - k);

                            } else if (c == '0') {
                                positivePart += 0;
                            } else {
                                outputNumber.put(i + ":" + j, "NaN");
                                System.out.println(
                                        "NAN " + testNaN + " cevabımız NaN: " + positivePart);
                                break;
                            }

                        }
                        twos = tMin + positivePart;

                        // - sonsuz kısımlarına filan fıstık bakacağız
                        if (twos < -2147483647) {
                            outputNumber.put(i + ":" + j, "-∞");
                            System.out.println(
                                    "NEGATİF " + testNaN + " cevabımız NEGATİF sonsuz: " + twos);
                            break;
                        } else if (twos > 2147483647) {
                            outputNumber.put(i + ":" + j, "∞");
                            System.out.println(
                                    "POSİTİF " + testNaN + " cevabımız POSİTİF sonsuz: " + twos);
                            break;
                        }

                        else {
                            outputNumber.put(i + ":" + j, Long.toString(twos));
                            System.out.println(
                                    "SIKINTISIZ" + testNaN + " cevabımız SIKINTISIZ : " + twos);
                        }

                        // Print the unsigned integer value
                        System.out
                                .println(" verilen binarymiz bu olsun " + testNaN + " cevabımız da bu: "
                                        + (twos));
                    }
                }

                break;

            // kadir
            case "unsigned":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= HEX_NUMBER / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);

                        // Convert binary string to unsigned integer
                        outputNumber.put(i + ":" + j, unsigned(currentBin));

                    }
                }
                printHashMap(outputNumber, rowCounter, dataSize, outputWriter);

                break;

            default:
                break;
        }
        // print the output
        // printHashMap(outputNumber, rowCounter, dataSize, outputWriter);

        // close scanners
        inputReader.close();
        outputWriter.close();
    }

}
