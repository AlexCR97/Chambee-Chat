package com.example.chambeechat.validators;

public class NonEmptyStringValidator extends Validator<String> {

    @Override
    public boolean validate(String s) {
        if (s == null)
            return false;

        if (s.trim().length() == 0)
            return false;

        return true;
    }

}
