package com.example.filmview.Filmview.Config;


import com.example.filmview.Filmview.Mapper.JSONMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public JSONMapper getJSONMapper(){return new JSONMapper();}
}
