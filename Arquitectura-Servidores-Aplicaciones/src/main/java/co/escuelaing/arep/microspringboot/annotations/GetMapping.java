package co.escuelaing.arep.microspringboot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 *
 * @author Juan David Rodríguez
 */
public @interface GetMapping {
    public String value();
}
