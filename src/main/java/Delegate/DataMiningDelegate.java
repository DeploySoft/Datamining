package Delegate;

import Services.impl.FileService;
import io.vavr.Tuple2;

import java.util.List;
import java.util.regex.Matcher;

public class DataMiningDelegate {
    private final FileService fileService;
    private final PatternsDelegate patternsDelegate;

    public DataMiningDelegate(FileService fileService, PatternsDelegate patternsDelegate) {
        this.fileService = fileService;
        this.patternsDelegate = patternsDelegate;
    }

    /**
     * @param url from the file where are the urls
     * @return list of the url
     */
    public List<String> getData(String url) {
        this.fileService.readSource(url)
                .forEach(stringStream ->
                        this.patternsDelegate.validateData(stringStream.toString())
                                .map(stringMatcherTuple2 -> stringMatcherTuple2.apply(this::getResult))
                );
        //        java.util.List<Matcher> matchers = allPatterns.asJava()
//                .stream()
//                .filter(Objects::nonNull)
//                .map(_pattern -> getMatchers(_pattern, webPageData))
//                .filter(_matchers -> !_matchers.isEmpty())
//                .map(Option::get)
//                .collect(Collectors.toList());
//
//        matchers.stream()
//                .map(Matcher::reset)
//                .forEach(matcher -> {
//                    while (matcher.find()) {
//                        //TODO HERE I SHOULD SEND TO THE SERVICES THAT WILL BE THE WRITER
//                        System.out.println(matcher.group(0));
//                    }
//                });
        return null;
    }


    private String getResult(String s, Matcher m) {
        m.reset();
        while (m.find()) {
            System.out.println(s + "|" + m.group(0));
            this.fileService.saveData(m.group(), s);
        }
        return "";
    }


}
