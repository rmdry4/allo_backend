package com.ramonda.allo_backend.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.ramonda.allo_backend.util.Unicode;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class WebClientConfig  {

    @Value("${git.username}")
    private static String name;

    public WebClientConfig() {
        
    }

    public String getName() {
        return this.name;
    }

    @Value("${git.username}")
    public void setName(String Nname){
        this.name = Nname;
    }

    public BigDecimal serviceChargePercentage(){
        int serviceCharge = Unicode.sumUnicode(this.name);
        BigDecimal serviceChargePercentage = BigDecimal.valueOf(serviceCharge);
        return serviceChargePercentage;
    }
}
