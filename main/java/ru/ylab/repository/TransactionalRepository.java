package ru.ylab.repository;

import ru.ylab.model.Transactional;

import java.util.List;

/**
 * Basic interface that provides operations for operating a transaction entity in Database
 */
public interface TransactionalRepository {
    /**
     * Returns boolean - is any match transaction contains in Database
     * @param id transaction ID
     * @return contain or not contains transaction in Database
     */
    boolean anyMatchTransactionalById(String id);

    /**
     * Method find all transactions by userId in Database
     * @param id UUID User ID
     * @return List of transactions
     */
    List<Transactional> findAllByUserId(long id);

    /**
     * Save transactional in Database
     *
     * @param transaction transactions entity
     * @return transaction entity
     */
    long saveTransaction(Transactional transaction);

}
