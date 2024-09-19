package spell;

import javafx.scene.control.TableRow;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class SpellCorrector implements ISpellCorrector{

    private ITrie trie = new Trie();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {

        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){

            String word = scanner.next();

            if(word != ""){

                trie.add(word);
            }
        }

        scanner.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {

        inputWord = inputWord.toLowerCase();

        if(inputWord == ""){

            return null;
        }

        if(trie.find(inputWord) != null){

            return inputWord;
        }

        String[] distance1Words = findEditedWords(inputWord);
        String mostLikely = similarWordHelper(distance1Words);

        if(mostLikely == ""){

            ArrayList<String> distance2WordsList = new ArrayList<>();

            for(int i = 0; i < distance1Words.length; i++){

                String[] currentEditedWords = findEditedWords(distance1Words[i]);

                for(int j = 0; j < currentEditedWords.length; j++){

                    distance2WordsList.add(currentEditedWords[j]);
                }
            }

            Object[] distance2WordsObjects = distance2WordsList.toArray();
            String[] distance2Words = Arrays.copyOf(distance2WordsObjects, distance2WordsObjects.length, String[].class);

            mostLikely = similarWordHelper(distance2Words);


            if(mostLikely == ""){

                return null;
            }else{

                return mostLikely;
            }

        }else{
            return mostLikely;
        }
    }

    private String similarWordHelper(String[] words){

        int highestValue = 0;
        String mostLikely = "";
        ArrayList<String> ties = new ArrayList<>();


        for(int i = 0; i < words.length; i++){

            String currentWord = words[i];
            INode node = trie.find(currentWord);

            if(node != null){

                int value = node.getValue();

                if(value > highestValue){

                    highestValue = value;
                    mostLikely = currentWord;
                    ties.clear();
                    ties.add(currentWord);
                }else if(value == highestValue){

                    ties.add(currentWord);
                }
            }
        }

        if(ties.size() > 1){

            Collections.sort(ties);
            mostLikely = ties.get(0);
        }

        return mostLikely;
    }

    private String[] findEditedWords(String word){

        ArrayList<String> editedWordsList = new ArrayList<>();

        //Deletion
        for(int i = 0; i < word.length(); i++){

            StringBuilder str = new StringBuilder(word);
            str.deleteCharAt(i);
            editedWordsList.add(str.toString());
        }

        //Transposition
        for(int i = 0; i < word.length() - 1; i++){

            StringBuilder str = new StringBuilder(word);
            char holder = str.charAt(i + 1);
            str.setCharAt(i + 1, str.charAt(i));
            str.setCharAt(i, holder);
            editedWordsList.add(str.toString());
        }

        //Alteration
        for(int i = 0; i < word.length(); i++){

            for(int j = 0; j < 26; j++){

                StringBuilder str = new StringBuilder(word);
                str.setCharAt(i, (char)('a' + j));
                editedWordsList.add(str.toString());
            }
        }

        //Insertion
        for(int i = 0; i < word.length() + 1; i++){

            for(int j = 0; j < 26; j++){

                StringBuilder str = new StringBuilder(word);
                str.insert(i, (char)('a' + j));
                editedWordsList.add(str.toString());
            }
        }


        Object[] editedWordsObjects = editedWordsList.toArray();
        String[] editedWords = Arrays.copyOf(editedWordsObjects, editedWordsObjects.length, String[].class);

        return editedWords;
    }
}
