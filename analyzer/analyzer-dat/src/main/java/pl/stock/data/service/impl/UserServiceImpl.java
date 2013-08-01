package pl.stock.data.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.core.GenericServiceImpl;
import pl.stock.data.entity.User;
import pl.stock.data.service.UserService;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<Integer, User> implements UserService {

}
