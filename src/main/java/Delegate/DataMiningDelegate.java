package Delegate;

import Services.IResourceService;
import Services.ISourceService;

import java.util.regex.Matcher;

public class DataMiningDelegate {

    private final IResourceService fileService;
    private final ISourceService internetService;
    private final PatternsDelegate patternsDelegate;

    public DataMiningDelegate(IResourceService fileService, ISourceService internetService, PatternsDelegate patternsDelegate) {
        this.fileService = fileService;
        this.internetService = internetService;
        this.patternsDelegate = patternsDelegate;
    }

    /**
     * @param urlResource from the file where are the urls
     */
    public void DataMining(String urlResource) {
        this.fileService.readSource(urlResource).toJavaOptional().ifPresent(
                dataFromResource -> dataFromResource.forEach(
                        _urlSource -> this.internetService.dataFromSource(_urlSource)
                                .ifPresent(
                                        dataFromSource -> this.patternsDelegate.validateData(dataFromSource)
                                                .map(resultTuple2 -> resultTuple2.apply((s, m) -> getResult(s, m, _urlSource)))
                                )
                )
        );
    }


    private String getResult(String s, Matcher m, String urlSource) {
        m.reset();
        while (m.find()) {
            this.fileService.saveData(m.group(), s, patternsDelegate.getNameResource(urlSource).getOrElse("RESOURCE NAME INVALID"));
        }
        return "";
    }


}
