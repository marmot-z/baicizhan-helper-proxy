package com.zxw.baicizhanhelper.service;

import com.zxw.baicizhanhelper.entity.*;
import com.zxw.baicizhanhelper.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zxw.baicizhanhelper.service.Constants.Urls.*;
import static com.zxw.baicizhanhelper.service.Constants.HeaderKeys.*;
import static com.zxw.baicizhanhelper.service.Constants.CookieKeys.*;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/10
 */
@Slf4j
@Service
@AllArgsConstructor
public class BusinessService {

    private HttpClientConnectionManager connectionManager;

    /**
     * 发送手机验证码
     *
     * @param phoneNum 手机号码
     * @throws Exception
     */
    public void sendSmsVerifyCode(String phoneNum) throws Exception {
        final int verifyType = 5;

        // TODO 验证手机号码合理性
        // TODO 增加手机号码请求限制
        operateByThriftClient(SEND_SMS_VERIFY_CODE_URL_PREFIX, client -> {
            ((UnifiedUserService.Client) client).send_sms_verify_code(phoneNum, verifyType);

            return null;
        });
    }

    /**
     * 手机验证码登录
     *
     * @param phoneNum 手机号码
     * @param verifyCode 验证码
     * @return
     * @throws Exception
     */
    public UserLoginResult loginWithPhone(String phoneNum, String verifyCode) throws Exception {
        return operateByThriftClient(LOGIN_URL_PREFIX, client ->
            ((UnifiedUserService.Client) client).login_with_phone(
                    new PhoneLoginRequest(
                            new PhoneVerifyCodeRequest(phoneNum, verifyCode),
                            new PhoneFreeVerifyRequest(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY),
                            getDeviceId()
                    )
            )
        );
    }

    /**
     * 获取用户信息
     *
     * @return
     * @throws Exception
     */
    public List<UserBindInfo> getUserInfo() throws Exception {
        return operateByThriftClient(CHECK_ACCESS_TOKEN_URL_PREFIX, client -> {
            UnifiedUserService.Client unifiedClient = (UnifiedUserService.Client) client;

            unifiedClient.check_access_token(getDeviceId());

            return unifiedClient.get_bind_info();
        });
    }

    /**
     * 搜索单词列表
     *
     * @param word 单词
     * @return
     * @throws Exception
     */
    public List<SearchWordResultV2> searchWord(String word) throws Exception {
        return operateByThriftClient(SEARCH_WORD_URL_PREFIX, client ->
                ((ResourceService.Client) client).search_word_v2(word)
        );
    }

    /**
     * 获取单词详情信息
     *
     * @param topicId 单词对应的 topicId
     * @return
     * @throws Exception
     */
    public TopicResourceV2 getWordDetail(Integer topicId) throws Exception {
        final int wordLevelId = 0;
        final int tagId = 0;
        final boolean withZpk = false;
        final boolean withDict = true;
        final boolean withDictWiki = false;
        final boolean withMedia = false;
        final boolean withSimilalWords = false;

        return operateByThriftClient(GET_WORD_DETAIL_URL_PREFIX, client -> {
            TopicKey topicKey = new TopicKey(topicId, wordLevelId, tagId);

            return ((ResourceService.Client) client).get_topic_resource_v2(
                    topicKey,
                    GetTopicResourceChannel.LOOK_UP,
                    withZpk,
                    withDict,
                    withDictWiki,
                    withMedia,
                    withSimilalWords
            );
        });
    }

    /**
     * 获取用户单词本列表
     *
     * @return
     * @throws Exception
     */
    public UserBookInfo getBookList() throws Exception {
        return operateByThriftClient(GET_USER_BOOKS_URL_PREFIX, client ->
                ((UserBookService.Client) client).get_user_books()
        );
    }

    /**
     * 添加单词到指定单词本中
     *
     * @param userBookId
     * @param word
     * @return
     * @throws Exception
     */
    public AddWordsRsp addWord2book(long userBookId, String word) throws Exception {
        return operateByThriftClient(MATCH_WORD_URL_PREFIX, client -> {
            UserBookService.Client userBookClient = (UserBookService.Client) client;
            List<UserBookWord> userBookWords = userBookClient.match_words(word);

            if (CollectionUtils.isEmpty(userBookWords)) {
                return null;
            }

            // 取最匹配的第一个单词添加到单词本中
            List<UserBookWord> bestMatchWord = Collections.singletonList(userBookWords.get(0));

            return userBookClient.add_words_to_book(userBookId, bestMatchWord);
        });
    }

    public AddWordsRsp removeWordFromBook(long userBookId, int topicId) throws Exception {
        return operateByThriftClient(MATCH_WORD_URL_PREFIX, client -> {
            UserBookService.Client userBookClient = (UserBookService.Client) client;
            UserBookWord userBookWord = new UserBookWord();

            userBookWord.setTopic_id(topicId);
            userBookWord.setBook_id(0);

            return userBookClient.add_words_to_book(userBookId, Collections.singletonList(userBookWord));
        });
    }

