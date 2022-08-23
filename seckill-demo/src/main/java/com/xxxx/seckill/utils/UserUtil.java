package com.xxxx.seckill.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//生成用户工具类
public class UserUtil {

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setLoginCount(0);
            user.setRegisterDate(new Date());
            user.setId(18300000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1dsf23.sf");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.println("create user");

        //插入数据库
        Connection connection = getConn();
        String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id) values(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            pstm.setInt(1, user.getLoginCount());
            pstm.setString(2, user.getNickname());
            pstm.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            pstm.setString(4, user.getSalt());
            pstm.setString(5, user.getPassword());
            pstm.setLong(6, user.getId());
            pstm.addBatch();
        }
        pstm.executeBatch();
        pstm.clearParameters();
        connection.close();
        System.out.println("insert to db");

        //登录，生成UserTicket
        String urlString = "http://localhost:8080/doLogin";
        File file = new File("/Users/mrzhang/Desktop/config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) >= 0) {
                bout.write(bytes, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper objectMapper = new ObjectMapper();

            RespBean respBean = objectMapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObj());
            System.out.println(respBean);
            System.out.println("create userTicket :" + user.getId() + ":" + userTicket);
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file :" + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "mysql123";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }

}
