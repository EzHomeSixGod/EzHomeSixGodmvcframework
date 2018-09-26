package com.ezhomesixgod.entity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @author renyang
 * @description
 * @createTime 2018-09-26 14:15
 */
public class EzWrapperResponse extends HttpServletResponseWrapper {

    private EzPrintWriter ezPrintWriter;

    private ByteArrayOutputStream outputStream;

    public EzWrapperResponse(HttpServletResponse response) {
        super(response);
        outputStream =new ByteArrayOutputStream();
        ezPrintWriter =new EzPrintWriter(outputStream);
    }

    public void finalize() throws Throwable {
        super.finalize();
        outputStream.close();
        ezPrintWriter.close();
    }

    public String getContent(){

        try {
            ezPrintWriter.flush();
            String s = ezPrintWriter.getMyOutput().toString("UTF-8");

            return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "UnsupportedEncoding";
        }
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return ezPrintWriter;
    }

    private static class  EzPrintWriter extends PrintWriter{

        ByteArrayOutputStream myOutput;

        public EzPrintWriter(ByteArrayOutputStream out) {
            super(out);
            myOutput =out;
        }

        public ByteArrayOutputStream getMyOutput() {
            return myOutput;
        }
    }

    public static String getJspOutput(String jsppath, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        EzWrapperResponse wrapperResponse =new EzWrapperResponse(response);
        request.getRequestDispatcher(jsppath).include(request,wrapperResponse);

        return wrapperResponse.getContent();
    }

}
