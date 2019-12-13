package Services;

import java.util.Optional;

/**
 * Source to get the data
 */
public interface ISourceService {

    Optional<String> dataFromSource(String url);

    boolean isRedirect(boolean redirect, int status);
}
