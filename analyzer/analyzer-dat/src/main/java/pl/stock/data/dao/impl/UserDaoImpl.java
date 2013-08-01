package pl.stock.data.dao.impl;

import org.springframework.stereotype.Repository;

import pl.stock.data.core.GenericDaoImpl;
import pl.stock.data.dao.UserDao;
import pl.stock.data.entity.User;

/**
 * Class with DAO operations for User entity
 * @author Piotr Mińkowski
 *
 */
@Repository
public class UserDaoImpl extends GenericDaoImpl<Integer, User> implements UserDao {

}
