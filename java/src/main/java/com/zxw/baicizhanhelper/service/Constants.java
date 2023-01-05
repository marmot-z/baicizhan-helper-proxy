package com.zxw.baicizhanhelper.service;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/13
 */
public class Constants {

    public static class Urls {
        /**
         * 发送手机验证码 url 前缀
         */
        public static final String SEND_SMS_VERIFY_CODE_URL_PREFIX = "https://passport.baicizhan.com/rpc/unified_user_service/send_sms_verify_code/";

        /**
         * 手机验证码登录 url 前缀
         */
        public static final String LOGIN_URL_PREFIX = "https://passport.baicizhan.com/rpc/unified_user_service/login_with_phone/";

        /**
         * 访问 token 验证 url 前缀
         */
        public static final String CHECK_ACCESS_TOKEN_URL_PREFIX = "https://passport.baicizhan.com/rpc/unified_user_service/check_access_token/";

        /**
         * 用户信息获取 url 前缀
         */
        public static final String GET_USER_INFO_URL_PREFIX = "https://passport.baicizhan.com/rpc/unified_user_service/get_bind_info/";

        /**
         * 查询单词 url 前缀
         */
        public static final String SEARCH_WORD_URL_PREFIX = "https://resource.baicizhan.com/rpc/resource_api/search_word_v2/";

        /**
         * 获取单词详情 url 前缀
         */
        public static final String GET_WORD_DETAIL_URL_PREFIX = "https://resource.baicizhan.com/rpc/resource_api/get_topic_resource_v2/";

        /**
         * 单词匹配 url 前缀
         */
        public static final String MATCH_WORD_URL_PREFIX = "https://booklist.baicizhan.com/rpc/user_book/match_words/";

        /**
         * 添加单词到单词本 url 前缀
         */
        public static final String ADD_WORD_URL_PREFIX = "https://booklist.baicizhan.com/rpc/user_book/add_words_to_book/";

        /**
         * 获取用户单词本列表 url 前缀
         */
        public static final String GET_USER_BOOKS_URL_PREFIX = "https://booklist.baicizhan.com/rpc/user_book/get_user_books/";
    }

    public static class HeaderKeys {
        public static final String HOST = "Host";
        public static final String USER_AGENT = "User-Agent";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String COOKIE = "Cookie";
    }

    public static class CookieKeys {
        public static final String DEVICE_NAME = "device_name";
        public static final String DEVICE_VERSION = "device_version";
        public static final String DEVICE_ID = "device_id";
        public static final String APP_NAME = "app_name";
        public static final String SERIAL = "serial";
        public static final String OS_VERSION = "os_version";
        public static final String APP_VERSION = "app_version";
        public static final String CHANNEL = "channel";
        public static final String CLIENT_TIME = "client_time";
        public static final String ACCESS_TOKEN = "access_token";
    }
}
