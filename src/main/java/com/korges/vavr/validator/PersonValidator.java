package com.korges.vavr.validator;

import com.korges.vavr.dto.Person;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class PersonValidator {
    private final String NAME_ERR = "Invalid characters in name: ";
    private final String AGE_ERR = "Age must be at least 0";

    public Validation<Seq<String>, Person> validatePerson(
            String name, int age) {
        return Validation.combine(
                validateName(name), validateAge(age)).ap(Person::new);
    }

    private Validation<String, String> validateName(String name) {
        String invalidChars = name.replaceAll("[a-zA-Z ]", "");
        return invalidChars.isEmpty() ?
                Validation.valid(name)
                : Validation.invalid(NAME_ERR + invalidChars);
    }

    private Validation<String, Integer> validateAge(int age) {
        return age < 0 ? Validation.invalid(AGE_ERR)
                : Validation.valid(age);
    }



}
