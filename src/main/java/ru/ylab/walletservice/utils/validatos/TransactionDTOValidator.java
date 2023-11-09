package ru.ylab.walletservice.utils.validatos;

import jakarta.validation.ValidationException;
import ru.ylab.walletservice.dto.TransactionDTO;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Utility class for validate TransactionDTO
 */
public class TransactionDTOValidator {

    private static final TransactionDTOValidator TRANSACTION_DTO_VALIDATOR = new TransactionDTOValidator();

    private final Predicate<String> transactionIdIsValid = transactionId -> transactionId.length() <= 10
            && Objects.nonNull(transactionId);
    private final Predicate<String> purpose = purpose -> purpose.length() <= 15 && Objects.nonNull(purpose);
    private final Predicate<Integer> amount = amount -> amount > 0 && amount < 50000 && Objects.nonNull(amount);

    public static TransactionDTOValidator getInstance(){
        return TRANSACTION_DTO_VALIDATOR;
    }
    public boolean isValid(TransactionDTO transactionDTO) {
        if (!transactionIdIsValid.test(transactionDTO.getTransactionalId())){
            throw new ValidationException("TransactionD id invalid");
        } else if (!purpose.test(transactionDTO.getPurpose())){
            throw new ValidationException("Transaction purpose invalid");
        } else if (!amount.test(transactionDTO.getAmount())){
            throw new ValidationException("Transaction amount invalid");
        }
        return true;
    }
}
