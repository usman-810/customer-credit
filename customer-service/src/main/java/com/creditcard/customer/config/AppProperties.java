package com.creditcard.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private String name;
    private String version;
    //shafj
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	private Api api = new Api();
    private Pagination pagination = new Pagination();
    
    @Data
    public static class Api {
        private String prefix = "/api";

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
        
        
    }
    
    @Data
    public static class Pagination {
        private int defaultPageSize = 10;
        private int maxPageSize = 100;
        private String defaultSortBy = "id";
        private String defaultSortDir = "ASC";
		public int getDefaultPageSize() {
			return defaultPageSize;
		}
		public void setDefaultPageSize(int defaultPageSize) {
			this.defaultPageSize = defaultPageSize;
		}
		public int getMaxPageSize() {
			return maxPageSize;
		}
		public void setMaxPageSize(int maxPageSize) {
			this.maxPageSize = maxPageSize;
		}
		public String getDefaultSortBy() {
			return defaultSortBy;
		}
		public void setDefaultSortBy(String defaultSortBy) {
			this.defaultSortBy = defaultSortBy;
		}
		public String getDefaultSortDir() {
			return defaultSortDir;
		}
		public void setDefaultSortDir(String defaultSortDir) {
			this.defaultSortDir = defaultSortDir;
		}
        
        
    }
}
