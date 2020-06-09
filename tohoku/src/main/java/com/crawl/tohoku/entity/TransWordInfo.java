package com.crawl.tohoku.entity;

import java.io.Serializable;

/**
 * trans_word_info
 * @author Hank
 */
public class TransWordInfo implements Serializable {
    private Long id;

    private String transWord;

    private Integer wordLength;

    /**
     * 第一词性
     */
    private String wordPos;

    /**
     * 第一汉语翻译
     */
    private String transContent;

    /**
     * 优先级 手动添加的优先级最高
     */
    private Integer wordLevel;

    private String translation;

    private String dictType;

    private String sourceUrl;

    private String realPath;

    private Integer dictStatus;

    /**
     * 第二词性
     */
    private String wordPos1;

    /**
     * 第二汉语翻译
     */
    private String transContent1;

    /**
     * 第三词性
     */
    private String wordPos2;

    /**
     * 第三汉语翻译
     */
    private String transContent2;

    /**
     * 第四词性
     */
    private String wordPos3;

    /**
     * 第四汉语翻译
     */
    private String transContent3;

    /**
     * 第五词性
     */
    private String wordPos4;

    /**
     * 第五汉语翻译
     */
    private String transContent4;

    /**
     * 校对人
     */
    private String proofreader;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransWord() {
        return transWord;
    }

    public void setTransWord(String transWord) {
        this.transWord = transWord;
    }

    public Integer getWordLength() {
        return wordLength;
    }

    public void setWordLength(Integer wordLength) {
        this.wordLength = wordLength;
    }

    public String getWordPos() {
        return wordPos;
    }

    public void setWordPos(String wordPos) {
        this.wordPos = wordPos;
    }

    public String getTransContent() {
        return transContent;
    }

    public void setTransContent(String transContent) {
        this.transContent = transContent;
    }

    public Integer getWordLevel() {
        return wordLevel;
    }

