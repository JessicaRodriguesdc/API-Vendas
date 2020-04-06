package io.github.jessica.validation;

import io.github.jessica.validation.constraintvalidation.NotEmptyListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//ser verificada em tempo de execulcao
@Target(ElementType.FIELD)
//onde pode ser colocada a annotation
@Constraint(validatedBy = NotEmptyListValidator.class)
//informa que e uma anotation de validacao
public @interface NotEmptyList {
    String message() default "A lista nao pode ser vazia.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
