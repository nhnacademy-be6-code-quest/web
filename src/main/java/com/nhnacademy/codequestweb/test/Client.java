package com.nhnacademy.codequestweb.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Client {
    long clientId;
    String clientName;
    String email;
}