    public void setWordLevel(Integer wordLevel) {
        this.wordLevel = wordLevel;
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

    public Integer getDictStatus() {
        return dictStatus;
    }

    public void setDictStatus(Integer dictStatus) {
        this.dictStatus = dictStatus;
    }

    public String getWordPos1() {
        return wordPos1;
    }

    public void setWordPos1(String wordPos1) {
        this.wordPos1 = wordPos1;
    }

    public String getTransContent1() {
        return transContent1;
    }

    public void setTransContent1(String transContent1) {
        this.transContent1 = transContent1;
    }

    public String getWordPos2() {
        return wordPos2;
    }

    public void setWordPos2(String wordPos2) {
        this.wordPos2 = wordPos2;
    }

    public String getTransContent2() {
        return transContent2;
    }

    public void setTransContent2(String transContent2) {
        this.transContent2 = transContent2;
    }

    public String getWordPos3() {
        return wordPos3;
    }

    public void setWordPos3(String wordPos3) {
        this.wordPos3 = wordPos3;
    }

    public String getTransContent3() {
        return transContent3;
    }

    public void setTransContent3(String transContent3) {
        this.transContent3 = transContent3;
    }

    public String getWordPos4() {
        return wordPos4;
    }

    public void setWordPos4(String wordPos4) {
        this.wordPos4 = wordPos4;
    }

    public String getTransContent4() {
        return transContent4;
    }

    public void setTransContent4(String transContent4) {
        this.transContent4 = transContent4;
    }

    public String getProofreader() {
        return proofreader;
    }

    public void setProofreader(String proofreader) {
        this.proofreader = proofreader;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TransWordInfo other = (TransWordInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTransWord() == null ? other.getTransWord() == null : this.getTransWord().equals(other.getTransWord()))
            && (this.getWordLength() == null ? other.getWordLength() == null : this.getWordLength().equals(other.getWordLength()))
            && (this.getWordPos() == null ? other.getWordPos() == null : this.getWordPos().equals(other.getWordPos()))
            && (this.getTransContent() == null ? other.getTransContent() == null : this.getTransContent().equals(other.getTransContent()))
            && (this.getWordLevel() == null ? other.getWordLevel() == null : this.getWordLevel().equals(other.getWordLevel()))
            && (this.getTranslation() == null ? other.getTranslation() == null : this.getTranslation().equals(other.getTranslation()))
            && (this.getDictType() == null ? other.getDictType() == null : this.getDictType().equals(other.getDictType()))
            && (this.getSourceUrl() == null ? other.getSourceUrl() == null : this.getSourceUrl().equals(other.getSourceUrl()))
            && (this.getRealPath() == null ? other.getRealPath() == null : this.getRealPath().equals(other.getRealPath()))
            && (this.getDictStatus() == null ? other.getDictStatus() == null : this.getDictStatus().equals(other.getDictStatus()))
            && (this.getWordPos1() == null ? other.getWordPos1() == null : this.getWordPos1().equals(other.getWordPos1()))
            && (this.getTransContent1() == null ? other.getTransContent1() == null : this.getTransContent1().equals(other.getTransContent1()))
            && (this.getWordPos2() == null ? other.getWordPos2() == null : this.getWordPos2().equals(other.getWordPos2()))
            && (this.getTransContent2() == null ? other.getTransContent2() == null : this.getTransContent2().equals(other.getTransContent2()))
            && (this.getWordPos3() == null ? other.getWordPos3() == null : this.getWordPos3().equals(other.getWordPos3()))
            && (this.getTransContent3() == null ? other.getTransContent3() == null : this.getTransContent3().equals(other.getTransContent3()))
            && (this.getWordPos4() == null ? other.getWordPos4() == null : this.getWordPos4().equals(other.getWordPos4()))
            && (this.getTransContent4() == null ? other.getTransContent4() == null : this.getTransContent4().equals(other.getTransContent4()))
            && (this.getProofreader() == null ? other.getProofreader() == null : this.getProofreader().equals(other.getProofreader()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTransWord() == null) ? 0 : getTransWord().hashCode());
        result = prime * result + ((getWordLength() == null) ? 0 : getWordLength().hashCode());
        result = prime * result + ((getWordPos() == null) ? 0 : getWordPos().hashCode());
        result = prime * result + ((getTransContent() == null) ? 0 : getTransContent().hashCode());
        result = prime * result + ((getWordLevel() == null) ? 0 : getWordLevel().hashCode());
        result = prime * result + ((getTranslation() == null) ? 0 : getTranslation().hashCode());
        result = prime * result + ((getDictType() == null) ? 0 : getDictType().hashCode());
        result = prime * result + ((getSourceUrl() == null) ? 0 : getSourceUrl().hashCode());
        result = prime * result + ((getRealPath() == null) ? 0 : getRealPath().hashCode());
        result = prime * result + ((getDictStatus() == null) ? 0 : getDictStatus().hashCode());
        result = prime * result + ((getWordPos1() == null) ? 0 : getWordPos1().hashCode());
        result = prime * result + ((getTransContent1() == null) ? 0 : getTransContent1().hashCode());
        result = prime * result + ((getWordPos2() == null) ? 0 : getWordPos2().hashCode());
        result = prime * result + ((getTransContent2() == null) ? 0 : getTransContent2().hashCode());
        result = prime * result + ((getWordPos3() == null) ? 0 : getWordPos3().hashCode());
        result = prime * result + ((getTransContent3() == null) ? 0 : getTransContent3().hashCode());
        result = prime * result + ((getWordPos4() == null) ? 0 : getWordPos4().hashCode());
        result = prime * result + ((getTransContent4() == null) ? 0 : getTransContent4().hashCode());
        result = prime * result + ((getProofreader() == null) ? 0 : getProofreader().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", transWord=").append(transWord);
        sb.append(", wordLength=").append(wordLength);
        sb.append(", wordPos=").append(wordPos);
        sb.append(", transContent=").append(transContent);
        sb.append(", wordLevel=").append(wordLevel);
        sb.append(", translation=").append(translation);
        sb.append(", dictType=").append(dictType);
        sb.append(", sourceUrl=").append(sourceUrl);
        sb.append(", realPath=").append(realPath);
        sb.append(", dictStatus=").append(dictStatus);
        sb.append(", wordPos1=").append(wordPos1);
        sb.append(", transContent1=").append(transContent1);
        sb.append(", wordPos2=").append(wordPos2);
        sb.append(", transContent2=").append(transContent2);
        sb.append(", wordPos3=").append(wordPos3);
        sb.append(", transContent3=").append(transContent3);
        sb.append(", wordPos4=").append(wordPos4);
        sb.append(", transContent4=").append(transContent4);
        sb.append(", proofreader=").append(proofreader);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}