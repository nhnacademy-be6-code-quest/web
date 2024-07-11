package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.payment.ClientUpdateGradeRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "clientClient", url = "localhost:8001")
public interface PaymentClientClient {

    @PutMapping("/api/client/grade")
    ResponseEntity<String> updateClientGrade(@RequestBody ClientUpdateGradeRequestDto clientUpdateGradeRequestDto);
}
