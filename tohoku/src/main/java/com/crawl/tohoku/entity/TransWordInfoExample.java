package com.crawl.tohoku.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransWordInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public TransWordInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTransWordIsNull() {
            addCriterion("trans_word is null");
            return (Criteria) this;
        }

        public Criteria andTransWordIsNotNull() {
            addCriterion("trans_word is not null");
            return (Criteria) this;
        }

        public Criteria andTransWordEqualTo(String value) {
            addCriterion("trans_word =", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordNotEqualTo(String value) {
            addCriterion("trans_word <>", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordGreaterThan(String value) {
            addCriterion("trans_word >", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordGreaterThanOrEqualTo(String value) {
            addCriterion("trans_word >=", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordLessThan(String value) {
            addCriterion("trans_word <", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordLessThanOrEqualTo(String value) {
            addCriterion("trans_word <=", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordLike(String value) {
            addCriterion("trans_word like", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordNotLike(String value) {
            addCriterion("trans_word not like", value, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordIn(List<String> values) {
            addCriterion("trans_word in", values, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordNotIn(List<String> values) {
            addCriterion("trans_word not in", values, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordBetween(String value1, String value2) {
            addCriterion("trans_word between", value1, value2, "transWord");
            return (Criteria) this;
        }

        public Criteria andTransWordNotBetween(String value1, String value2) {
            addCriterion("trans_word not between", value1, value2, "transWord");
            return (Criteria) this;
        }

        public Criteria andWordLengthIsNull() {
            addCriterion("word_length is null");
            return (Criteria) this;
        }

        public Criteria andWordLengthIsNotNull() {
            addCriterion("word_length is not null");
            return (Criteria) this;
        }

        public Criteria andWordLengthEqualTo(Integer value) {
            addCriterion("word_length =", value, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthNotEqualTo(Integer value) {
            addCriterion("word_length <>", value, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthGreaterThan(Integer value) {
            addCriterion("word_length >", value, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthGreaterThanOrEqualTo(Integer value) {
            addCriterion("word_length >=", value, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthLessThan(Integer value) {
            addCriterion("word_length <", value, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthLessThanOrEqualTo(Integer value) {
            addCriterion("word_length <=", value, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthIn(List<Integer> values) {
            addCriterion("word_length in", values, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthNotIn(List<Integer> values) {
            addCriterion("word_length not in", values, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthBetween(Integer value1, Integer value2) {
            addCriterion("word_length between", value1, value2, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordLengthNotBetween(Integer value1, Integer value2) {
            addCriterion("word_length not between", value1, value2, "wordLength");
            return (Criteria) this;
        }

        public Criteria andWordPosIsNull() {
            addCriterion("word_pos is null");
            return (Criteria) this;
        }

        public Criteria andWordPosIsNotNull() {
            addCriterion("word_pos is not null");
            return (Criteria) this;
        }

        public Criteria andWordPosEqualTo(String value) {
            addCriterion("word_pos =", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosNotEqualTo(String value) {
            addCriterion("word_pos <>", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosGreaterThan(String value) {
            addCriterion("word_pos >", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosGreaterThanOrEqualTo(String value) {
            addCriterion("word_pos >=", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosLessThan(String value) {
            addCriterion("word_pos <", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosLessThanOrEqualTo(String value) {
            addCriterion("word_pos <=", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosLike(String value) {
            addCriterion("word_pos like", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosNotLike(String value) {
            addCriterion("word_pos not like", value, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosIn(List<String> values) {
            addCriterion("word_pos in", values, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosNotIn(List<String> values) {
            addCriterion("word_pos not in", values, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosBetween(String value1, String value2) {
            addCriterion("word_pos between", value1, value2, "wordPos");
            return (Criteria) this;
        }

        public Criteria andWordPosNotBetween(String value1, String value2) {
            addCriterion("word_pos not between", value1, value2, "wordPos");
            return (Criteria) this;
        }

        public Criteria andTransContentIsNull() {
            addCriterion("trans_content is null");
            return (Criteria) this;
        }

        public Criteria andTransContentIsNotNull() {
            addCriterion("trans_content is not null");
            return (Criteria) this;
        }

        public Criteria andTransContentEqualTo(String value) {
            addCriterion("trans_content =", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentNotEqualTo(String value) {
            addCriterion("trans_content <>", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentGreaterThan(String value) {
            addCriterion("trans_content >", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentGreaterThanOrEqualTo(String value) {
            addCriterion("trans_content >=", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentLessThan(String value) {
            addCriterion("trans_content <", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentLessThanOrEqualTo(String value) {
            addCriterion("trans_content <=", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentLike(String value) {
            addCriterion("trans_content like", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentNotLike(String value) {
            addCriterion("trans_content not like", value, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentIn(List<String> values) {
            addCriterion("trans_content in", values, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentNotIn(List<String> values) {
            addCriterion("trans_content not in", values, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentBetween(String value1, String value2) {
            addCriterion("trans_content between", value1, value2, "transContent");
            return (Criteria) this;
        }

        public Criteria andTransContentNotBetween(String value1, String value2) {
            addCriterion("trans_content not between", value1, value2, "transContent");
            return (Criteria) this;
        }

        public Criteria andWordLevelIsNull() {
            addCriterion("word_level is null");
            return (Criteria) this;
        }

        public Criteria andWordLevelIsNotNull() {
            addCriterion("word_level is not null");
            return (Criteria) this;
        }

        public Criteria andWordLevelEqualTo(Integer value) {
            addCriterion("word_level =", value, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelNotEqualTo(Integer value) {
            addCriterion("word_level <>", value, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelGreaterThan(Integer value) {
            addCriterion("word_level >", value, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("word_level >=", value, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelLessThan(Integer value) {
            addCriterion("word_level <", value, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelLessThanOrEqualTo(Integer value) {
            addCriterion("word_level <=", value, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelIn(List<Integer> values) {
            addCriterion("word_level in", values, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelNotIn(List<Integer> values) {
            addCriterion("word_level not in", values, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelBetween(Integer value1, Integer value2) {
            addCriterion("word_level between", value1, value2, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andWordLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("word_level not between", value1, value2, "wordLevel");
            return (Criteria) this;
        }

        public Criteria andTranslationIsNull() {
            addCriterion("`translation` is null");
            return (Criteria) this;
        }

        public Criteria andTranslationIsNotNull() {
            addCriterion("`translation` is not null");
            return (Criteria) this;
        }

        public Criteria andTranslationEqualTo(String value) {
            addCriterion("`translation` =", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationNotEqualTo(String value) {
            addCriterion("`translation` <>", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationGreaterThan(String value) {
            addCriterion("`translation` >", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationGreaterThanOrEqualTo(String value) {
            addCriterion("`translation` >=", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationLessThan(String value) {
            addCriterion("`translation` <", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationLessThanOrEqualTo(String value) {
            addCriterion("`translation` <=", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationLike(String value) {
            addCriterion("`translation` like", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationNotLike(String value) {
            addCriterion("`translation` not like", value, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationIn(List<String> values) {
            addCriterion("`translation` in", values, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationNotIn(List<String> values) {
            addCriterion("`translation` not in", values, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationBetween(String value1, String value2) {
            addCriterion("`translation` between", value1, value2, "translation");
            return (Criteria) this;
        }

        public Criteria andTranslationNotBetween(String value1, String value2) {
            addCriterion("`translation` not between", value1, value2, "translation");
            return (Criteria) this;
        }

        public Criteria andDictTypeIsNull() {
            addCriterion("dict_type is null");
            return (Criteria) this;
        }

        public Criteria andDictTypeIsNotNull() {
            addCriterion("dict_type is not null");
            return (Criteria) this;
        }

        public Criteria andDictTypeEqualTo(String value) {
            addCriterion("dict_type =", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeNotEqualTo(String value) {
            addCriterion("dict_type <>", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeGreaterThan(String value) {
            addCriterion("dict_type >", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeGreaterThanOrEqualTo(String value) {
            addCriterion("dict_type >=", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeLessThan(String value) {
            addCriterion("dict_type <", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeLessThanOrEqualTo(String value) {
            addCriterion("dict_type <=", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeLike(String value) {
            addCriterion("dict_type like", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeNotLike(String value) {
            addCriterion("dict_type not like", value, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeIn(List<String> values) {
            addCriterion("dict_type in", values, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeNotIn(List<String> values) {
            addCriterion("dict_type not in", values, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeBetween(String value1, String value2) {
            addCriterion("dict_type between", value1, value2, "dictType");
            return (Criteria) this;
        }

        public Criteria andDictTypeNotBetween(String value1, String value2) {
            addCriterion("dict_type not between", value1, value2, "dictType");
            return (Criteria) this;
        }

        public Criteria andSourceUrlIsNull() {
            addCriterion("source_url is null");
            return (Criteria) this;
        }

        public Criteria andSourceUrlIsNotNull() {
            addCriterion("source_url is not null");
            return (Criteria) this;
        }

        public Criteria andSourceUrlEqualTo(String value) {
            addCriterion("source_url =", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotEqualTo(String value) {
            addCriterion("source_url <>", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlGreaterThan(String value) {
            addCriterion("source_url >", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlGreaterThanOrEqualTo(String value) {
            addCriterion("source_url >=", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlLessThan(String value) {
            addCriterion("source_url <", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlLessThanOrEqualTo(String value) {
            addCriterion("source_url <=", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlLike(String value) {
            addCriterion("source_url like", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotLike(String value) {
            addCriterion("source_url not like", value, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlIn(List<String> values) {
            addCriterion("source_url in", values, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotIn(List<String> values) {
            addCriterion("source_url not in", values, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlBetween(String value1, String value2) {
            addCriterion("source_url between", value1, value2, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andSourceUrlNotBetween(String value1, String value2) {
            addCriterion("source_url not between", value1, value2, "sourceUrl");
            return (Criteria) this;
        }

        public Criteria andRealPathIsNull() {
            addCriterion("real_path is null");
            return (Criteria) this;
        }

        public Criteria andRealPathIsNotNull() {
            addCriterion("real_path is not null");
            return (Criteria) this;
        }

        public Criteria andRealPathEqualTo(String value) {
            addCriterion("real_path =", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathNotEqualTo(String value) {
            addCriterion("real_path <>", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathGreaterThan(String value) {
            addCriterion("real_path >", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathGreaterThanOrEqualTo(String value) {
            addCriterion("real_path >=", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathLessThan(String value) {
            addCriterion("real_path <", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathLessThanOrEqualTo(String value) {
            addCriterion("real_path <=", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathLike(String value) {
            addCriterion("real_path like", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathNotLike(String value) {
            addCriterion("real_path not like", value, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathIn(List<String> values) {
            addCriterion("real_path in", values, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathNotIn(List<String> values) {
            addCriterion("real_path not in", values, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathBetween(String value1, String value2) {
            addCriterion("real_path between", value1, value2, "realPath");
            return (Criteria) this;
        }

        public Criteria andRealPathNotBetween(String value1, String value2) {
            addCriterion("real_path not between", value1, value2, "realPath");
            return (Criteria) this;
        }

        public Criteria andDictStatusIsNull() {
            addCriterion("dict_status is null");
            return (Criteria) this;
        }

        public Criteria andDictStatusIsNotNull() {
            addCriterion("dict_status is not null");
            return (Criteria) this;
        }

        public Criteria andDictStatusEqualTo(Integer value) {
            addCriterion("dict_status =", value, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusNotEqualTo(Integer value) {
            addCriterion("dict_status <>", value, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusGreaterThan(Integer value) {
            addCriterion("dict_status >", value, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("dict_status >=", value, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusLessThan(Integer value) {
            addCriterion("dict_status <", value, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusLessThanOrEqualTo(Integer value) {
            addCriterion("dict_status <=", value, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusIn(List<Integer> values) {
            addCriterion("dict_status in", values, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusNotIn(List<Integer> values) {
            addCriterion("dict_status not in", values, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusBetween(Integer value1, Integer value2) {
            addCriterion("dict_status between", value1, value2, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andDictStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("dict_status not between", value1, value2, "dictStatus");
            return (Criteria) this;
        }

        public Criteria andWordPos1IsNull() {
            addCriterion("word_pos1 is null");
            return (Criteria) this;
        }

        public Criteria andWordPos1IsNotNull() {
            addCriterion("word_pos1 is not null");
            return (Criteria) this;
        }

        public Criteria andWordPos1EqualTo(String value) {
            addCriterion("word_pos1 =", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1NotEqualTo(String value) {
            addCriterion("word_pos1 <>", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1GreaterThan(String value) {
            addCriterion("word_pos1 >", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1GreaterThanOrEqualTo(String value) {
            addCriterion("word_pos1 >=", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1LessThan(String value) {
            addCriterion("word_pos1 <", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1LessThanOrEqualTo(String value) {
            addCriterion("word_pos1 <=", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1Like(String value) {
            addCriterion("word_pos1 like", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1NotLike(String value) {
            addCriterion("word_pos1 not like", value, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1In(List<String> values) {
            addCriterion("word_pos1 in", values, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1NotIn(List<String> values) {
            addCriterion("word_pos1 not in", values, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1Between(String value1, String value2) {
            addCriterion("word_pos1 between", value1, value2, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andWordPos1NotBetween(String value1, String value2) {
            addCriterion("word_pos1 not between", value1, value2, "wordPos1");
            return (Criteria) this;
        }

        public Criteria andTransContent1IsNull() {
            addCriterion("trans_content1 is null");
            return (Criteria) this;
        }

        public Criteria andTransContent1IsNotNull() {
            addCriterion("trans_content1 is not null");
            return (Criteria) this;
        }

        public Criteria andTransContent1EqualTo(String value) {
            addCriterion("trans_content1 =", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1NotEqualTo(String value) {
            addCriterion("trans_content1 <>", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1GreaterThan(String value) {
            addCriterion("trans_content1 >", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1GreaterThanOrEqualTo(String value) {
            addCriterion("trans_content1 >=", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1LessThan(String value) {
            addCriterion("trans_content1 <", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1LessThanOrEqualTo(String value) {
            addCriterion("trans_content1 <=", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1Like(String value) {
            addCriterion("trans_content1 like", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1NotLike(String value) {
            addCriterion("trans_content1 not like", value, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1In(List<String> values) {
            addCriterion("trans_content1 in", values, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1NotIn(List<String> values) {
            addCriterion("trans_content1 not in", values, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1Between(String value1, String value2) {
            addCriterion("trans_content1 between", value1, value2, "transContent1");
            return (Criteria) this;
        }

        public Criteria andTransContent1NotBetween(String value1, String value2) {
            addCriterion("trans_content1 not between", value1, value2, "transContent1");
            return (Criteria) this;
        }

        public Criteria andWordPos2IsNull() {
            addCriterion("word_pos2 is null");
            return (Criteria) this;
        }

        public Criteria andWordPos2IsNotNull() {
            addCriterion("word_pos2 is not null");
            return (Criteria) this;
        }

        public Criteria andWordPos2EqualTo(String value) {
            addCriterion("word_pos2 =", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2NotEqualTo(String value) {
            addCriterion("word_pos2 <>", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2GreaterThan(String value) {
            addCriterion("word_pos2 >", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2GreaterThanOrEqualTo(String value) {
            addCriterion("word_pos2 >=", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2LessThan(String value) {
            addCriterion("word_pos2 <", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2LessThanOrEqualTo(String value) {
            addCriterion("word_pos2 <=", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2Like(String value) {
            addCriterion("word_pos2 like", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2NotLike(String value) {
            addCriterion("word_pos2 not like", value, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2In(List<String> values) {
            addCriterion("word_pos2 in", values, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2NotIn(List<String> values) {
            addCriterion("word_pos2 not in", values, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2Between(String value1, String value2) {
            addCriterion("word_pos2 between", value1, value2, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andWordPos2NotBetween(String value1, String value2) {
            addCriterion("word_pos2 not between", value1, value2, "wordPos2");
            return (Criteria) this;
        }

        public Criteria andTransContent2IsNull() {
            addCriterion("trans_content2 is null");
            return (Criteria) this;
        }

        public Criteria andTransContent2IsNotNull() {
            addCriterion("trans_content2 is not null");
            return (Criteria) this;
        }

        public Criteria andTransContent2EqualTo(String value) {
            addCriterion("trans_content2 =", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2NotEqualTo(String value) {
            addCriterion("trans_content2 <>", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2GreaterThan(String value) {
            addCriterion("trans_content2 >", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2GreaterThanOrEqualTo(String value) {
            addCriterion("trans_content2 >=", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2LessThan(String value) {
            addCriterion("trans_content2 <", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2LessThanOrEqualTo(String value) {
            addCriterion("trans_content2 <=", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2Like(String value) {
            addCriterion("trans_content2 like", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2NotLike(String value) {
            addCriterion("trans_content2 not like", value, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2In(List<String> values) {
            addCriterion("trans_content2 in", values, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2NotIn(List<String> values) {
            addCriterion("trans_content2 not in", values, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2Between(String value1, String value2) {
            addCriterion("trans_content2 between", value1, value2, "transContent2");
            return (Criteria) this;
        }

        public Criteria andTransContent2NotBetween(String value1, String value2) {
            addCriterion("trans_content2 not between", value1, value2, "transContent2");
            return (Criteria) this;
        }

        public Criteria andWordPos3IsNull() {
            addCriterion("word_pos3 is null");
            return (Criteria) this;
        }

        public Criteria andWordPos3IsNotNull() {
            addCriterion("word_pos3 is not null");
            return (Criteria) this;
        }

        public Criteria andWordPos3EqualTo(String value) {
            addCriterion("word_pos3 =", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3NotEqualTo(String value) {
            addCriterion("word_pos3 <>", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3GreaterThan(String value) {
            addCriterion("word_pos3 >", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3GreaterThanOrEqualTo(String value) {
            addCriterion("word_pos3 >=", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3LessThan(String value) {
            addCriterion("word_pos3 <", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3LessThanOrEqualTo(String value) {
            addCriterion("word_pos3 <=", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3Like(String value) {
            addCriterion("word_pos3 like", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3NotLike(String value) {
            addCriterion("word_pos3 not like", value, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3In(List<String> values) {
            addCriterion("word_pos3 in", values, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3NotIn(List<String> values) {
            addCriterion("word_pos3 not in", values, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3Between(String value1, String value2) {
            addCriterion("word_pos3 between", value1, value2, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andWordPos3NotBetween(String value1, String value2) {
            addCriterion("word_pos3 not between", value1, value2, "wordPos3");
            return (Criteria) this;
        }

        public Criteria andTransContent3IsNull() {
            addCriterion("trans_content3 is null");
            return (Criteria) this;
        }

        public Criteria andTransContent3IsNotNull() {
            addCriterion("trans_content3 is not null");
            return (Criteria) this;
        }

        public Criteria andTransContent3EqualTo(String value) {
            addCriterion("trans_content3 =", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3NotEqualTo(String value) {
            addCriterion("trans_content3 <>", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3GreaterThan(String value) {
            addCriterion("trans_content3 >", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3GreaterThanOrEqualTo(String value) {
            addCriterion("trans_content3 >=", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3LessThan(String value) {
            addCriterion("trans_content3 <", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3LessThanOrEqualTo(String value) {
            addCriterion("trans_content3 <=", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3Like(String value) {
            addCriterion("trans_content3 like", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3NotLike(String value) {
            addCriterion("trans_content3 not like", value, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3In(List<String> values) {
            addCriterion("trans_content3 in", values, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3NotIn(List<String> values) {
            addCriterion("trans_content3 not in", values, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3Between(String value1, String value2) {
            addCriterion("trans_content3 between", value1, value2, "transContent3");
            return (Criteria) this;
        }

        public Criteria andTransContent3NotBetween(String value1, String value2) {
            addCriterion("trans_content3 not between", value1, value2, "transContent3");
            return (Criteria) this;
        }

        public Criteria andWordPos4IsNull() {
            addCriterion("word_pos4 is null");
            return (Criteria) this;
        }

        public Criteria andWordPos4IsNotNull() {
            addCriterion("word_pos4 is not null");
            return (Criteria) this;
        }

        public Criteria andWordPos4EqualTo(String value) {
            addCriterion("word_pos4 =", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4NotEqualTo(String value) {
            addCriterion("word_pos4 <>", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4GreaterThan(String value) {
            addCriterion("word_pos4 >", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4GreaterThanOrEqualTo(String value) {
            addCriterion("word_pos4 >=", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4LessThan(String value) {
            addCriterion("word_pos4 <", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4LessThanOrEqualTo(String value) {
            addCriterion("word_pos4 <=", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4Like(String value) {
            addCriterion("word_pos4 like", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4NotLike(String value) {
            addCriterion("word_pos4 not like", value, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4In(List<String> values) {
            addCriterion("word_pos4 in", values, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4NotIn(List<String> values) {
            addCriterion("word_pos4 not in", values, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4Between(String value1, String value2) {
            addCriterion("word_pos4 between", value1, value2, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andWordPos4NotBetween(String value1, String value2) {
            addCriterion("word_pos4 not between", value1, value2, "wordPos4");
            return (Criteria) this;
        }

        public Criteria andTransContent4IsNull() {
            addCriterion("trans_content4 is null");
            return (Criteria) this;
        }

        public Criteria andTransContent4IsNotNull() {
            addCriterion("trans_content4 is not null");
            return (Criteria) this;
        }

        public Criteria andTransContent4EqualTo(String value) {
            addCriterion("trans_content4 =", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4NotEqualTo(String value) {
            addCriterion("trans_content4 <>", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4GreaterThan(String value) {
            addCriterion("trans_content4 >", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4GreaterThanOrEqualTo(String value) {
            addCriterion("trans_content4 >=", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4LessThan(String value) {
            addCriterion("trans_content4 <", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4LessThanOrEqualTo(String value) {
            addCriterion("trans_content4 <=", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4Like(String value) {
            addCriterion("trans_content4 like", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4NotLike(String value) {
            addCriterion("trans_content4 not like", value, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4In(List<String> values) {
            addCriterion("trans_content4 in", values, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4NotIn(List<String> values) {
            addCriterion("trans_content4 not in", values, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4Between(String value1, String value2) {
            addCriterion("trans_content4 between", value1, value2, "transContent4");
            return (Criteria) this;
        }

        public Criteria andTransContent4NotBetween(String value1, String value2) {
            addCriterion("trans_content4 not between", value1, value2, "transContent4");
            return (Criteria) this;
        }

        public Criteria andProofreaderIsNull() {
            addCriterion("proofreader is null");
            return (Criteria) this;
        }

        public Criteria andProofreaderIsNotNull() {
            addCriterion("proofreader is not null");
            return (Criteria) this;
        }

        public Criteria andProofreaderEqualTo(String value) {
            addCriterion("proofreader =", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderNotEqualTo(String value) {
            addCriterion("proofreader <>", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderGreaterThan(String value) {
            addCriterion("proofreader >", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderGreaterThanOrEqualTo(String value) {
            addCriterion("proofreader >=", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderLessThan(String value) {
            addCriterion("proofreader <", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderLessThanOrEqualTo(String value) {
            addCriterion("proofreader <=", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderLike(String value) {
            addCriterion("proofreader like", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderNotLike(String value) {
            addCriterion("proofreader not like", value, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderIn(List<String> values) {
            addCriterion("proofreader in", values, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderNotIn(List<String> values) {
            addCriterion("proofreader not in", values, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderBetween(String value1, String value2) {
            addCriterion("proofreader between", value1, value2, "proofreader");
            return (Criteria) this;
        }

        public Criteria andProofreaderNotBetween(String value1, String value2) {
            addCriterion("proofreader not between", value1, value2, "proofreader");
            return (Criteria) this;
        }

        public Criteria andCreateIdIsNull() {
            addCriterion("create_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateIdIsNotNull() {
            addCriterion("create_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateIdEqualTo(Long value) {
            addCriterion("create_id =", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotEqualTo(Long value) {
            addCriterion("create_id <>", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThan(Long value) {
            addCriterion("create_id >", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThanOrEqualTo(Long value) {
            addCriterion("create_id >=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThan(Long value) {
            addCriterion("create_id <", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThanOrEqualTo(Long value) {
            addCriterion("create_id <=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdIn(List<Long> values) {
            addCriterion("create_id in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotIn(List<Long> values) {
            addCriterion("create_id not in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdBetween(Long value1, Long value2) {
            addCriterion("create_id between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotBetween(Long value1, Long value2) {
            addCriterion("create_id not between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andModifyIdIsNull() {
            addCriterion("modify_id is null");
            return (Criteria) this;
        }

        public Criteria andModifyIdIsNotNull() {
            addCriterion("modify_id is not null");
            return (Criteria) this;
        }

        public Criteria andModifyIdEqualTo(Long value) {
            addCriterion("modify_id =", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotEqualTo(Long value) {
            addCriterion("modify_id <>", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdGreaterThan(Long value) {
            addCriterion("modify_id >", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("modify_id >=", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLessThan(Long value) {
            addCriterion("modify_id <", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLessThanOrEqualTo(Long value) {
            addCriterion("modify_id <=", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdIn(List<Long> values) {
            addCriterion("modify_id in", values, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotIn(List<Long> values) {
            addCriterion("modify_id not in", values, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdBetween(Long value1, Long value2) {
            addCriterion("modify_id between", value1, value2, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotBetween(Long value1, Long value2) {
            addCriterion("modify_id not between", value1, value2, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}