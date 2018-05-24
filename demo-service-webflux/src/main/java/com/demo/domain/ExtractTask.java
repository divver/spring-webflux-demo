package com.demo.domain;

import com.google.common.collect.Maps;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.Map;

@RedisHash
public class ExtractTask implements Serializable {
    @Id
    private Long requestId;

    private Map<String, String> extractorMap = Maps.newHashMap();

    @TimeToLive
    private Long timeout;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Map<String, String> getExtractorMap() {
        return extractorMap;
    }

    public void setExtractorMap(Map<String, String> extractorMap) {
        this.extractorMap = extractorMap;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
