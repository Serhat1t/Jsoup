package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import java.io.IOException;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) {
        try (   InputStream sentence = Main.class.getClassLoader().getResourceAsStream("en-sent.bin");
                InputStream token = Main.class.getClassLoader().getResourceAsStream("en-token.bin");
                InputStream nameFinder = Main.class.getClassLoader().getResourceAsStream("en-ner-person.bin")){
            Document document= Jsoup.connect("https://opennlp.apache.org/books-tutorials-and-talks.html").get();
            String fullText=document.select("div.container").text();
            SentenceModel sentenceModel = new SentenceModel(sentence);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
            String sentences[] = sentenceDetector.sentDetect(fullText);
            TokenizerModel tokenizerModel = new TokenizerModel(token);
            Tokenizer tokenizer = new TokenizerME(tokenizerModel);
            TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(nameFinder);
            NameFinderME nameFinderME = new NameFinderME(tokenNameFinderModel);

            for(int i=0;i< sentences.length;i++) {
                String[] tokens=tokenizer.tokenize(sentences[i]);
                Span[] nameSpans = nameFinderME.find(tokens);
                for (int j=0;j<nameSpans.length;j++){
                    System.out.println(tokens[nameSpans[j].getStart()]+" "+tokens[nameSpans[j].getStart()+1]);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }








    }


}