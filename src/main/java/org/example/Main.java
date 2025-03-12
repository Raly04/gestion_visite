package org.example;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.daos.UserDao;
import org.example.frames.Login;
import org.example.implementations.UserDaoImpl;
import org.example.models.User;
import org.example.utils.HibernateUtil;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Initialize Hibernate
        HibernateUtil.getSessionFactory();

        //Create a default user
        UserDao userDao = new UserDaoImpl();
        if(userDao.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            userDao.save(user);
        }
        FlatLightLaf.setup();
        UIManager.put("Button.arc", 6);
        UIManager.put("Component.arc", 6);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", true);
        UIManager.put("Table.intercellSpacing", new Dimension(1, 1));
        UIManager.put("ScrollBar.thumb", new Color(220, 220, 220));

        SwingUtilities.invokeLater(Login::new);
    }
}