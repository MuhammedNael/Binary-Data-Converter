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
    //will be used to convert current hex to binary
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
    //convert hex to binary
    public static String hexToBin(String hex) {
        String bin = "";
        for (int i = 0; i < hex.length(); i++) {
            bin += hexNumbers(hex.charAt(i));
        }
        return bin;
    }
    //reverse the order due to little endian
    public static String littleEndianHex (String hex) {
        String littleEndian = "";
        for (int i = hex.length() - 1; i >= 0; i--) {
            littleEndian += hex.charAt(i);
        }
        return littleEndian;
    }
    //convert from binary to unsigned integer
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
    		addedOne[0] = '0';
    	}
    	String added = "";
    	for (int i = 0; i < addedOne.length; i++)
    		added += addedOne[i];
    	return added;
    }
    //convert from binary to float
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
    	    	 // check when fraction is 1 to increment mantissa
    	    	 boolean incrementMantissa = true;
    	    	 for (int i = 0; i < roundFrac.length(); i++) {
    	    		 if (roundFrac.charAt(i) != '1')
    	    			 incrementMantissa = false;
    	    	 }
    	    	 
    	    	 if (greaterThanHalfWay) {
    	    		 roundFrac = addOneToFraction(roundFrac);
    	    		 if (incrementMantissa) {
    	    			 mantissa++;
    	    		 }
    	    	 }
    	    	 else if (halfWay && roundFrac.charAt(12) == '1') {
    	    		 roundFrac = addOneToFraction(roundFrac);
    	    		 if (incrementMantissa) {
    	    			 mantissa++;
    	    		 }
    	    	 }
    	    	 
    	    	 fraction = roundFrac;	 
    	     }
    	}
    	
    	// calculate mantissa
    	
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
        // crate scanners to read input and write output
        Scanner inputReader = new Scanner(System.in);
        Scanner lineCounter = null;
        Scanner splitter = null;
        FileWriter outputWriter = null;
        File input = null;

        // take input from user
        System.out.println("Give your input in order of inputFileName (input.txt), byteOrderig (l or b), dataType (float or int or unsigned), dataSize (1, 2, 3 or 4) with blanks between: ");
        String inputFileName = inputReader.next(); // input.txt
        String byteOrdering = inputReader.next(); // l or b
        String dataType = inputReader.next(); // float, int or unsigned
        int dataSize = inputReader.nextInt();// 1, 2, 3 or 4

        // check if the input is valid
        try {
            input = new File("./" + inputFileName);
            lineCounter = new Scanner(input);
            splitter = new Scanner(input);
        } catch (FileNotFoundException e) {
            System.out.println("Input file is not found.");
        }

        // check if the output file is valid
        try {
            outputWriter = new FileWriter("./output.txt");
        } catch (IOException e) {
            System.out.println("Output file is not found.");
        }

        // count the number of lines in the input file
        int rowCounter = 0;

        // create hashmaps to store the input (hex )and output (decimal) numbers
        HashMap<String, String> inputNumber = new HashMap<String, String>();
        HashMap<String, String> outputNumber = new HashMap<String, String>();
        // create an array to store the hex numbers in the current line
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
            // convert float
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
            // convert int
            case "int":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= HEX_NUMBER / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);

                        // Convert binary string to unsigned integer
                        char signBit = currentBin.charAt(0);
                        long tMin = 0;
                        long positivePart = 0;
                        long twos;
                        // decide whether the number is positive or negative
                        if (signBit == '1') {
                            tMin = -(long) Math.pow(2, currentBin.length() - 1);
                            
                        } else if (signBit == '0') {
                            tMin = 0;
                        } 
                        // convert the rest of the bits to decimal
                        for (int k = 1; k < currentBin.length(); k++) {
                            char c = currentBin.charAt(k);
                            if (c == '1') {
                                positivePart += Math.pow(2, currentBin.length() - 1 - k);

                            } else if (c == '0') {
                                positivePart += 0;
                            } 

                        }
                        twos = tMin + positivePart;
                        // put the decimal number to the hashmap
                        outputNumber.put(i + ":" + j, Long.toString(twos));
                    }
                }

                break;

            // convert unsigned
            case "unsigned":
                // convert hex to bin
                for (int i = 1; i <= rowCounter; i++) {
                    for (int j = 1; j <= HEX_NUMBER / dataSize; j++) {
                        String currentHex = inputNumber.get(i + ":" + j);
                        String currentBin = hexToBin(currentHex);
                        
                        // Convert binary string to unsigned integer and put it to the hashmap
                        outputNumber.put(i + ":" + j, unsigned(currentBin));
                        
                    }
                }
                
                break;

            default:
                break;
        }
        // print the output
        printHashMap(outputNumber, rowCounter, dataSize, outputWriter);

        // close scanners
        inputReader.close();
        lineCounter.close();
        outputWriter.close();
    }

}