    /**
     * 路径和对应 thrift client 构造器对应关系
     * <请求路径地址 : thrift client constructor>
     */
    public static final Map<String, Function<TProtocol, ? extends TServiceClient>> URL_CONSTRUCTOR_MAP;

    static {
        URL_CONSTRUCTOR_MAP = new HashMap<>();

        URL_CONSTRUCTOR_MAP.put(SEND_SMS_VERIFY_CODE_URL_PREFIX, UnifiedUserService.Client::new);
        URL_CONSTRUCTOR_MAP.put(LOGIN_URL_PREFIX, UnifiedUserService.Client::new);
        URL_CONSTRUCTOR_MAP.put(CHECK_ACCESS_TOKEN_URL_PREFIX, UnifiedUserService.Client::new);
        URL_CONSTRUCTOR_MAP.put(GET_USER_INFO_URL_PREFIX, UnifiedUserService.Client::new);
        URL_CONSTRUCTOR_MAP.put(SEARCH_WORD_URL_PREFIX, ResourceService.Client::new);
        URL_CONSTRUCTOR_MAP.put(GET_WORD_DETAIL_URL_PREFIX, ResourceService.Client::new);
        URL_CONSTRUCTOR_MAP.put(GET_USER_BOOKS_URL_PREFIX, UserBookService.Client::new);
        URL_CONSTRUCTOR_MAP.put(MATCH_WORD_URL_PREFIX, UserBookService.Client::new);
        URL_CONSTRUCTOR_MAP.put(ADD_WORD_URL_PREFIX, UserBookService.Client::new);
    }

    /**
     * 通过 thrift 客户端进行业务操作
     *
     * @param url 相关操作 url
     * @param operator 业务操作方法
     * @param <T> client 类型
     * @param <R> 业务操作返回类型
     * @return
     * @throws TException
     * @throws IOException
     */
    private <T extends TServiceClient, R> R operateByThriftClient(String url, ThriftClientOperator<T, R> operator) throws TException, IOException {
        Function<TProtocol, ? extends TServiceClient> constructor = URL_CONSTRUCTOR_MAP.get(url);

        if (Objects.isNull(constructor)) {
            throw new IllegalArgumentException("未知的 url: " + url);
        }

        // TODO 出现 IO 异常进行重试
        CloseableHttpClient httpClient = getHttpClient();
        THttpClient client = new THttpClient(url + new Date().getTime(), httpClient);
        TTransport transport = new TFramedTransport(client);
        TProtocol protocol = new TCompactProtocol(transport);

        client.setCustomHeaders(getCustomHeaders(url));

        return operator.apply((T) constructor.apply(protocol));
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    private Map<String, String> getCustomHeaders(String url) {
        Map<String, String> headers = new HashMap<>();
        final String defaultUserAgent = "Cocoa/THTTPClient bcz_app_iphone/7020300";
        final String defaultAcceptLanguage = "zh-cn";
        final String cookie = getCookies().entrySet()
                .stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue().toString()))
                .collect(Collectors.joining("; "));

        headers.put(HOST, Utils.extractHost(url));
        headers.put(USER_AGENT, defaultUserAgent);
        headers.put(ACCEPT_LANGUAGE, defaultAcceptLanguage);
        headers.put(COOKIE, cookie);

        return headers;
    }

    private Map<String, Object> getCookies() {
        Map<String, Object> cookies = new HashMap<>();
        final String defaultDeviceName = "iPhone10.3";
        final String defaultDeviceVersion = "13.4.1";
        final String defaultAppVersion = "7020300";
        final String defaultSerial = "450E525F-B05223249";
        final String defaultChannel = "appstore";
        final long defaultClientTime = new Date().getTime() / 1000;

        cookies.put(DEVICE_NAME, defaultDeviceName);
        cookies.put(DEVICE_VERSION, defaultDeviceVersion);
        cookies.put(DEVICE_ID, getDeviceId());
        cookies.put(APP_NAME, defaultAppVersion);
        cookies.put(SERIAL, defaultSerial);
        cookies.put(OS_VERSION, defaultDeviceVersion);
        cookies.put(APP_VERSION, defaultAppVersion);
        cookies.put(CHANNEL, defaultChannel);
        cookies.put(CLIENT_TIME, defaultClientTime);
        Optional.ofNullable(getAccessToken())
                .ifPresent(accessToken -> cookies.put(ACCESS_TOKEN, accessToken));

        return cookies;
    }

    /**
     * 获取当前用户的访问 token
     * 该值来源于浏览器插件请求
     *
     * @return 访问 token，未传递则返回 null
     */
    private String getAccessToken() {
        return Optional.ofNullable(
                        ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                                .getRequest()
                                .getHeader("access_token")
                )
                .orElse(null);
    }

    /**
     * 获取设备 id
     * 该值来源于浏览器插件，且每个插件的 device_id 总是固定的
     *
     * @return 设备 id，未传递则返回空字符串
     */
    private String getDeviceId() {
        return Optional.ofNullable(
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest()
                        .getHeader("device_id")
        )
        .orElse(StringUtils.EMPTY);
    }
}
