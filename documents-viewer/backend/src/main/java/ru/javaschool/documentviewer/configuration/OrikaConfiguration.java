package ru.javaschool.documentviewer.configuration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;
import ru.javaschool.documentviewer.documents.store.Document;

@Configuration
public class OrikaConfiguration {

    @Bean
    public MapperFactory mapperFactory() {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        // Register mapping between Document and DocumentDto
        mapperFactory.classMap(Document.class, DocumentDto.class)
                .field("statusCode", "status.code")
                .field("statusName", "status.name")
                .byDefault()
                .register();

        return mapperFactory;
    }

    @Bean
    public MapperFacade documentMapper() {
        return mapperFactory().getMapperFacade();
    }
}
