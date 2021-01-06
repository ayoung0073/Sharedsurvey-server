package com.doubleslash.sharedsurvey.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "may")
public class ApplicationYmlRead {
    private String base_dir;

    public void setBase_dir(String base_dir) {
        this.base_dir = base_dir;
    }

    public String getBase_dir() {
        return base_dir;
    }
}
