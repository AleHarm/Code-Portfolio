package spell;

public class Node implements INode{

    private int wordCount = 0;
    private INode[] children = new Node[26];

    @Override
    public int getValue() {

        return wordCount;
    }

    @Override
    public void incrementValue() {

        wordCount++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
