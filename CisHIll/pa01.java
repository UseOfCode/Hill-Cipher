import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class pa01 {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java pa01 <keyfile> <plaintextfile>");
            return;
        }

        String keyFileName = args[0];
        String plaintextFileName = args[1];

        try {
            // Read the encryption key matrix from the key file
            int[][] keyMatrix = readKeyMatrix(keyFileName);

            // Read the plaintext from the plaintext file
            String plaintext = readPlaintext(plaintextFileName, keyMatrix.length);

            // Encrypt the plaintext using the Hill cipher
            String ciphertext = hillCipherEncrypt(plaintext, keyMatrix);

            // Output the results to the console
            printResults(keyMatrix, plaintext, ciphertext);

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    // Method to read the encryption key matrix from the key file
    private static int[][] readKeyMatrix(String keyFileName) throws FileNotFoundException {
        Scanner keyScanner = new Scanner(new File(keyFileName));
        int n = keyScanner.nextInt();
        int[][] keyMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                keyMatrix[i][j] = keyScanner.nextInt();
            }
        }
        keyScanner.close();
        return keyMatrix;
    }

    // Method to read the plaintext from the plaintext file
    private static String readPlaintext(String plaintextFileName, int keySize) throws FileNotFoundException {
        StringBuilder plaintextBuilder = new StringBuilder();
        Scanner textScanner = new Scanner(new File(plaintextFileName));
        while (textScanner.hasNextLine()) {
            String line = textScanner.nextLine();
            for (char c : line.toCharArray()) {
                if (Character.isLetter(c) && c < 128) {  // Only consider alphabetic ASCII characters
                    plaintextBuilder.append(Character.toLowerCase(c));
                }
            }
        }
        textScanner.close();

        // Pad the plaintext to match the block size of the key matrix
        while (plaintextBuilder.length() % keySize != 0) {
            plaintextBuilder.append('x');
        }
        return plaintextBuilder.toString();
    }

    // Method to encrypt the plaintext using the Hill cipher
    private static String hillCipherEncrypt(String plaintext, int[][] keyMatrix) {
        int n = keyMatrix.length;
        StringBuilder ciphertextBuilder = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += n) {
            int[] vector = new int[n];
            for (int j = 0; j < n; j++) {
                vector[j] = plaintext.charAt(i + j) - 'a';
            }

            int[] encryptedVector = new int[n];
            for (int row = 0; row < n; row++) {
                encryptedVector[row] = 0;
                for (int col = 0; col < n; col++) {
                    encryptedVector[row] += keyMatrix[row][col] * vector[col];
                }
                // Fix modulus operation to handle negative values
                encryptedVector[row] = (encryptedVector[row] % 26 + 26) % 26;
                ciphertextBuilder.append((char) (encryptedVector[row] + 'a'));
            }
        }

        return ciphertextBuilder.toString();
    }

    // Method to print the results to the console
    private static void printResults(int[][] keyMatrix, String plaintext, String ciphertext) {
        // Add a blank line at the beginning (if required)
        System.out.println();

        System.out.println("Key matrix:");
        // Print key matrix with consistent formatting (%4d)
        for (int i = 0; i < keyMatrix.length; i++) {
            for (int j = 0; j < keyMatrix[i].length; j++) {
                System.out.printf("%4d", keyMatrix[i][j]); // Align each number to be 4 spaces wide
            }
            System.out.println();
        }

        // Add exactly one blank line after the key matrix
        System.out.println();

        // Print plaintext
        System.out.println("Plaintext:");
        printFormattedText(plaintext);

        // Add one blank line between plaintext and ciphertext
        System.out.println();

        // Print ciphertext
        System.out.println("Ciphertext:");
        printFormattedText(ciphertext);

        // No extra newline at the end
    }

    // Method to print formatted text with exactly 80 characters per line
    private static void printFormattedText(String text) {
        int length = text.length();
        for (int i = 0; i < length; i++) {
            System.out.print(text.charAt(i));
            if ((i + 1) % 80 == 0 && i != length - 1) {  // Only print newline after full 80 chars, avoid after last character
                System.out.println();
            }
        }
        // Ensure there's no extra newline at the end
        if (length % 80 != 0) {
            System.out.println();  // Only add this final newline if the last line isn't exactly 80 characters
        }
    }
}

