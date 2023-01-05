package com.zxw.baicizhanhelper.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/13
 */
public class Utils {

    public static String extractHost(String url) {
        Pattern pattern = Pattern.compile("https?://([^/]*)/.*");
        Matcher matcher = pattern.matcher(url);

        return matcher.find() ? matcher.group(1) : StringUtils.EMPTY;
    }
}
