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

    private List<HashMap<String, Pattern>> getDefaultPatterns() {
        return List.of(
                HashMap.of("mention", Pattern.compile("([@][A-z]{5,})")),
                HashMap.of("hashtag", Pattern.compile("([#][A-z]{5,})")),
                HashMap.of("name", Pattern.compile("^([A-Z][a-z]+([ ]?[a-z]?['-]?[A-Z][a-z]+)*)$")),
                HashMap.of("account", Pattern.compile("(https?:\\/\\/)?(www\\.)?twitter\\.com\\/[A-Za-z0-9_]{5,15}(\\?(\\w+=\\w+&?)*)?")));
    }


    List<Tuple2<String, Matcher>> validateData(String webPageData) {
//        List allPatterns = getDefaultPatterns();
//        allPatterns.pushAll(getAdditionalPatterns());
        this.webPageData = webPageData;
        return getDefaultPatterns()
                .flatMap(Value::toStream)
                .map(this::getMatchers)
                .filter(matchers -> !matchers.isEmpty())
                .map(Option::get)
                .collect(List.collector());

    }

    Option<String> getNameResource(String nameResource) {
        Matcher matcher = Pattern.compile("([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}").matcher(nameResource);
        if (matcher.find()) {
            return Option.of(matcher.group(0));
        }
        return Option.none();
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