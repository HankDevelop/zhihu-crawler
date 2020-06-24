package com.crawl.tohoku.parser;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.entity.TransWordInfo;
import com.crawl.tohoku.entity.TransWordInfoExample;
import com.crawl.tohoku.service.TohokuComponent;
import com.github.wycm.common.Page;
import com.github.wycm.common.parser.ListPageParser;
import com.github.wycm.common.util.PhraseTranslateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class DictContentListParser implements ListPageParser<TransWordInfo> {

    public static final String LOAD_DIC_IMAGE = "loadDicImage";
    @Autowired
    private TohokuComponent tohokuComponent;

    @Override
    public List<TransWordInfo> parseListPage(Page page) {
        Document dc = Jsoup.parse(page.getHtml());
        Elements contentList = dc.select("table.table-hover tbody > *");
        if (contentList == null || contentList.isEmpty()) {
            return null;
        }
        List<TransWordInfo> transWordInfos = new ArrayList<>();
        for (Element rowItem : contentList) {
            TransWordInfo transWordInfo = new TransWordInfo();
            transWordInfo.setTransWord(parseTransWord(rowItem));
            transWordInfo.setTranslation(parseTranslation(rowItem));
            parseDictImageInfo(rowItem, transWordInfo);
            persistenceTransWordInfo(transWordInfo);
            transWordInfos.add(transWordInfo);
        }
        return transWordInfos;
    }

    protected abstract String parseTranslation(Element rowItem);

    protected abstract String parseTransWord(Element rowItem);

    protected void parseDictImageInfo(Element rowItem, TransWordInfo transWordInfo) {
        String tmpStr = rowItem.select("td:eq(5) a[onclick]").attr("onclick");
        if (tmpStr.startsWith(LOAD_DIC_IMAGE)) {
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
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void persistenceTransWordInfo(TransWordInfo transWordInfo) {
        TransWordInfoExample transWordInfoExample = new TransWordInfoExample();
        TransWordInfoExample.Criteria criteria = transWordInfoExample.createCriteria();
        if (StringUtils.isNotBlank(transWordInfo.getTransWord())) {
            criteria.andTransWordEqualTo(transWordInfo.getTransWord());
            transWordInfo.setWordLength(transWordInfo.getTransWord().split(" ").length);
        }
        if (StringUtils.isNotBlank(transWordInfo.getDictType())) {
            criteria.andDictTypeEqualTo(transWordInfo.getDictType());
        }
        if (StringUtils.isNotBlank(transWordInfo.getTranslation())) {
            criteria.andTranslationEqualTo(transWordInfo.getTranslation());
            transWordInfo.setWordPos(PhraseTranslateUtils.parseWordPos(transWordInfo.getTranslation()));
            transWordInfo.setTransContent(PhraseTranslateUtils.parseDictTranslation(transWordInfo.getTranslation()));
        }
        List<TransWordInfo> transWordInfos = tohokuComponent.getTransWordInfoDao().selectByExample(transWordInfoExample);
        if (Objects.isNull(transWordInfos) || transWordInfos.isEmpty()) {
            transWordInfo.setDictStatus(0);
            transWordInfo.setCreateTime(new Date());
            tohokuComponent.getTransWordInfoDao().insertSelective(transWordInfo);
        }
        log.debug(transWordInfo.toString());
    }
}
