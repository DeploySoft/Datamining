import io.vavr.collection.List;
import io.vavr.control.Option;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.vavr.API.Some;


public class Main {
    private final static Logger LOG = Logger.getLogger(Main.class.getName());

    public Main() {
    }

    public static void main(String[] args) {
        try {
            LOG.info("start");
            String url = "https://www.bbc.co.uk/news";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            System.out.println("Request URL ... " + url);
            boolean redirect = false;
//             normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            System.out.println("Response Code ... " + status);
            if (redirect) {
                String newUrl = conn.getHeaderField("Location");
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                System.out.println("Redirect to URL : " + newUrl);

            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer html = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
//            System.out.println("URL Content... \n" + html.toString());
            System.out.println("Done");
            String defHtml = html.toString();

            Pattern mention = Pattern.compile("([@][A-z]+)");
            Pattern hashtag = Pattern.compile("([#][A-z]+)");
            Pattern name = Pattern.compile("^([A-Z][a-z]+([ ]?[a-z]?['-]?[A-Z][a-z]+)*)$");
            Pattern account = Pattern.compile("(https?:\\/\\/)?(www\\.)?twitter\\.com\\/[A-Za-z0-9_]{5,15}(\\?(\\w+=\\w+&?)*)?");
            java.util.List<Pattern> asd = List.of(mention, hashtag, name, account).asJava();

            java.util.List<Matcher> matchers = asd.stream()
                    .filter(Objects::nonNull)
                    .map(_pattern -> test.getMatchers(_pattern, defHtml))
                    .filter(_matchers -> !_matchers.isEmpty())
                    .map(Option::get)
                    .collect(Collectors.toList());

            matchers.stream()
                    .map(Matcher::reset)
                    .forEach(matcher -> {
                        while (matcher.find()) {
                            //TODO HERE I SHOULD SEND TO THE SERVICES THAT WILL BE THE WRITER
                            System.out.println(matcher.group(0));
                        }
                    });
        } catch (Exception e) {
            //TODO Here controlled this ex url doesn't exist
            e.printStackTrace();
        }

    }
}


class test {


    static Option<Matcher> getMatchers(Pattern regex, CharSequence webData) {
        Matcher matcher = regex.matcher(webData);
        return matcher.find() ? Some(matcher) : Option.none();
    }
}