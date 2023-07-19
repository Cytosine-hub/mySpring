package com.cytosine.spring.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * Xml与JavaBean相互转换的工具类
 */
@XmlRootElement
public class JaxbUtil {

    /**
     * JavaBean 转为XML
     * @param o    一般为Response
     * @param bodyClass  报文body对应的实体类
     * @return
     */
    public static String convertToXml(Object o,Class bodyClass){
        try{
            //创建JAXContext
            JAXBContext context = JAXBContext.newInstance(o.getClass(),bodyClass);
            //创建一个marshaller
            Marshaller marshaller = context.createMarshaller();
            //设置属性
            marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
            //字节输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //新建一个XML输出工厂实例
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            //XML输入
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(baos,"UTF-8");
            //文档开始
            xmlStreamWriter.writeStartDocument((String) marshaller.getProperty(Marshaller.JAXB_ENCODING),"1.0");
            //文档内容
            marshaller.marshal(o,xmlStreamWriter);
            //文档结束
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.close();
//            return baos.toString();
            return baos.toString("UTF-8");


        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param xml  由xml报文转的字符串
     * @param c  要转的JavaBean对象 一般为Response
     * @param bodyClass  body部分的Java对象
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToJavaBean(String xml,Class<T> c,Class bodyClass){
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c,bodyClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T)unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return t;
    }

//    public static void main(String[] args) {
//        ResponseHead responseHead = new ResponseHead();
//        responseHead.setCode("001");
//        responseHead.setData(null);
//        responseHead.setMessage("测试");
//        responseHead.setType(RequestType.BIND_CARD);
//
//        User user = new User();
//        user.setUserId("12222");
//        user.setPassword("123456");
//        user.setUsername("朱国良");
//
//        Response response = new Response();
//        response.setBody(user);
//        response.setHead(responseHead);
//
//        String xml = JaxbUtil.convertToXml(response,user.getClass());
//
//        Socket socket = null;
//        try {
//            String resXml = SocketUtil.sendXmlSocket(xml);
//            System.out.println(resXml);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(xml);
//
//        Response response1 = convertToJavaBean(xml,Response.class,User.class);
//
//        System.out.println(response1);
//    }
}
