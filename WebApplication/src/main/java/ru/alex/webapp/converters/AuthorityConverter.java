package ru.alex.webapp.converters;

import org.springframework.stereotype.Component;
import ru.alex.webapp.model.enums.Authority;

import javax.faces.convert.EnumConverter;

/**
 * @author Alex
 */
@Component
public class AuthorityConverter extends EnumConverter {
    public AuthorityConverter() {
        super(Authority.class);
    }
}
