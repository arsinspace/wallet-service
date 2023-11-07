package ru.ylab.walletservice.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.model.Transaction;

/**
 * Utility class for mapping TransactionDTO entity
 */
@Mapper
public interface TransactionDTOMapper {
    TransactionDTOMapper INSTANCE = Mappers.getMapper(TransactionDTOMapper.class);
    TransactionDTO toDto(Transaction transaction);
    Transaction toTransaction(TransactionDTO dto);
}
