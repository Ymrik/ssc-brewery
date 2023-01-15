package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTest {

    private final static String PASSWORD = "password";

    @Test
    public void testBcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();

        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode("123"));
    }

    @Test
    public void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();

        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("tiger"));

        String encodedPassword = ldap.encode(PASSWORD);

        assertTrue(ldap.matches(PASSWORD, encodedPassword));
    }

    @Test
    public void testBcrypt15() {
        PasswordEncoder bcrypt15 = new BCryptPasswordEncoder(15);

        System.out.println(bcrypt15.encode("tiger"));
    }

    @Test
    public void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        System.out.println( noOp.encode(PASSWORD));
    }

    @Test
    public void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();

        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));
    }

    @Test
    public void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salted = PASSWORD + "ThisIsMySALTVALUE";

        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }
}