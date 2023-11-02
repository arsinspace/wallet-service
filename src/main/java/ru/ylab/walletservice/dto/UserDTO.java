package ru.ylab.walletservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UserDTO {

    private String name;
    private String lastName;
    private String age;
    private String email;
}
