import java.util.ArrayList;
import java.util.Scanner;

public class Playfair {

    private static final String[][] key = new String[5][5];

    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        System.out.print("Enter text to be encrypted: ");
        String textForEncryption = console.nextLine();

        String textFormated = formatText(textForEncryption);
        System.out.println("Prepared text: " + textFormated);

        ArrayList<String> textPrepared = prepareText(textFormated);
        System.out.println(textPrepared);

        System.out.println("Enter encryption key: ");
        String encryptionKey = console.nextLine();

        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        String encryptionKeyPrepared = prepareEncyptionKey(encryptionKey + alphabet);
        System.out.println("Prepared text for encryption key: " + encryptionKeyPrepared);

        String[][] keyMatrix = tableMatrix(encryptionKeyPrepared);

        ArrayList<String> encryptedText = encryptDecrypt("e", textPrepared);
        System.out.println(encryptedText);
        System.out.println("Encrypted message: " + arraylistToString(encryptedText));

        ArrayList<String> decryptedText = encryptDecrypt("d", encryptedText);
        System.out.println(prepareDecryptedText(decryptedText));
        System.out.println("Decrypted message: " + arraylistToString(prepareDecryptedText(decryptedText)));
    }

    //Transform text in uppercase + remove all special characters and numbers + replace J with I
    //Used for preparing the text and the encryption key
    public static String formatText(String textForEncryption){
        textForEncryption = textForEncryption.toUpperCase();
        textForEncryption = textForEncryption.replaceAll("\\s+","Q");
        textForEncryption = textForEncryption.replaceAll("\\t","Q");
        textForEncryption = textForEncryption.replaceAll("\\n","W");
        textForEncryption = textForEncryption.replaceAll("[^A-Z]", "");
        textForEncryption = textForEncryption.replace("J", "I");
        return textForEncryption;
    }

    //Transform the text into pairs and create an array for easy handling
    //If there are groups of 2 characters with the same letter, replace the last one with "Z"
    //If the last group of characters is odd, then insert "X" as last character
    public static ArrayList<String> prepareText(String textFormated){

        ArrayList<String> textArray = new ArrayList<>();

        int i = 0;
        while(i < textFormated.length()){

            int j = i + 2;
            if(j > textFormated.length())
                j = textFormated.length();
            textArray.add(textFormated.substring(i,j));
            i = j;
        }

        int j = 0;
        while(j < textArray.size()-1){

            if(String.valueOf(textArray.get(j).charAt(0)).equals(String.valueOf(textArray.get(j).charAt(1)))){
                textArray.set(j, textArray.get(j).charAt(0) + "Y");
            }
            j++;
        }

        int lastItem = textArray.size()-1;

        if((textArray.get(lastItem).length()) == 1){
            textArray.set(lastItem, textArray.get(lastItem).charAt(0) + "Q");
        }

        return textArray;
    }

    //Format the encryption key text
    //Remove the duplicates and insert the rest of the letters from alphabet
    public static String prepareEncyptionKey(String encyptionKey){

        String inputEncryptionKey = formatText(encyptionKey);
        StringBuilder outputEncyptionKey = new StringBuilder();

        for(int i = 0; i < inputEncryptionKey.length(); i++){
            if(!outputEncyptionKey.toString().contains(String.valueOf(inputEncryptionKey.charAt(i)))) {
                outputEncyptionKey.append(inputEncryptionKey.charAt(i));
           }
        }

        return outputEncyptionKey.toString();
    }

    //Using the preparedEncryptionKey - prepare the encryption matrix
    public static String[][] tableMatrix(String encryptionKeyPrepared){

        int matrixCount = 0;
        for(int m=0; m < 5; m++)
        {
            for(int n=0; n < 5; n++)
            {
                key[m][n] = String.valueOf(encryptionKeyPrepared.charAt(matrixCount++));
                System.out.print(" " + key[m][n]);
            }
            System.out.printf("%n");
        }
        return key;
    }

    // searches matrix for char index
    public static int[] findRowColumn (String character){
        int[] pair = {-1,-1}; // row,column

        out:
        for(int row = 0; row < 5; row++) // row
        {
            for(int column = 0; column < 5; column++) // column
            {
                if(key[row][column].equals(character))
                {
                    pair[0] = row;
                    pair[1] = column;
                    break out;
                }
            }
        }
        return pair;
    }

    //encrypt/decrypt the text prepared using the encryption key
    public static ArrayList<String>  encryptDecrypt(String type, ArrayList<String> textPrepared){
        int[] fChar;
        int[] sChar;

        ArrayList<String> converted = new ArrayList<>(); //(ArrayList<String>) (textPrepared.clone());

        int mod = 0;

        if (type.equals("e")){
            mod = 1;
        }
        else if(type.equals("d")) {
            mod = 4;
        }

        for (String s : textPrepared) {
            fChar = findRowColumn(String.valueOf(s.charAt(0)));
            sChar = findRowColumn(String.valueOf(s.charAt(1)));

            if (fChar[0] == sChar[0]) // same row
            {
                converted.add(key[fChar[0]][(fChar[1] + mod) % 5] + key[sChar[0]][(sChar[1] + mod) % 5]);
            } else if (fChar[1] == sChar[1]) // same column
            {
                converted.add(key[(fChar[0] + mod) % 5][fChar[1]] + key[(sChar[0] + mod) % 5][sChar[1]]);
            } else // box
            {
                converted.add(key[fChar[0]][sChar[1]] + key[sChar[0]][fChar[1]]);
            }
        }
    return converted;
    }

    //after decryption, replace the z and x characters back
    public static ArrayList<String> prepareDecryptedText(ArrayList<String> decryptedText) {
        
        ArrayList<String> decryptedTextPrepared = (ArrayList<String>) decryptedText.clone();

        for (int i = 0; i < decryptedText.size(); i++) {
            if (String.valueOf(decryptedText.get(i).charAt(1)).equals("Y")) {
                decryptedTextPrepared.set(i, (String.valueOf(decryptedText.get(i).charAt(0)) + String.valueOf(decryptedText.get(i).charAt(0))));
            }
            if (String.valueOf(decryptedText.get(i).charAt(0)).equals("Q")) {
                decryptedTextPrepared.set(i, (" " + String.valueOf(decryptedText.get(i).charAt(1))));
            }
            if (String.valueOf(decryptedText.get(i).charAt(1)).equals("Q")) {
                decryptedTextPrepared.set(i, (String.valueOf(decryptedText.get(i).charAt(0))) + " ");
            }
        }

        int lastItem = decryptedText.size() - 1;

        if (String.valueOf(decryptedText.get(lastItem).charAt(1)).equals("Q")) {
            decryptedTextPrepared.set(lastItem, String.valueOf(decryptedText.get(lastItem).charAt(0)));

        }
        return decryptedTextPrepared;
    }

    //covert an array list to String
    public static String arraylistToString (ArrayList < String > textToConvert) {
        StringBuilder textConverted = new StringBuilder();
        for (String s : textToConvert) {
            textConverted.append(s);
        }
        return textConverted.toString();
    }

}