package com.crawl.tohoku.parser;

import com.crawl.tohoku.TohokuConstants;
import com.crawl.tohoku.entity.TransWordInfo;
import com.github.wycm.common.Page;
import com.github.wycm.common.parser.ListPageParser;
import com.github.wycm.common.util.PhraseTranslateUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * http://hkuri.cneas.tohoku.ac.jp/project1/kdic/listAction页面解析字典条目
 * @author Hank
 */
@Slf4j
@Service
public class Kdic33DictListParser extends DictContentListParser implements ListPageParser<TransWordInfo> {

    @Override
    protected String parseTransWord(Element rowItem) {
        return PhraseTranslateUtils.formatBlankInfo(rowItem.select("td:eq(1)").text());
    }

    @Override
    protected String parseTranslation(Element rowItem) {
        return rowItem.select("td:eq(4)").text();
    }

}
