package com.creditcard.customer.config;



import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CustomErrorDecoder implements ErrorDecoder {
private static final Logger log = LoggerFactory.getLogger(CustomErrorDecoder.class);
    
    private final ErrorDecoder defaultErrorDecoder = new Default();


    @Override
    public Exception decode(String methodKey, Response response) {
    
    
        log.error("Feign client error - Method: {}, Status: {}", methodKey, response.status());
        
        switch (response.status()) {
            case 400:
                return new RuntimeException("Bad Request to " + methodKey);
            case 404:
                return new RuntimeException("Resource not found in " + methodKey);
            case 503:
                return new RuntimeException("Service unavailable: " + methodKey);
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}