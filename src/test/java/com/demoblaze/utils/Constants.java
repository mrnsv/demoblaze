package com.demoblaze.utils;

import com.github.javafaker.Faker;


public class Constants {
    private static final Faker faker = new Faker();

    public static final String BASE_URL = "https://www.demoblaze.com/";
    public static final String USERNAME = faker.name().username();
    public static final String PASSWORD = faker.name().bloodGroup();
    public static final int IMPLICIT_WAIT = 10;
}
