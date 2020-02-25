package com.example.chambeechat.validators;

public class PasswordValidator extends Validator<String> {

    @Override
    public boolean validate(String s) {
        if (s == null)
            return false;

        if (s.trim().length() < 6)
            return false;

        return true;
    }
}
