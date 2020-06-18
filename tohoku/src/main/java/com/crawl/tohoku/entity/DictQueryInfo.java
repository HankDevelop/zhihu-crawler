package com.crawl.tohoku.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * dict_query_info
 * @author 
 */
public class DictQueryInfo implements Serializable {
    /**
     * 流水号
     */
    private Long id;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求参数
     */
    private String requestInfo;

    /**
     * http响应码
     */
    private Integer respCode;

    /**
     * 调用次数
     */
    private Integer calls;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 修改人
     */
    private String modifyId;

    /**
     * 响应body
     */
    private String respTxt;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public Integer getCalls() {
        return calls;
    }

    public void setCalls(Integer calls) {
        this.calls = calls;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getRespTxt() {
        return respTxt;
    }

    public void setRespTxt(String respTxt) {
        this.respTxt = respTxt;
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
        DictQueryInfo other = (DictQueryInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRequestUri() == null ? other.getRequestUri() == null : this.getRequestUri().equals(other.getRequestUri()))
            && (this.getRequestInfo() == null ? other.getRequestInfo() == null : this.getRequestInfo().equals(other.getRequestInfo()))
            && (this.getRespCode() == null ? other.getRespCode() == null : this.getRespCode().equals(other.getRespCode()))
            && (this.getCalls() == null ? other.getCalls() == null : this.getCalls().equals(other.getCalls()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
            && (this.getModifyId() == null ? other.getModifyId() == null : this.getModifyId().equals(other.getModifyId()))
            && (this.getRespTxt() == null ? other.getRespTxt() == null : this.getRespTxt().equals(other.getRespTxt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRequestUri() == null) ? 0 : getRequestUri().hashCode());
        result = prime * result + ((getRequestInfo() == null) ? 0 : getRequestInfo().hashCode());
        result = prime * result + ((getRespCode() == null) ? 0 : getRespCode().hashCode());
        result = prime * result + ((getCalls() == null) ? 0 : getCalls().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getModifyId() == null) ? 0 : getModifyId().hashCode());
        result = prime * result + ((getRespTxt() == null) ? 0 : getRespTxt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", requestUri=").append(requestUri);
        sb.append(", requestInfo=").append(requestInfo);
        sb.append(", respCode=").append(respCode);
        sb.append(", calls=").append(calls);
        sb.append(", createTime=").append(createTime);
        sb.append(", createId=").append(createId);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", modifyId=").append(modifyId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}