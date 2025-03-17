package evliess.io.config;

public class Constants {
    public static final String X_TOKEN = "X-token";
    public static final String X_OPENID = "X-Openid";
    public static final String APP_SECRET = "APP_SECRET";
    public static final String APP_ID = "APP_ID";
    public static final String CODE = "code";
    public static final String CODE_401 = "401";
    public static final String PUBLIC_PATH_PREFIX = "/public/";
    public static final String UID_ENDPOINT = "https://api.weixin.qq.com/sns/jscode2session?appid=${appid}&secret=${secret}&js_code=${code}&grant_type=authorization_code";
    public static final String DOUBLE_COLON = "::";
    public static final String TYPE_DICT = "dict";
    public static final String TYPE_LLM = "llm";
}
