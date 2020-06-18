package com.crawl.tohoku.dao;

import com.crawl.tohoku.entity.DictQueryInfo;
import com.crawl.tohoku.entity.DictQueryInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DictQueryInfoDao {
    long countByExample(DictQueryInfoExample example);

    int deleteByExample(DictQueryInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DictQueryInfo record);

    int insertSelective(DictQueryInfo record);

    List<DictQueryInfo> selectByExampleWithBLOBs(DictQueryInfoExample example);

    List<DictQueryInfo> selectByExample(DictQueryInfoExample example);

    DictQueryInfo selectByPrimaryKey(Long id);

    DictQueryInfo selectUniqueByExample(DictQueryInfoExample example);

    int updateByExampleSelective(@Param("record") DictQueryInfo record, @Param("example") DictQueryInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") DictQueryInfo record, @Param("example") DictQueryInfoExample example);

    int updateByExample(@Param("record") DictQueryInfo record, @Param("example") DictQueryInfoExample example);

    int updateByPrimaryKeySelective(DictQueryInfo record);

    int updateByPrimaryKeyWithBLOBs(DictQueryInfo record);

    int updateByPrimaryKey(DictQueryInfo record);
}