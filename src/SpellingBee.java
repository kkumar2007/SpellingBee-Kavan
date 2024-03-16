import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
//Kavan Kumar
//Spelling Bee
//CS2
//Mr. Blick

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [Kavan Kumar]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        //calls the recursive method for generate
        generate("", letters);
    }

    public void generate(String start, String chars) {
        // Base case: If there are no more characters left to generate permutations from, add the generated word to the list and return.
        if (chars.isEmpty()) {
            words.add(start);
            return;
        }

        // Iterate through each character in the remaining characters set.
        for (int i = 0; i < chars.length(); i++) {
            // Add the current prefix to the list of generated words.
            words.add(start);
            // Create a new prefix by appending the current character to the existing prefix.
            String newPrefix = start + chars.charAt(i);
            // Create a new string representing the remaining characters after removing the current character.
            String newRemaining = chars.substring(0, i) + chars.substring(i + 1);
            // Recursively call the generate method with the new prefix and remaining characters.
            generate(newPrefix, newRemaining);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Start the merge sort algorithm with the entire list.
        mergeSort(0, words.size()-1);
    }

    public void mergeSort(int low, int high) {
        // Base case: If the range of elements to sort is 1 or less, no sorting is needed.
        if (high - low == 0) {
            return;
        }

        // Calculate the midpoint of the range.
        int mid = (low + high) / 2;

        // Recursively apply merge sort to the left and right halves of the range.
        mergeSort(low, mid);
        mergeSort(mid + 1, high);

        // Merge the sorted halves together.
        mergeFinal(low, high, mid);
    }

    // Merge two sorted halves into a single sorted list.
    public void mergeFinal(int low, int high, int mid) {
        // Create a temporary list to hold the merged result.
        ArrayList<String> c = new ArrayList<String>();

        // Initialize pointers for the left and right halves of the range.
        int i = low;
        int j = mid + 1;

        // Merge the two halves while both halves have elements.
        while (i <= mid && j <= high) {
            // Compare elements from the left and right halves and add the smaller one to the temporary list.
            if (words.get(i).compareTo(words.get(j)) < 0) {
                c.add(words.get(i));
                i++;
            } else {
                c.add(words.get(j));
                j++;
            }
        }

        // Add any remaining elements from the left half.
        while (i <= mid) {
            c.add(words.get(i));
            i++;
        }

        // Add any remaining elements from the right half.
        while (j <= high) {
            c.add(words.get(j));
            j++;
        }

        // Copy the merged result from the temporary list back to the original list.
        for (int k = 0; k < c.size(); k++) {
            words.set(low + k, c.get(k));
        }
    }
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // Loop through each word in the list.
        for (int i = 0; i < words.size(); i++) {
            // If the word is not found in the dictionary, remove it from the list.
            if (!binarySearch(words.get(i))) {
                words.remove(i); // Remove the word at index i.
                i--; // Decrement i to account for the removal of the word.
            }
        }
    }

    // Binary search to check if a word exists in the dictionary.
    public boolean binarySearch(String word) {
        int low = 0;
        int high = DICTIONARY_SIZE - 1;

        // Perform binary search until the low and high pointers meet.
        while (low <= high) {
            int mid = (low + high) / 2; // Calculate the midpoint.
            int comparison = DICTIONARY[mid].compareTo(word); // Compare the word with the word at the midpoint.

            // If the word is found at the midpoint, return true.
            if (comparison == 0) {
                return true;
            } else if (comparison < 0) { // If the word is lexicographically smaller, search the right half.
                low = mid + 1;
            } else { // If the word is lexicographically larger, search the left half.
                high = mid - 1;
            }
        }
        // If the word is not found in the dictionary, return false.
        return false;
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
