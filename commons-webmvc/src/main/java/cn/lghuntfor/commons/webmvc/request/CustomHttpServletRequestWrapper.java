package cn.lghuntfor.commons.webmvc.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * HttpServletRequest 包装类, 方便对body的多次读取
 * @author liaogang
 * @date 2020/9/10
 */
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int BUFFER_START_POSITION = 0;

    private static final int CHAR_BUFFER_LENGTH = 1024;

    /**
     * input stream 的buffer
     */
    private byte[] body;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            InputStream inputStream = request.getInputStream();
            byte[] charBuffer = new byte[CHAR_BUFFER_LENGTH];
            int bytesRead;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(charBuffer)) > 0) {
                baos.write(charBuffer, BUFFER_START_POSITION, bytesRead);
            }
            this.body = baos.toByteArray();
        } catch (IOException e) {
            logger.error("Fail to read input stream", e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new CustomServletInputStream(byteArrayInputStream);
    }


}
