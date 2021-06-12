package com.ghpark.hotalk.security.auth.jwt.extractor;

/**
 * 이 인터페이스는 BASE-64 인코드의 JWT TOKEN으로 표현되어야 함
 * @author pkh879
 */

public interface TokenExtractor {
    public String extract(String payload);
}
