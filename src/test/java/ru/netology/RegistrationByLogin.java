package ru.netology;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Data
@RequiredArgsConstructor
public class RegistrationByLogin {
    private final String login;
    private final String password;
    private final String status;
}
