package com.crawl.tohoku.entity;

/**
 * CREATE TABLE `trans_word_info` (
 `id`  int(11) NOT NULL AUTO_INCREMENT ,
 `trans_word`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `translation`  varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `dict_type`  int(11) NULL DEFAULT NULL ,
 `source_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 `real_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
 PRIMARY KEY (`id`)
 )
 ENGINE=InnoDB
 DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
 AUTO_INCREMENT=1
 ROW_FORMAT=COMPACT
 ;

 *
 *
 */
public class DictItem {

    String transWord;
    String partOfSpeech;
    String translation;
    String dictType;
    String sourceUrl;
    String realPath;

    public String getTransWord() {
        return transWord;
    }

    public void setTransWord(String transWord) {
        this.transWord = transWord;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    @Override
    public String toString() {
        return "DictItem{" +
                "transWord='" + transWord + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", translation='" + translation + '\'' +
                ", dictType='" + dictType + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", realPath='" + realPath + '\'' +
                '}';
    }
}
