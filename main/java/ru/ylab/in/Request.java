package ru.ylab.in;

import lombok.Getter;
import ru.ylab.utils.KeyboardReader;
/**
 * Class describes the user request
 */
@Getter
public class Request {

    private String request;

    public String getRequest(){
        return this.request = KeyboardReader.readLine();
    }
}
