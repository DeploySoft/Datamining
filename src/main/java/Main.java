import Config.PropertiesConfig;
import Delegate.DataMiningDelegate;
import Delegate.PatternsDelegate;
import Services.impl.FileService;
import Services.impl.InternetService;


public class Main {


    private final DataMiningDelegate dataMiningDelegate;

    private Main() {
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        this.dataMiningDelegate = new DataMiningDelegate(new FileService(propertiesConfig), new InternetService(), new PatternsDelegate(propertiesConfig));
        this.dataMiningDelegate.DataMining("c://urls.txt");
    }

    public static void main(String[] args) {
        new Main();
    }
}