//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {
    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp3|zip|gz))$");
    BufferedWriter bw1 = null;
    BufferedWriter bw2 = null;
    BufferedWriter bw3 = null;
    BufferedWriter txt1 = null;
    String fetchStatistics = null;
    String visitStatistics = null;
    String urlStatistics = null;
    int noOfFetchesAttempted = 0;
    int noOfFetchesSucceeded = 0;
    int noOfFetchesAborted = 0;
    int noOfFetchesFailed = 0;
    int noOfUrlsExtracted = 0;
    int noOfUniqueUrls = 0;
    int noOfUniqueUrlsWithinSite = 0;
    int noOfUniqueUrlsOutsideSite = 0;
    int noOf200s = 0;
    int noOf301s = 0;
    int noOf401s = 0;
    int noOf403s = 0;
    int noOf404s = 0;
    int noOfFilesUnder1kb = 0;
    int noOfFiles1To10kb = 0;
    int noOfFiles10to100kb = 0;
    int noOfFiles100to1000kb = 0;
    int noOfFilesOver1000kb = 0;
    int noOfHtmlFiles = 0;
    int noOfGifFiles = 0;
    int noOfTifFiles = 0;
    int noOfJpegFiles = 0;
    int noOfPngFiles = 0;
    int noOfPdfFiles = 0;
    ArrayList<String> urlsList = new ArrayList();
    boolean isUnique = false;

    public MyCrawler() {
    }

    public void onStart() {
        try {
            this.bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fetch_Boston_Globe.csv", true), "UTF-8"));
            this.bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("visit_Boston_Globe.csv", true), "UTF-8"));
            this.bw3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("urls_Boston_Globe.csv", true), "UTF-8"));
            this.txt1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("CrawlReport_Boston_Globe.txt", true), "UTF-8"));

            try {
                this.txt1.write("Mounika ");
                this.txt1.newLine();
                this.txt1.write("");
                this.txt1.newLine();
                this.txt1.write("News site crawled: http://www.nydailynews.com/");
                this.txt1.newLine();
                this.txt1.newLine();
            } catch (Exception var2) {
                ;
            }
        } catch (UnsupportedEncodingException var3) {
            ;
        } catch (FileNotFoundException var4) {
            ;
        }

    }

    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL();

        try {
            if (!this.urlsList.contains(href)) {
                this.urlsList.add(href);
                ++this.noOfUniqueUrls;
                this.isUnique = true;
            } else {
                this.isUnique = false;
            }

            ++this.noOfUrlsExtracted;
            StringBuffer oneLine = new StringBuffer();
            oneLine.append(href.replaceAll(",", "-"));
            oneLine.append(",");
            if (href.startsWith("https://www.bostonglobe.com/")) {
                oneLine.append("OK");
                if (this.isUnique) {
                    ++this.noOfUniqueUrlsWithinSite;
                }
            } else {
                oneLine.append("N_OK");
                if (this.isUnique) {
                    ++this.noOfUniqueUrlsOutsideSite;
                }
            }

            this.bw3.write(oneLine.toString());
        } catch (Exception var13) {
            ;
        } finally {
            try {
                this.bw3.newLine();
            } catch (Exception var12) {
                ;
            }

        }

        return !FILTERS.matcher(href).matches() && href.startsWith("https://www.bostonglobe.com/");
    }

    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        try {
            ++this.noOfFetchesAttempted;
            if (statusCode == 200) {
                ++this.noOfFetchesSucceeded;
                ++this.noOf200s;
            } else {
                ++this.noOfFetchesFailed;
                if (statusCode == 301) {
                    ++this.noOf301s;
                }

                if (statusCode == 401) {
                    ++this.noOf401s;
                }

                if (statusCode == 403) {
                    ++this.noOf403s;
                }

                if (statusCode == 404) {
                    ++this.noOf404s;
                }
            }

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(webUrl.getURL().replaceAll(",", "-"));
            oneLine.append(",");
            oneLine.append(statusCode);
            this.bw1.write(oneLine.toString());
            this.bw1.newLine();
        } catch (Exception var5) {
            ++this.noOfFetchesAborted;
        }

    }

    public void visit(Page page) {
        try {
            String url = page.getWebURL().getURL();
            byte[] pageContent = page.getContentData();
            double fileSize = (double)(pageContent == null ? 0 : pageContent.length) / 1024.0D;
            if (fileSize < 1.0D) {
                ++this.noOfFilesUnder1kb;
            } else if (fileSize >= 1.0D && fileSize < 10.0D) {
                ++this.noOfFiles1To10kb;
            } else if (fileSize >= 10.0D && fileSize < 100.0D) {
                ++this.noOfFiles10to100kb;
            } else if (fileSize >= 100.0D && fileSize < 1024.0D) {
                ++this.noOfFiles100to1000kb;
            } else if (fileSize >= 1024.0D) {
                ++this.noOfFilesOver1000kb;
            }

            StringBuffer oneLine = new StringBuffer();
            oneLine.append(url.replaceAll(",", "-"));
            oneLine.append(",");
            oneLine.append(fileSize + " kb");
            oneLine.append(",");
            Set links;
            int linksSize;
            if (page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData)page.getParseData();
                links = htmlParseData.getOutgoingUrls();
                linksSize = htmlParseData == null ? 0 : links.size();
                oneLine.append(linksSize);
            } else {
                ParseData parseData = page.getParseData();
                links = parseData.getOutgoingUrls();
                linksSize = parseData == null ? 0 : links.size();
                oneLine.append(linksSize);
            }

            oneLine.append(",");
            String contentType = page.getContentType().split(";")[0];
            oneLine.append(contentType);
            this.bw2.write(oneLine.toString());
            if (contentType.equals("text/html")) {
                ++this.noOfHtmlFiles;
            } else if (contentType.equals("image/gif")) {
                ++this.noOfGifFiles;
            } else if (contentType.equals("image/tif")) {
                ++this.noOfTifFiles;
            } else if (contentType.equals("image/jpeg")) {
                ++this.noOfJpegFiles;
            } else if (contentType.equals("image/png")) {
                ++this.noOfPngFiles;
            } else if (contentType.equals("application/pdf")) {
                ++this.noOfPdfFiles;
            }
        } catch (IOException var18) {
            ;
        } finally {
            try {
                this.bw2.newLine();
            } catch (Exception var17) {
                ;
            }

        }

    }

    public void onBeforeExit() {
        try {
            this.txt1.write("Fetch Statistics");
            this.txt1.newLine();
            this.txt1.write("================");
            this.txt1.newLine();
            this.txt1.write("# fetches attempted: " + this.noOfFetchesAttempted);
            this.txt1.newLine();
            this.txt1.write("# fetches succeeded: " + this.noOfFetchesSucceeded);
            this.txt1.newLine();
            this.txt1.write("# fetches aborted: " + this.noOfFetchesAborted);
            this.txt1.newLine();
            this.txt1.write("# fetches failed: " + this.noOfFetchesFailed);
            this.txt1.newLine();
            this.txt1.newLine();
            this.txt1.write("Outgoing URLs");
            this.txt1.newLine();
            this.txt1.write("=============");
            this.txt1.newLine();
            this.txt1.write("Total URLs extracted: " + this.noOfUrlsExtracted);
            this.txt1.newLine();
            this.txt1.write("# unique URLs extracted: " + this.noOfUniqueUrls);
            this.txt1.newLine();
            this.txt1.write("# unique URLs within News Site: " + this.noOfUniqueUrlsWithinSite);
            this.txt1.newLine();
            this.txt1.write("# unique URLs outside News Site: " + this.noOfUniqueUrlsOutsideSite);
            this.txt1.newLine();
            this.txt1.newLine();
            this.txt1.write("Status Codes");
            this.txt1.newLine();
            this.txt1.write("============");
            this.txt1.newLine();
            this.txt1.write("200 OK: " + this.noOf200s);
            this.txt1.newLine();
            this.txt1.write("301 Moved Permanently: " + this.noOf301s);
            this.txt1.newLine();
            this.txt1.write("401 Unauthorized: " + this.noOf401s);
            this.txt1.newLine();
            this.txt1.write("403 Forbidden: " + this.noOf403s);
            this.txt1.newLine();
            this.txt1.write("404 Not Found: " + this.noOf404s);
            this.txt1.newLine();
            this.txt1.newLine();
            this.txt1.write("File Sizes");
            this.txt1.newLine();
            this.txt1.write("==========");
            this.txt1.newLine();
            this.txt1.write("< 1KB: " + this.noOfFilesUnder1kb);
            this.txt1.newLine();
            this.txt1.write("1KB ~ <10KB: " + this.noOfFiles1To10kb);
            this.txt1.newLine();
            this.txt1.write("10KB ~ <100KB: " + this.noOfFiles10to100kb);
            this.txt1.newLine();
            this.txt1.write("100KB ~ <1MB: " + this.noOfFiles100to1000kb);
            this.txt1.newLine();
            this.txt1.write(">= 1MB: " + this.noOfFilesOver1000kb);
            this.txt1.newLine();
            this.txt1.newLine();
            this.txt1.write("Content Types");
            this.txt1.newLine();
            this.txt1.write("=============");
            this.txt1.newLine();
            this.txt1.write("text/html: " + this.noOfHtmlFiles);
            this.txt1.newLine();
            this.txt1.write("image/gif: " + this.noOfGifFiles);
            this.txt1.newLine();
            this.txt1.write("image/tif: " + this.noOfTifFiles);
            this.txt1.newLine();
            this.txt1.write("image/jpeg: " + this.noOfJpegFiles);
            this.txt1.newLine();
            this.txt1.write("image/png: " + this.noOfPngFiles);
            this.txt1.newLine();
            this.txt1.write("application/pdf: " + this.noOfPdfFiles);
            this.txt1.newLine();
            this.bw1.close();
            this.bw2.close();
            this.bw3.close();
            this.txt1.close();
        } catch (IOException var2) {
            ;
        }

    }
}
