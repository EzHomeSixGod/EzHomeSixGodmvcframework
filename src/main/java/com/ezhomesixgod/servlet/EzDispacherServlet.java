package com.ezhomesixgod.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ezhomesixgod.EzInterface.*;
import com.ezhomesixgod.entity.EzWrapperResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resources;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 分发器
 * @createTime 2018-09-25 10:46
 */
public class EzDispacherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(EzDispacherServlet.class);

    private static final Long serialVersionUID=1l;

    private static final String contextConfigLocation="contextConfigLocation";

    private static final String prefix ="/WEB-INF";

    private List<String> classNames= new ArrayList<>();

    private Map<String,Object> iocMap = new ConcurrentHashMap<>();

    private Map<String,Method> handlerMappingMap = new ConcurrentHashMap<>();

    private Properties prop =new Properties();;

    public EzDispacherServlet(){
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化配置文件
        doConfigLocationContext(config.getInitParameter(contextConfigLocation));

        //扫描所有相关类
        doScanner(prop.getProperty("scanPackage"));
        //初始化相关容器类并放入IOC 容器
        doInstance();
        //依赖注入
        try {
            doAutoWired();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //构造HandlerMapping
        initHandlerMapping();
        //等待请求，匹配url，定位方法，反射调用执行
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doPost(req,resp);
    }




    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispacher(req, resp);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    protected Object doDispacher(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException, ServletException {
            if(this.handlerMappingMap.isEmpty()){return null;}


            String requestUrl = req.getRequestURI();
            logger.info("requestUrl:"+requestUrl);
            if(!handlerMappingMap.containsKey(requestUrl)){
                return null;
            }

            if(handlerMappingMap.containsKey(requestUrl)){
                Map<String,String[]> params =req.getParameterMap();

                String[] param = requestUrl.split("/");
                String controllerName = param[1];
                String methodname = param[2];
                Method method =this.handlerMappingMap.get(requestUrl);
                String beanName = lowerFirstCase(controllerName);
                Object o = iocMap.get(beanName);
                method.setAccessible(true);
                Object object = method.invoke(o,req,resp);

                logger.info("return data:"+object);
                if(method.isAnnotationPresent(EzResponseBody.class)){
                    logger.info("json:["+object+"]");
                    resp.getWriter().write(JSON.toJSONString(object));
                }

            }else{

//                String path = requestUrl+"/WEB-INF/success.jsp";
//                req.getRequestDispatcher(path).forward(req,resp);

//                logger.info("跳转到jsp:"+req.getContextPath()+"/index.jsp" );
//                if("".equals(requestUrl) || "/ezhomesixgodmvcframework/".equals(requestUrl)){
//                    logger.info("跳转到jsp:"+req.getContextPath()+"/index.jsp" );
//                    req.getRequestDispatcher(req.getContextPath()+"/index.jsp").forward(req,resp);
//                }
//                req.getRequestDispatcher(req.getContextPath()+requestUrl+".jsp").forward(req,resp);
            }
            return null;

    }

    protected void doConfigLocationContext(String contextConfigLocation){
        logger.info("init contextConfigLocation:"+contextConfigLocation);
        InputStream inputStream =null;
        try {
            if(contextConfigLocation.indexOf(":")>0){
                contextConfigLocation = contextConfigLocation.split(":")[1];
            }
            URL UR = Thread.currentThread().getContextClassLoader().getResource("");
            logger.info("url path:"+UR.getPath());
            String path =UR.getPath()+"/"+contextConfigLocation;
            prop.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream !=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected void doScanner(String packageName){
        //com.ezhomesixgod
        if(packageName ==null){ return; }
        logger.info("scanner package:"+packageName);
        URL url =Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".","/"));
        logger.info("url file:"+url.getFile());
        File file =new File(url.getFile());
        for(File fileItem:file.listFiles()){
            //文件夹
            if(fileItem.isDirectory()){
                doScanner(packageName+"."+fileItem.getName());
            }else{
                classNames.add(packageName +"."+ fileItem.getName().replace(".class","").trim());
            }

        }
    }


    //实例类名默认为首字母小写
    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0] = (char) (chars[0]+32);
        return String.valueOf(chars);
    }
    protected void doInstance(){
        logger.info("doInstance");
        if(classNames.size()==0){return;}

        for(String className : classNames){
            logger.info("[Path:"+className+"]");
            try {
                Class<?> clazz = Class.forName(className);

                if(clazz.isAnnotationPresent(EzController.class)){
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    iocMap.put(beanName,clazz.newInstance());
                }else if(clazz.isAnnotationPresent(EzService.class)){
                    EzService ezService = clazz.getAnnotation(EzService.class);
                    String serviceName = ezService.value();
                    if(serviceName !=null && serviceName !=""){
                        iocMap.put(serviceName,clazz.newInstance());
                        continue;
                    }

                    Class<?>[] classes = clazz.getInterfaces();
                    for(Class<?> c : classes){
                        iocMap.put(c.getSimpleName(),clazz.newInstance());
                    }

                }else{
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }


        }

    }


    protected void doAutoWired() throws ClassNotFoundException {
        logger.info("doAutoWired");
        //自动装配
        if(iocMap.isEmpty()){return;}

        for(Map.Entry<String,Object> entry : iocMap.entrySet()){
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(EzAutoWired.class)){
                    EzAutoWired ezAutoWired =field.getAnnotation(EzAutoWired.class);
                    String value =ezAutoWired.value().trim();
                    if("".equals(value)){
                        value = field.getType().getName();
                    }
                    if(value.lastIndexOf(".")>0){
                        value =value.substring(value.lastIndexOf(".")+1,value.length());
                    }
                    value =lowerFirstCase(value);
                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(),iocMap.get(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        continue;
                    }
                }else{
                    continue;
                }
            }
        }

    }


    protected void initHandlerMapping(){
        logger.info("----init handlerMapping---");
        if(iocMap.isEmpty()){return;}

        for(Map.Entry<String,Object> entry : iocMap.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            if(!clazz.isAnnotationPresent(EzController.class)){continue;}

            String baseUrl ="/";
            EzRequestMapping requestMapping =clazz.getAnnotation(EzRequestMapping.class);
            String mapping = requestMapping.value();
            baseUrl +=mapping;
            Method[] methods =clazz.getMethods();
            for(Method m : methods){
                if(!m.isAnnotationPresent(EzRequestMapping.class)){continue;}
                EzRequestMapping ezRequestMapping =m.getAnnotation(EzRequestMapping.class);
                baseUrl +="/"+ ezRequestMapping.value();
                handlerMappingMap.put(baseUrl,m);
                logger.info("Mapped Mapping :"+baseUrl);
                if(m.isAnnotationPresent(EzResponseBody.class)){
                    logger.info("Return Type:EzResponseBody");
                }else{
                    logger.info("Return Type:View");
                }

            }


        }

    }


}
