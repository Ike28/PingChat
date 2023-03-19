package com.map222.sfmc.socialnetworkfx.domain.validators;


import com.map222.sfmc.socialnetworkfx.domain.validators.exceptions.ValidationException;

/**
 * An interface for business-object-specific validators
 * @param <T> -entity to be validated
 */
public interface Validator<T> {

    /**
     * Checks for validity of an object, otherwise throws an exception
     * @param entity -object to be validated
     * @throws ValidationException if entity contains unwanted/invalid attributes
     */
    void validate(T entity) throws ValidationException;
}
