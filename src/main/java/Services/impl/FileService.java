package Services.impl;

import Config.PropertiesConfig;
import Services.IResourceService;
import io.vavr.control.Option;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileService implements IResourceService {

    private final static Logger LOG = Logger.getLogger(FileService.class.getName());

    private final PropertiesConfig propertiesConfig;

    public FileService(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public Option<Stream<String>> readSource(String url) {
        // try-resource will close de object hahahahaha i forget it
//        try (Stream<String> stream = Files.lines(Paths.get(url))) {
        try {
            Stream<String> stream = Files.lines(Paths.get(url));
            return Option.of(stream);
        } catch (
                IOException e) {
            LOG.severe("The file isn't in that url");
            return Option.none();
        }
    }

    @Override
    public boolean saveData(String data, String nameFile, String nameResource) {
        Path path = Paths.get(propertiesConfig.getRootPath().substring(1) + nameFile + "_" + nameResource + ".txt");
        try {
            Files.write(path, (data + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            if (e instanceof NoSuchFileException) {
                try {
                    Files.write(path, (data).getBytes(), StandardOpenOption.CREATE);
                } catch (IOException ex) {
                    return false;
                }
            }
        }
        return true;
    }
}

