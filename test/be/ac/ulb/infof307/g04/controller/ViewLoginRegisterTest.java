package be.ac.ulb.infof307.g04.controller;


import be.ac.ulb.infof307.g04.model.DatabaseArticle;
import be.ac.ulb.infof307.g04.model.DatabaseUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.xml.sax.SAXException;

import javax.xml.crypto.Data;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ViewLoginRegisterTest {

    private String valid_username;
    private String valid_password;

    private ViewLoginRegister loginregister = new ViewLoginRegister();

    @BeforeAll
    void setupBeforeArticleVerification() throws IOException, ParserConfigurationException, SAXException, ParseException {
        valid_username = "username";
        valid_password = "password";
    }


    @Test
    void login_valid_input_false() throws IOException, ParserConfigurationException, SAXException, ParseException {
        String no_username = "";
        String no_password = "";
        assertFalse(loginregister.login_inputs_valids(no_username,no_password)); // two empty inputs
        assertFalse(loginregister.login_inputs_valids(valid_username,no_password)); // one valid username and no password
        assertFalse(loginregister.login_inputs_valids(no_username,valid_password)); // no username and a valid password
    }

    @Test
    void login_valid_input_true() throws IOException, ParserConfigurationException, SAXException, ParseException {
        assertTrue(loginregister.login_inputs_valids(valid_username,valid_password));
    }





    @Test
    void test_register_valid_input_false() throws IOException, ParserConfigurationException, SAXException, ParseException {
        String no_username = "";
        String no_password = "";
        String invalid_username1 = "user"; // invalid because it has less than 5 caracters
        String invalid_password1 = "p"; // invalid because it has less than 5 caracters
        String not_same_valid_password = "password2";


        // case no inputs
        assertFalse( loginregister.register_inputs_valids(no_username,no_password,no_password) ); // no inputs
        assertFalse( loginregister.register_inputs_valids(valid_username,no_password,no_password) ); // valid username and no password
        assertFalse(loginregister.register_inputs_valids(no_username,valid_password,valid_password) ); // no username and valid matching passwords

        // no matching passwords
        assertFalse(loginregister.register_inputs_valids(valid_username,valid_password,not_same_valid_password) ); // no username and valid matching passwords

    }



    @Test
    void register_valid_input_true() throws IOException, ParserConfigurationException, SAXException, ParseException {
        assertTrue(loginregister.register_inputs_valids(valid_username,valid_password,valid_password));
    }



    @Test
    void test_register_input_too_much_char_false() throws IOException, ParserConfigurationException, SAXException, ParseException {
        String invalid_username2 = "username_way_too_long"; // invalid because it has more than 17 caracters
        String invalid_password2 = "password_way_too_long"; // invalid because it has more than 17 caracters

        //too much caracters
        assertFalse( loginregister.register_inputs_valids(invalid_username2,invalid_password2,invalid_password2) ); // too much characters (username and password)
        assertFalse( loginregister.register_inputs_valids(invalid_username2,valid_password,valid_password) ); // too much characters (username)
        assertFalse( loginregister.register_inputs_valids(valid_username,invalid_password2,invalid_password2) ); // too much characters (password)

    }


    @Test
    void test_register_input_not_enough_characters_false() throws IOException, ParserConfigurationException, SAXException, ParseException {
        String invalid_username1 = "user"; // invalid because it has less than 5 caracters
        String invalid_password1 = "p"; // invalid because it has less than 5 caracters

        // case not enough caracter
        assertFalse( loginregister.register_inputs_valids(invalid_username1,invalid_password1,invalid_password1) ); // not enough characters (username and password)
        assertFalse( loginregister.register_inputs_valids(invalid_username1,"password","password") ); // not enough characters (username)
        assertFalse( loginregister.register_inputs_valids("username",invalid_password1,invalid_password1) ); // not enough characters (password)

    }










}
