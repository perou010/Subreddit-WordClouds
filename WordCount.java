public class WordCount implements Comparable<WordCount>{
    private String word;
    private int count;

    public WordCount(String word, int count)  {
        this.word = word;
        this.count = count;
    }

    public WordCount(String word){
        this.word = word;
        count = 0;
    }

    public String getWord(){
        return word;
    }

    public int getCount(){
        return count;
    }

    public void addCount(){
        count++;
    }

    public String toString(){
        return String.format("%s (%d)", word, count);
    }

    public int compareTo(WordCount other) {
        return other.count - count;
    }
}
