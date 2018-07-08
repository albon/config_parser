package bj.albon.arith.config.parser.api.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
    public static final String BROWSER_COMPATIBILITY = "compatibility";

    static {
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        client.getHttpConnectionManager().getParams().setSoTimeout(60000);
        client.getParams().setConnectionManagerTimeout(3000l);
        client.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(10);
        client.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler() {

            public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
                return executionCount < 3;
            }
        });
    }

    public static String get(String url) {
        GetMethod method = new GetMethod(url);
        method.setFollowRedirects(true);
        try {
            setRequestHeadersForTrace(method);
            int code = client.executeMethod(method);
            if (code == 200) {
                long contentLength = method.getResponseContentLength();
                if (contentLength == -1)
                    return method.getResponseBodyAsString(Integer.MAX_VALUE);
                else
                    return method.getResponseBodyAsString();
            } else {
                logger.warn("get url:{} return {}", url, code);
            }
        } catch (Exception e) {
            logger.error("push fail: url=" + url, e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public enum ClientType {
        NORMAL
    }

    public static String postJson(String url, String body, ClientType clientType) {
        PostMethod method = new PostMethod(url);
        try {
            method.setRequestEntity(new StringRequestEntity(body, "application/json", "UTF-8"));
            setRequestHeadersForTrace(method);
            int code = 0;
            switch (clientType) {
                case NORMAL:
                    code = client.executeMethod(method);
                    break;
                default:
                    logger.error("error ClientType:" + clientType.name());
                    return null;
            }
            if (code == 200) {
                long contentLength = method.getResponseContentLength();
                if (contentLength == -1)
                    return method.getResponseBodyAsString(Integer.MAX_VALUE);
                else
                    return method.getResponseBodyAsString();
            }
            logger.error("post fail: url={}, body={}, returnCode={}", url, body, code);
        } catch (Exception e) {
            logger.error("push fail: url=" + url + ", body=" + body + ", ClientType=" + clientType.name(), e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static String post(String url, Map<String, String> params, String body, ClientType clientType) {
        PostMethod method = new PostMethod(url);
        try {
            method.setRequestEntity(new StringRequestEntity(body, "application/x-www-form-urlencoded", "UTF-8"));

            setRequestHeadersForTrace(method);
            if (params != null) {
                for (String name : params.keySet()) {
                    method.addParameter(name, params.get(name));
                }
            }
            int code = 0;

            switch (clientType) {
                case NORMAL:
                    code = client.executeMethod(method);
                    break;
                default:
                    logger.error("error ClientType:" + clientType.name());
                    return null;
            }

            if (code == 200) {
                long contentLength = method.getResponseContentLength();
                if (contentLength == -1)
                    return method.getResponseBodyAsString(Integer.MAX_VALUE);
                else
                    return method.getResponseBodyAsString();
            }
        } catch (Exception e) {
            logger.error("push fail: url=" + url + ", body=" + body + ", params=" + params + ", ClientType="
                    + clientType.name(), e);
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static String post(String url, String body) {
        return post(url, null, body, ClientType.NORMAL);
    }

    /**
     * 根据AppContext中的变量，设置GET或者POST的request header
     * (虽然RFC中request header的key不区分大小写,但我们采用约定好的key)
     *
     * @param httpMethod
     */
    public static void setRequestHeadersForTrace(HttpMethod httpMethod) {
        HttpMethodParams httpMethodParams = new HttpMethodParams();
        httpMethodParams.setContentCharset("UTF-8");
        httpMethod.setParams(httpMethodParams);
    }

}
