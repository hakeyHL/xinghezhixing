package hasoffer.core.analysis;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import hasoffer.core.bo.match.TagMatchResult;
import hasoffer.core.bo.match.TagType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chevy on 2016/7/3.
 */
public class LingHelper {

    private static MapDictionary<String> dictionary = new MapDictionary<String>();

    private static ExactDictionaryChunker dictionaryChunkerFF;

    public static void createDictionaryChunker() {
        dictionaryChunkerFF = new ExactDictionaryChunker(
                dictionary,
                IndoEuropeanTokenizerFactory.INSTANCE,
                false,
                false
        );
    }

    public static void addToDict(String tag, TagType tagType, double score) {
        dictionary.addEntry(new DictionaryEntry<String>(tag, tagType.name(), score));
    }

    public static Map<String, List<String>> analysis(String text) {
        Chunking chunking = dictionaryChunkerFF.chunk(text);

        Map<String, List<String>> tagMap = new LinkedHashMap<String, List<String>>();

        for (Chunk chunk : chunking.chunkSet()) {
            int start = chunk.start();
            int end = chunk.end();
            String type = chunk.type();
            double score = chunk.score();
            String phrase = text.substring(start, end);
//            System.out.println(text +
//                    "  phrase=|" + phrase + "|"
//                    + " start=" + start
//                    + " end=" + end
//                    + " type=" + type
//                    + " score=" + score);
            List<String> tags = tagMap.get(type);

            if (tags == null) {
                tags = new ArrayList<String>();
                tagMap.put(type, tags);
            }

            tags.add(phrase);
        }

        return tagMap;
    }

    public static Map<String, List<TagMatchResult>> analysis2(String text) {
        Chunking chunking = dictionaryChunkerFF.chunk(text);

        Map<String, List<TagMatchResult>> tagMap = new LinkedHashMap<String, List<TagMatchResult>>();

        for (Chunk chunk : chunking.chunkSet()) {
            int start = chunk.start();
            int end = chunk.end();
            String type = chunk.type();
            double score = chunk.score();
            String phrase = text.substring(start, end);

            List<TagMatchResult> tags = tagMap.get(type);

            if (tags == null) {
                tags = new ArrayList<TagMatchResult>();
                tagMap.put(type, tags);
            }

            String tag = phrase.toLowerCase();

            tags.add(new TagMatchResult(phrase, TagMapHelper.get(TagType.valueOf(type), tag)));
        }

        return tagMap;
    }

    public static void clearDict() {
        dictionary.clear();
    }

    public static int getDictSize() {
        return dictionary.size();
    }
}
