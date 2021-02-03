package com.local.security.component;

import com.local.common.utils.CryptoHelper;
import com.local.common.utils.RandomHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Create-By: yanchen 2021/1/14 17:03
 * @Description: 自定义spring security PasswordEncoder
 */
@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

    private static final String PASSWORD_SALT_SEPARATOR="-";
    @Override
    public String encode(CharSequence rawPassword) {
        String salt= RandomHelper.oneString(5,true,true);
        return CryptoHelper.makeSaltedSha1Hash(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        final String passwordNote = rawPassword.toString();
        final Pair<String, String> passwordSaltPair = processPasswordSaltPair(passwordNote);
        return CryptoHelper.checkSha1Hash(encodedPassword, passwordSaltPair.getLeft(), passwordSaltPair.getRight());
    }

    /**
     * @create-by: yanchen 2021/1/14 17:31
     * @description:
     * @param passwordNote 密码与盐值字符串；形如：原密码(xxxx)-盐值(xxx),密码盐值连接符号由 PASSWORD_SALT_SEPARATOR指定
     * @return: org.apache.commons.lang3.tuple.Pair<java.lang.String,java.lang.String>
     */
    private Pair<String,String> processPasswordSaltPair(String passwordNote){

        int lastIndexOf = passwordNote.lastIndexOf(PASSWORD_SALT_SEPARATOR);
        final String password=passwordNote.substring(0,lastIndexOf);
        final String salt=passwordNote.substring(lastIndexOf+1);
       return Pair.of(password,salt);
    }
}
