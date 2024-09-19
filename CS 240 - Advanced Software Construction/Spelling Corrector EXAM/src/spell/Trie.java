package spell;

public class Trie implements ITrie{

    private int wordCount = 0;
    private int nodeCount = 1;
    private INode rootNode = new Node();

    @Override
    public void add(String word) {

        word = word.toLowerCase();
        INode currentNode = rootNode;

        for(int i = 0; i < word.length(); i++){

            char currentChar = word.charAt(i);
            int index = currentChar - 'a';

            if(currentNode.getChildren()[index] == null){

                currentNode.getChildren()[index] = new Node();
                nodeCount++;
            }

            currentNode = currentNode.getChildren()[index];
        }

        if(currentNode.getValue() == 0){

            wordCount++;
        }

        currentNode.incrementValue();
    }

    @Override
    public INode find(String word) {

        word = word.toLowerCase();
        INode currentNode = rootNode;

        if(word == ""){

            return null;
        }

        for(int i = 0; i < word.length(); i++){

            char currentChar = word.charAt(i);
            int index = currentChar - 'a';

            if(currentNode.getChildren()[index] != null){

                currentNode = currentNode.getChildren()[index];
            }else{
                return null;
            }
        }

        if(currentNode.getValue() > 0){

            return currentNode;
        }

        return null;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString(){

        StringBuilder word = new StringBuilder();
        StringBuilder words = new StringBuilder();

        return toStringHelper(rootNode, word, words);
    }

    private String toStringHelper(INode node, StringBuilder word, StringBuilder words){

        if(node.getValue() > 0){

            words.append(word);
            words.append("\n");
        }

        for(int i = 0; i < 26; i++){

            INode[] children = node.getChildren();

            if(children[i] != null){

                char currentChar = (char)('a' + i);
                word.append(currentChar);
                toStringHelper(children[i], word, words);
                word.deleteCharAt(word.length() - 1);
            }
        }

        return words.toString();
    }

    @Override
    public int hashCode(){

        int numChildren = 0;
        int sumChildren = 0;

        for(int i = 0; i < 26; i++){

            if(rootNode.getChildren()[i] != null){

                numChildren++;
                sumChildren += i;
            }
        }

        return nodeCount * wordCount * (numChildren + sumChildren);
    }

    @Override
    public boolean equals(Object o){

        if(o == null){

            return false;
        }

        if(o == this){

            return true;
        }

        if(o instanceof Trie){

            Trie trieO = (Trie)o;

            if(this.nodeCount != trieO.nodeCount || this.wordCount != trieO.wordCount){

                return false;
            }

            return equalsHelper(this.rootNode, trieO.rootNode);
        }

        return false;
    }

    private boolean equalsHelper(INode node1, INode node2){

        if(node1.getValue() != node2.getValue()){

            return false;
        }

        for(int i = 0; i < 26; i++){

            INode child1 = node1.getChildren()[i];
            INode child2 = node2.getChildren()[i];
            boolean c1Null = (child1 == null);
            boolean c2Null = (child2 == null);

            if(!c1Null && !c2Null){

                if(!equalsHelper(child1, child2)){

                    return false;
                }
            }else if(c1Null != c2Null){
                return false;
            }
        }
        return true;
    }
}
