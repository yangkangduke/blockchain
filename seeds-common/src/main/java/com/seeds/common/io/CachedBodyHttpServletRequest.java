//package com.seeds.common.io;
//
//import org.springframework.util.StreamUtils;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.io.*;
//
///**
// * @author rickierao
// * @email antilaw@yahoo.com
// * @date 2021/1/24
// */
//public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
//    private byte[] cachedBody;
//
//    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
//        super(request);
//        InputStream requestInputStream = request.getInputStream();
//        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
//    }
//
//    @Override
//    public ServletInputStream getInputStream() {
//        return new CachedBodyServletInputStream(this.cachedBody);
//    }
//
//    @Override
//    public BufferedReader getReader() {
//        // Create a reader from cachedContent
//        // and return it
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
//        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
//    }
//}
