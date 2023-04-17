import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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

    public static String littleEndianHex (String hex) {
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
    
    public static String addOneToFraction(String s) {
    	int carry = 1;
    	char[] addedOne = new char[s.length()];
    	for (int i = s.length() - 1; i >= 0; i--) {
    		int bit = Character.getNumericValue(s.charAt(i));
    		if (bit + carry == 2) {
    			addedOne[i] = '0';
    			carry = 1;
    		}
    		else if (bit + carry == 1) {
    			addedOne[i] = '1';
    			carry = 0;
    		}
    		else
    			addedOne[i] = '0';
    	}
    	if (carry == 1) {
    		addedOne[0] = '1';
    	}
    	String added = "";
    	for (int i = 0; i < addedOne.length; i++)
    		added += addedOne[i];
    	return added;
    }
    
    public static String binToFloat(String s, int byteDataSize) {
    	int bitDataSize = 8 * byteDataSize;
    	int expBits = byteDataSize * 2 + 2;
    	int signBit = 0, expValue = 0, E = 0, bias = 0;
    	double mantissa = 0;
    	

    	bias = (int) (Math.pow(2, expBits - 1) - 1);
    	signBit = s.charAt(0) == '1' ? 1 : 0;
    	
    	// calculating exponent value
    	for (int i = 1; i <= expBits; i++) {
    		int bit = Character.getNumericValue(s.charAt(i));
    		expValue += bit * Math.pow(2, expBits - i);
    	}
    	
    	// calculate largest exponent value
    	int largestExp = 0;
    	for (int i = 1; i <= expBits; i++) {
    		largestExp += 1 * Math.pow(2, expBits - i);
    	}
    	
    	// splitting the string to make calculating fraction part easier
    	String fraction = s.substring(expBits + 1, bitDataSize);
    	if (fraction.length() > 13) {
    		// round to even
    		 boolean halfWay = false;
    		 boolean greaterThanHalfWay = false;
    		 String roundFrac = fraction.substring(0, 13);
    	     String checkHalfWay = fraction.substring(13);
    	     
    	     if (checkHalfWay.charAt(0) == '1') {
    	    	 halfWay = true;
    	    	 for (int i = 1; i < checkHalfWay.length(); i++) {
    	    		 // greater than half way?
    	    		 if (checkHalfWay.charAt(i) == '1') {
    	    			 greaterThanHalfWay = true;
    	    			 halfWay = false;
    	    		 }
    	    	 }
    	    	 
    	    	 if (greaterThanHalfWay) 
    	    		 roundFrac = addOneToFraction(roundFrac);
    	    	 else if (halfWay && roundFrac.charAt(12) == '1')
    	    		 roundFrac = addOneToFraction(roundFrac);
    	    	 
    	    	 fraction = roundFrac;	 
    	     }
    	}
    	
    	// calculate mantissa
    	
    	// Normalized values
    	if (expValue != 0 && expValue != largestExp) {
    		mantissa = 1;
    		E = expValue - bias;
    	}
    	// Denormalized values
    	else if (expValue == 0) {
    		mantissa = 0;
    		E = 1 - bias;
    	}
    	// calculate decimal value for both normalized and denormalized values
    	for (int i = 0; i < fraction.length(); i++) {
    		int bit = Character.getNumericValue(fraction.charAt(i));
    		mantissa += bit * Math.pow(2, -(i + 1));
    	}
    	// return Normalized and Denormalized values
    	if (expValue != largestExp) {
    		double decimalValue = Math.pow(-1, signBit) * mantissa * Math.pow(2, E);
    		if (decimalValue == 0) 
    			return signBit == 0 ? "0" : "-0";
    		if (E == 1) {
    			return String.format("%.5f", decimalValue);
    		}
    		return String.format("%.5e", decimalValue);
    	}
    	// return special cases (i.e. inf, NaN)
    	else if (expValue == largestExp) {
    		if (mantissa == 0)
    			return signBit == 0 ? "inf" : "-inf";
    		else
    			return "NaN";
    	}
    	return "";
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
        String[] dataInCurrentLine = new String[HEX_NUMBER];

        while (splitter.hasNextLine()) {
            // get the current line
            String currentLine = splitter.nextLine();
            dataInCurrentLine = currentLine.split(" ");
            // if the byte ordering is little endian, reverse the order of the hex numbers
            if (byteOrdering.equalsIgnoreCase("l")) {
                    for (int i = 0; i < HEX_NUMBER; i += dataSize) {
                        int k = dataSize + i;
                        for (int j = i; j < i + dataSize/2; j++) {
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
                        outputNumber.put(i + ":" + j, binToFloat(currentBin, dataSize));
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
                        // convert bin to int

                        //put the output to the outputNumber hashmap

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
        lineCounter.close();
        outputWriter.close();
    }

}
