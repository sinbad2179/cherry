package cn.cherry.authenticate.service;


import cn.cherry.authenticate.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.util.Base64;

/**
 * @author :  sinbad.cheng
 * @since :  2024-07-18 17:51
 */
@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AuthUserDetailsServiceTest {

    @Test
    public void keyPair() {
        String keystorePath = "cherry.jks";
        String keystorePd = "ngfQwRk7";
        String keyAlias = "bosum";
        String keyPd = "mvvYkvZj";

        ClassPathResource ksFile = new ClassPathResource(keystorePath);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(ksFile, keystorePd.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyAlias, keyPd.toCharArray());

        String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println("Public Key as String: " + publicKeyString);

        String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        System.out.println("Private Key as String: " + privateKeyString);

    }

}