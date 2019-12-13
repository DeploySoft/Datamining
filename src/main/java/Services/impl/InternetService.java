package Services.impl;

import Services.ISourceService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;

public class InternetService implements ISourceService {
    private final static Logger LOG = Logger.getLogger(InternetService.class.getName());

    public InternetService() {
    }

    @Override
    public Optional<String> dataFromSource(String url) {
        try {
            LOG.info("Starting read " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            //Some page have redirects
            boolean redirect = false;
            if (isRedirect(redirect, conn.getResponseCode())) {
                String newUrl = conn.getHeaderField("Location");
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder html = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            LOG.info("Data has been extracted " + url);
            return Optional.of(html.toString());
        } catch (Exception e) {
            LOG.severe("Some happening with the page " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean isRedirect(boolean redirect, int status) {
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
                redirect = true;
        }
        return redirect;
    }
}
