package com.nhnacademy.codequestweb.service.client;

import com.nhnacademy.codequestweb.request.client.ClientChangePasswordRequestDto;
import com.nhnacademy.codequestweb.request.client.ClientRecoveryRequestDto;

public interface ClientService {
    /**
     *
     * @param clientChangePasswordRequestDto
     */
    void changePassword(ClientChangePasswordRequestDto clientChangePasswordRequestDto);

    /**
     *
     * @param email
     * @return
     */
    String sendChangePassword(String email);

    /**
     *
     * @param email
     * @return
     */
    String sendRecoveryAccount(String email);

    /**
     *
     * @param clientRecoveryRequestDto
     * @return
     */
    String recoverAccount(ClientRecoveryRequestDto clientRecoveryRequestDto);
}
