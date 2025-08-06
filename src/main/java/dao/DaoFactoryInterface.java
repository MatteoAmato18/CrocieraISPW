package dao;

import entity.User;

public interface DaoFactoryInterface {
    
    GenericDao<User> getUserDao();
    
}