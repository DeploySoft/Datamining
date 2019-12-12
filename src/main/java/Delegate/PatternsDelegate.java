package Delegate;

import Config.PropertiesConfig;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Value;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.vavr.API.Some;

public class PatternsDelegate {

    private PropertiesConfig propertiesConfig;

    private String webPageData;

    public PatternsDelegate(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }


    public PatternsDelegate() {

    }

    public static void main(String[] args) {
        new PatternsDelegate().validateData("123");
    }

    private List<HashMap<String, Pattern>> getDefaultPatterns() {
        return List.of(
                HashMap.of("mention", Pattern.compile("([@][A-z]{5,})")),
                HashMap.of("hashtag", Pattern.compile("([#][A-z]{5,})")),
                HashMap.of("name", Pattern.compile("^([A-Z][a-z]+([ ]?[a-z]?['-]?[A-Z][a-z]+)*)$")),
                HashMap.of("account", Pattern.compile("(https?:\\/\\/)?(www\\.)?twitter\\.com\\/[A-Za-z0-9_]{5,15}(\\?(\\w+=\\w+&?)*)?")));
    }

    public List<HashMap<String, Pattern>> getAdditionalPatterns() {
//    todo read here new patterns
        return null;
    }


    public List<Tuple2<String, Matcher>> validateData(String webPageData) {
//        List allPatterns = getDefaultPatterns();
//        allPatterns.pushAll(getAdditionalPatterns());
        //todo add the new patterns
        this.webPageData = webPageData;
        return getDefaultPatterns()
                .flatMap(Value::toStream)
                .map(this::getMatchers)
                .filter(matchers -> !matchers.isEmpty())
                .map(Option::get)
                .collect(List.collector());

    }


    /**
     * @param tupleOf2 pattern name and regex
     * @return Option Tuple 2 pattern name and Matchers
     */
    private Option<Tuple2<String, Matcher>> getMatchers(Tuple2<String, Pattern> tupleOf2) {
        Matcher matcher = tupleOf2._2().matcher(webPageData);
        return matcher.find() ? Some(Tuple.of(tupleOf2._1(), matcher)) : Option.none();
    }

}