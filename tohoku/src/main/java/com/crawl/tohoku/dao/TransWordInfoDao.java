package com.crawl.tohoku.dao;

import com.crawl.tohoku.entity.TransWordInfo;
import com.crawl.tohoku.entity.TransWordInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransWordInfoDao {
    long countByExample(TransWordInfoExample example);

    int deleteByExample(TransWordInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TransWordInfo record);

    int insertSelective(TransWordInfo record);

    List<TransWordInfo> selectByExample(TransWordInfoExample example);

    List<String> selectImageSource(String dictType);

    TransWordInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TransWordInfo record, @Param("example") TransWordInfoExample example);

    int updateByExample(@Param("record") TransWordInfo record, @Param("example") TransWordInfoExample example);

    int updateByPrimaryKeySelective(TransWordInfo record);

    int updateByPrimaryKey(TransWordInfo record);
}