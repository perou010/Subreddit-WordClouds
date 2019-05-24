import java.util.Comparator;

public class SortByTotal implements Comparator<WordCount> {
    public int compare(WordCount a, WordCount b){
        return b.getCount() - a.getCount();
    }
}
