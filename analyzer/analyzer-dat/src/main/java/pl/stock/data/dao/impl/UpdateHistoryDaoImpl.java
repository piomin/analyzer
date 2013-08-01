package pl.stock.data.dao.impl;

import org.springframework.stereotype.Repository;

import pl.stock.data.core.GenericDaoImpl;
import pl.stock.data.dao.UpdateHistoryDao;

/**
 * Class with DAO operations for UpdateHistory entity
 * @author Piotr Mińkowski
 *
 */
@Repository
public class UpdateHistoryDaoImpl extends GenericDaoImpl<Integer> implements UpdateHistoryDao {

}
