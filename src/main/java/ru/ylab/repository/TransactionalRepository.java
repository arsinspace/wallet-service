package ru.ylab.repository;

import ru.ylab.model.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Basic interface that provides operations for operating a transaction entity in application memory
 */
public interface TransactionalRepository {
    /**
     * Returns boolean - is any match transaction contains in transaction memory
     * @param id transaction ID
     * @return contain or not contains transaction in application memory
     */
    boolean anyMatchTransactionalById(String id);

    /**
     * Method find all transactional by userId in application memory
     * @param id UUID User ID
     * @return List of transactional
     */
    List<Transactional> findAllByUserId(UUID id);

    /**
     * Save transactional in application memory
     * @param transactional transactional entity
     * @return transactional entity
     */
    Transactional saveTransactional(Transactional transactional);

    /**
     * Update transactional in application memory
     * @param transactional transactional entity
     * @return transactional entity
     */
    Transactional updateTransactional(Transactional transactional);

    /**
     * Delete transactional in application memory
     * @param transactional transactional entity
     * @return transactional entity
     */
    Transactional deleteTransactional(Transactional transactional);
}
