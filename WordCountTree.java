import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WordCountTree {
    WordBSTNode root;
    int UniqueWordCount;
    int TotalWordCount;
    public String name;

    public WordCountTree(){
        root = null;
        UniqueWordCount = 0;
        TotalWordCount = 0;
    }


    public WordBSTNode find(String word){
        if(word == null){return null;}
         return find(word, root);
    }

    public WordBSTNode find(String word, WordBSTNode root){
        if (root == null){return null;}
        if (word.equals(root.getWordCount().getWord())){return root;}
        WordBSTNode left = find(word, root.left);
        if (left != null){return left;}
        return find(word, root.right);
    }


    public void count(String word){
        TotalWordCount++;
        if (root == null){
            root = new WordBSTNode(new WordCount(word,1));
            return;
        }
        WordBSTNode temp = root;
        WordBSTNode parent = null;
        while (temp != null){
            int test = word.compareTo(temp.getWordCount().getWord());
            if (test == 0){
                temp.getWordCount().addCount();
                return;
            } else if (test >= 0){
                parent = temp;
                temp = temp.left;
            } else {
                parent = temp;
                temp = temp.right;
            }
        }

        if(parent.getWordCount().getWord().compareTo(word) <= 0){
            parent.left = new WordBSTNode(new WordCount(word,1));
            UniqueWordCount++;
        } else{
            parent.right = new WordBSTNode(new WordCount(word,1));
            UniqueWordCount++;
        }
    }

    public int getUniqueWordCont() {
        return UniqueWordCount;
    }

    public int getTotalWordCount(){
        return TotalWordCount;
    }

    public boolean contains(String word){
        return (find(word, root) != null);
    }

    public int getCount(String word){
        if (contains(word)){
            return 0;
        } else{
            WordBSTNode temp = root;
            while(temp != null){
                if(word.equals(temp.getWordCount().getWord())){
                    return temp.getWordCount().getCount();
                }
            }
        }
        System.out.println("getCount shouldn't get here");
        return 0;
    }

    public List<WordCount> wordsInOrder(){
        List<WordCount> returnList = wordsInOrderH(root, new ArrayList());
        Comparator wcComparator = new SortByTotal();
        returnList.sort(wcComparator);
        return returnList;
    }

    private List<WordCount> wordsInOrderH(WordBSTNode root, ArrayList list){
        if(root == null){return list;}
        wordsInOrderH(root.left,list);
        list.add(root.getWordCount());
        wordsInOrderH(root.right,list);
        return list;
    }

    public class WordBSTNode{
        WordCount wc;
        WordBSTNode left;
        WordBSTNode right;
        public WordBSTNode(){
            this.wc = null;
            left = null;
            right = null;
        }
        public WordBSTNode(WordCount wc){
            this.wc = wc;
            left = null;
            right = null;
        }
        public WordCount getWordCount(){
            return wc;
        }
    }
}
