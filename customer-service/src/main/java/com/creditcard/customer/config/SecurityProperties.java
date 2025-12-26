package com.creditcard.customer.config;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    
    private Jwt jwt = new Jwt();
    
    private List<String> allowedOrigins = new ArrayList<>();
    
    @Data
    public static class Jwt {
        private String secret;
        private long expiration = 86400000; // 24 hours
        private String header = "Authorization";
        private String prefix = "Bearer ";
		public String getSecret() {
			return secret;
		}
		public void setSecret(String secret) {
			this.secret = secret;
		}
		public long getExpiration() {
			return expiration;
		}
		public void setExpiration(long expiration) {
			this.expiration = expiration;
		}
		public String getHeader() {
			return header;
		}
		public void setHeader(String header) {
			this.header = header;
		}
		public String getPrefix() {
			return prefix;
		}
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
        
        
    }
}