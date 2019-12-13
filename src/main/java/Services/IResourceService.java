package Services;

import io.vavr.control.Option;

import java.util.stream.Stream;

/**
 * Resource to save the data
 */
public interface IResourceService {

    Option<Stream<String>> readSource(String url);

    boolean saveData(String data, String nameFile, String nameResource);
}
