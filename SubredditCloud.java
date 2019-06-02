import java.util.List;


public class SubredditCloud{


    public static WordCountTree readBook(String filename, WordCountTree ignoreWords){
        WordCountTree wct = new WordCountTree();
        WordIterator wi = new WordIterator(filename);
        long start = System.currentTimeMillis();
        wi.hasNext();
        wct.name = wi.next();
        while (wi.hasNext()){
            if (ignoreWords != null){
                if(!ignoreWords.contains(wi.next())){
                    wct.count(wi.next());
                }
            } else{
                wct.count(wi.next());
            }
        }
//        System.out.println("Time");
//            System.out.println(System.currentTimeMillis() - start);
//        System.out.println("Total Words:");
//            System.out.println(wct.getTotalWordCount());
//        System.out.println("Unique Words:");
//            System.out.println(wct.getUniqueWordCont());
        return wct;
    }

    public static void render(WordCountTree wct, int max_words){
        WordlGenerator wlg = new WordlGenerator(500,500);
        wlg.setCountRange(wct.wordsInOrder().get(0).getCount(),1);
        wlg.setFontRange(8,100);
        wlg.setFontName("Utopia");
        wlg.setSpeedMult(2);
        List<WordCount> list = wct.wordsInOrder().subList(0,max_words);
        for (WordCount elem:list){
            wlg.addWord(elem);
        }
        wlg.save("/home/ryan/SubWordClouds/"+wct.name+".png");
    }



    public static void main(String[] args) {
        WordCountTree ignore = readBook("/home/ryan/IdeaProjects/Project3/src/StopWords", null);

        String filename = args[0];
        //String filename = "space";

        String path = "/home/ryan/SubWordClouds/"+filename;

        WordCountTree wct = readBook(path, ignore);
        render(wct, 35);

/////////////////////////////////////////////////////////////////////////////////////////////////////////

//        try{
//            WordCountTree wct = readBook(path, ignore);
//            render(wct, 35);
//        } catch (Exception e){
//            //run python code to make the file
//                try{
//                    System.out.println("python");
//
//                        Process p = Runtime.getRuntime().exec(
//                                new String[]{"sh","/home/ryan/job.sh"},
//                                null, null);
//                        int exitCode = p.waitFor();
//                    System.out.println();
//
//
//                } catch (Exception f){
//                    System.out.println("fail");
//                }
//
//            WordCountTree wct = readBook(path, ignore);
//            render(wct, 35);
//        }





//        WordCountTree kjb = readBook("/home/ryan/Downloads/kjb",ignore);
//        render(kjb,50);
//        WordCountTree nt84 = readBook("/home/ryan/1984", ignore);
//        render(nt84, 50);
//        WordCountTree bnw = readBook("/home/ryan/bnw", ignore);
//        render(bnw, 50);
//        WordCountTree science = readBook("/home/ryan/SubWordClouds/science", ignore);
//        render(science, 35);
//
//        WordCountTree math = readBook("/home/ryan/SubWordClouds/math", ignore);
//        render(math, 35);
//
//        WordCountTree history = readBook("/home/ryan/SubWordClouds/history", ignore);
//        render(history, 35);
//
//        WordCountTree sports = readBook("/home/ryan/SubWordClouds/sports", ignore);
//        render(sports, 35);
    }
}
