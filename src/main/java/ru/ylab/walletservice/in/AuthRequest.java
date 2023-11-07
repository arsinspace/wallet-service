package ru.ylab.walletservice.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class for send Auth requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6986746375915710855L;
    private String username;
    private String password;
}
