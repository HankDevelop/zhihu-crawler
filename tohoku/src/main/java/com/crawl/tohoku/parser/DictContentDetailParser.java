package com.crawl.tohoku.parser;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.TohokuHttpClient;
import com.crawl.tohoku.entity.TransWordInfo;
import com.github.wycm.common.Page;
import com.github.wycm.common.parser.ListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * http://hkuri.cneas.tohoku.ac.jp/project1/manchu/listAction页面解析字典条目
 */
@Component("dictContentDetailParser")
public class DictContentDetailParser implements ListPageParser<TransWordInfo> {

    /*private static DictContentDetailParser instance;

    public static DictContentDetailParser getInstance() {
        if (instance == null) {
            synchronized (TohokuHttpClient.class) {
                if (instance == null) {
                    instance = new DictContentDetailParser();
                }
            }
        }
        return instance;
    }

    private DictContentDetailParser() {
    }*/

    @Override
    public List<TransWordInfo> parseListPage(Page page) {
        Document dc = Jsoup.parse(page.getHtml());
        Elements contentList = dc.select("table.table-hover tbody > *");
        if (contentList == null || contentList.isEmpty()) {
            return null;
        }
        List<TransWordInfo> transWordInfos = new ArrayList<>();
        String tmpStr = "";
        for (Element rowItem : contentList) {
            TransWordInfo transWordInfo = new TransWordInfo();
            transWordInfo.setTransWord(rowItem.select("td:eq(2)").text());
            transWordInfo.setTranslation(rowItem.select("td:eq(4)").text());
            tmpStr = rowItem.select("td:eq(5) a[onclick]").attr("onclick");
            if (tmpStr.startsWith("loadDicImage")) {
                String dictType = tmpStr.substring(tmpStr.indexOf("(") + 1, tmpStr.indexOf(","));
                String srcId = tmpStr.substring(tmpStr.indexOf("'") + 1, tmpStr.lastIndexOf("'"));
                String sourceUrl = String.format(TohokuConstants.DOWNLOAD_URL, dictType, srcId);
                transWordInfo.setSourceUrl(sourceUrl);
                transWordInfo.setDictType(dictType);
                transWordInfo.setRealPath(srcId);
            } else {
                transWordInfo.setSourceUrl("");
                transWordInfo.setDictType("");
                transWordInfo.setRealPath("");
            }
            transWordInfos.add(transWordInfo);
        }
        return transWordInfos;
    }


}
