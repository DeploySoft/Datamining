package Services.impl;

import Services.IResourceService;
import io.vavr.control.Option;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileService implements IResourceService {

    private final static Logger LOG = Logger.getLogger(FileService.class.getName());

    public FileService() {
    }

    public static void main(String[] args) {
        String fileName = "c://urls.txt";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(System.out::println);
            System.out.println(stream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Path path = Paths.get("c:/users/janbs/output.txt");
//        //Use try-with-resource to get auto-closeable writer instance
//        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//            writer.write("Hello World !!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Option<Stream<String>> readSource(String url) {
        try (Stream<String> stream = Files.lines(Paths.get(url))) {
            return Option.of(stream);
        } catch (IOException e) {
            LOG.severe("The file isn't in that url");
            return Option.none();
        }
    }

    @Override
    public boolean saveData(String data, String nameFile) {
        Path path = Paths.get("c:/users/janbs/" + nameFile + ".txt");
        //Use try-with-resource to get auto-closeable writer instance
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

