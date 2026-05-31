package com.ruoyi.framework.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 服务相关配置
 * 
 * @author ruoyi
 */
@Component
public class ServerConfig
{
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * 
     * @return 服务地址
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        String contextPath = request.getServletContext().getContextPath();
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (StringUtils.isEmpty(scheme))
        {
            scheme = request.getScheme();
        }
        String host = request.getHeader("Host");
        if (StringUtils.isEmpty(host))
        {
            host = request.getServerName();
            int serverPort = request.getServerPort();
            if (("http".equalsIgnoreCase(scheme) && serverPort != 80) || ("https".equalsIgnoreCase(scheme) && serverPort != 443))
            {
                host = host + ":" + serverPort;
            }
        }
        return scheme + "://" + host + contextPath;
    }
}
