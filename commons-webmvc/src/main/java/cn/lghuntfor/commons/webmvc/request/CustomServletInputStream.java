package cn.lghuntfor.commons.webmvc.request;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义Servlet输入流
 * @author liaogang
 * @date 2020/9/10
 */
public class CustomServletInputStream extends ServletInputStream {

    private final InputStream sourceStream;

    public CustomServletInputStream(InputStream inputStream) {
        sourceStream = inputStream;
    }

    public final InputStream getSourceStream() {
        return this.sourceStream;
    }

    @Override
    public int read() throws IOException {
        return this.sourceStream.read();
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.sourceStream.close();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}
