package com.korges.vavr;

import io.vavr.control.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Introduction to Vavr
 * https://www.baeldung.com/vavr
 */
public class VavrBaeldung {

    // Vavr - OPTION

    /**
     * The main goal of Option is to eliminate null checks in our code by leveraging the Java type system.
     * Option is an object container in Vavr with a similar end goal like Optional in Java 8.
     * Vavr's Option implements Serializable, Iterable, and has a richer API.
     * Since any object reference in Java can have a null value, we usually have to check for nullity
     * with if statements before using it. These checks make the code robust and stable.
     */
    @Test
    public void givenValue_whenNullCheckNeeded_thenCorrect() {
        Object possibleNullObject = null;
        if (possibleNullObject == null) {
            possibleNullObject = "defaultValue";
        }
        assertNotNull(possibleNullObject);
    }

    /**
     * Without checks, the application can crash due to a simple NPE.
     * However, the checks make the code verbose and not so readable,
     * especially when the if statements end up being nested multiple times.
     */
    @Test(expected = NullPointerException.class)
    public void givenValue_whenNullCheckNeeded_thenCorrect2() {
        Object possibleNullObject = null;
        possibleNullObject.toString();
    }

    /**
     * Option solves this problem by totally eliminating nulls and replacing them with a valid object reference
     * for each possible scenario.
     * With Option a null value will evaluate to an instance of None,
     * while a non-null value will evaluate to an instance of Some.
     */
    @Test
    public void givenValue_whenCreatesOption_thenCorrect() {
        Option<Object> noneOption = Option.of(null);
        Option<Object> someOption = Option.of("value");

        assertEquals("None", noneOption.toString());
        assertEquals("Some(value)", someOption.toString());
    }

    /**
     * Therefore, instead of using object values directly,
     * it's advisable to wrap them inside an Option instance as shown above.
     * Notice, that we did not have to do a check before calling toString
     * yet we did not have to deal with a NullPointerException as we had done before.
     * Option's toString returns us meaningful values in each call.
     * In the second snippet of this section, we needed a null check,
     * in which we would assign a default value to the variable, before attempting to use it.
     * Option can deal with this in a single line, even if there is a null:
     */
    @Test
    public void givenValue_whenCreatesOption_thenCorrect2() {
        String name = null;
        Option<String> nameOption = Option.of(name);

        assertEquals("else", nameOption.getOrElse("else"));
    }

    /**
     * Or a non-null:
     */
    @Test
    public void givenValue_whenCreatesOption_thenCorrect3() {
        String name = "notNull";
        Option<String> nameOption = Option.of(name);

        assertEquals("notNull", nameOption.getOrElse("else"));
    }

}
