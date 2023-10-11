package ru.ylab.applicationMemory;

import ru.ylab.model.Transactional;
import ru.ylab.repository.TransactionalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @implNote Implementation with logic for working with transactions in application memory
 */
public class TransactionMemory implements TransactionalRepository {

    private final Map<UUID,Transactional> memory = new HashMap<>();

    @Override
    public boolean anyMatchTransactionalById(String id){
       return memory.values()
                .stream()
                .anyMatch(el -> el.getTransactionalId().equals(id));
    }

    @Override
    public List<Transactional> findAllByUserId(UUID id) {
        return memory.values()
                .stream()
                .filter(el -> el.getUserId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public Transactional saveTransactional(Transactional transactional) {
        return memory.put(transactional.getUserId(),transactional);
    }

    @Override
    public Transactional updateTransactional(Transactional transactional) {
        return memory.put(transactional.getUserId(),transactional);
    }

    @Override
    public Transactional deleteTransactional(Transactional transactional) {
        return memory.remove(transactional.getUserId());
    }
}
