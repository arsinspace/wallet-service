package ru.ylab.in;

import lombok.Getter;
import ru.ylab.tools.KeyboardReader;
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
