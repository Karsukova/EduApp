package space.karsukova.educateapp.utils;

public class Document {
    private String docTitle, docUrl;

    public Document() {
    }

    public Document(String docTitle, String docUrl) {
        this.docTitle = docTitle;
        this.docUrl = docUrl;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public String getDocUrl() {
        return docUrl;
    }
}
