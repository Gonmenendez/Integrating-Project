package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.config;

import org.hibernate.collection.spi.PersistentSet;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Configuration
@Component
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<PersistentSet, Set> persistentSetToSetConverter = new AbstractConverter<PersistentSet, Set>() {
            @Override
            protected Set convert(PersistentSet persistentSet) {
                return new HashSet<>(persistentSet);
            }
        };

        modelMapper.addConverter(persistentSetToSetConverter);
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        return modelMapper;
    }

}