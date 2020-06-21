package com.github.wycm.common.parser;


import com.github.wycm.common.Page;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DetailPageParser<T> extends Parser {
    T parseDetailPage(Page page) throws URISyntaxException, IOException;
}
