package com.nhnacademy.codequestweb.service.client;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.client.message.MessageClient;
import com.nhnacademy.codequestweb.request.client.ClientChangePasswordRequestDto;
import com.nhnacademy.codequestweb.request.client.ClientRecoveryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {
    private final UserClient client;
    private final MessageClient messageClient;

    @Override
    public void changePassword(ClientChangePasswordRequestDto clientChangePasswordRequestDto) {
        client.changePasswordClient(clientChangePasswordRequestDto);
    }

    @Override
    public String sendChangePassword(String email) {
        return messageClient.sendChangePassword(email).getBody();
    }

    @Override
    public String sendRecoveryAccount(String email) {
        return messageClient.sendRecoverAccount(email).getBody();
    }

    public String recoverAccount(ClientRecoveryRequestDto clientRecoveryRequestDto) {
        return client.recoveryClient(clientRecoveryRequestDto).getBody();
    }
}
