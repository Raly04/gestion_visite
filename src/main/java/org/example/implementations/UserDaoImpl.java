package org.example.implementations;

import org.example.daos.UserDao;
import org.example.models.User;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class UserDaoImpl extends GenericDaoImpl<User, Integer> implements UserDao {
    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResultOptional();
        }
    }
}
