package com.deliverytech.delivery.api.validation;

import java.lang.annotation.*;

import com.deliverytech.delivery.api.validation.validator.CategoriaValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CategoriaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoriaValida {
    String message() default "Categoria inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default{};

}

