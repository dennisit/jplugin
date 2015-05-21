//--------------------------------------------------------------------------
// Copyright (c) 2010-2020, En.dennisit or Cn.苏若年
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the dennisit nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
// Author: dennisit@163.com | dobby | 苏若年
//--------------------------------------------------------------------------
package com.plugin.mail;

import com.plugin.mail.user.SendUser;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * description:
 *
 * @author dennisit@163.com
 * @version 1.0
 */
public class EmailSend {

    /**
     * send email
     * @param sendUser sendUser
     * @param to  receiveUser
     * @param subject email subject
     * @param content email content
     * @param list attachment
     */
    public void send(SendUser sendUser, List<String> to, String subject, String content,List<File> list) {
        try {

            LinkedList attachList = new LinkedList(); // 附件的list,它的element都是byte[],即图片的二进制流
            for(File f:list){
                byte[] datas = new byte[(int)f.length()];
                new DataInputStream(new FileInputStream(f)).read(datas);
                attachList.add(datas);
            }
            Properties props = new Properties();
            props.put("mail.smtp.host", EmailAuthority.SMTP_HOST);
            props.put("mail.smtp.auth", EmailAuthority.SMPT_AUTH);
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage message;
            InternetAddress address[] = new InternetAddress[to.size()];
            int i1 = 0;
            int k = 0;
            while (i1 < to.size()) {
                if (to.get(i1) == null || to.get(i1).equals("")) {
                    i1++;
                    continue;
                } else {
                    address[k] = new InternetAddress(to.get(i1).toString());
                    k++;
                    i1++;
                }
            }
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendUser.emailAddr));
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new java.util.Date());
            // 新建一个MimeMultipart对象用来存放BodyPart对象(事实上可以存放多个)
            MimeMultipart mm = new MimeMultipart();
            // 新建一个存放信件内容的BodyPart对象
            BodyPart mdp = new MimeBodyPart();
            // 给BodyPart对象设置内容和格式/编码方式
            mdp.setContent(content.toString(), "text/html;charset=GBK");
            // 这句很重要，千万不要忘了
            mm.setSubType("related");
            mm.addBodyPart(mdp);

            // add the attachments
            for( int i=0; i<attachList.size(); i++)
            {
                // 新建一个存放附件的BodyPart
                mdp = new MimeBodyPart();
                DataHandler dh = new DataHandler(new ByteArrayDataSource((byte[])attachList.get(i),"application/octet-stream"));
                mdp.setDataHandler(dh);
                // 加上这句将作为附件发送,否则将作为信件的文本内容
                mdp.setFileName(new Integer(i).toString() + ".jpg");
                mdp.setHeader("Content-ID", "IMG" + new Integer(i).toString());
                // 将含有附件的BodyPart加入到MimeMultipart对象中
                mm.addBodyPart(mdp);
            }
            // 把mm作为消息对象的内容
            message.setContent(mm);

            message.saveChanges();
            javax.mail.Transport transport = session.getTransport("smtp");
            transport.connect(EmailAuthority.SMTP_HOST, sendUser.getUsername(), sendUser.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class EmailAuthority{
        public static String SMTP_HOST = "mail.qq.com";
        public static String SMPT_AUTH = "true";
    }


    class ByteArrayDataSource implements DataSource {

        /** * Data to write. */
        private byte[] data;
        /** * Content-Type. */
        private String type;

        // Create a datasource from an input stream
        public ByteArrayDataSource(InputStream is, String type) {
            this.type = type;
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                int ch;
                while ((ch = is.read()) != -1){
                    os.write(ch);
                }
                data = os.toByteArray();
            } catch (IOException ioe) {
            }
        }

        // Create a datasource from a byte array
        public ByteArrayDataSource(byte[] data, String type) {
            this.data = data;
            this.type = type;
        }

        // Create a datasource from a String
        public ByteArrayDataSource(String data, String type) {
            try {
                this.data = data.getBytes("iso-8859-1");
            } catch (UnsupportedEncodingException uee) {
            }
            this.type = type;
        }

        public InputStream getInputStream() throws IOException {
            if (null == this.data){
                throw new IOException("no data");
            }
            return new ByteArrayInputStream(this.data);
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("cannot do this");
        }

        public String getContentType() {
            return this.type;
        }

        @Override
        public String getName() {
            return "dummy";
        }

    }
}
