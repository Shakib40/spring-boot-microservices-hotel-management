package com.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.auth.client.dto.RoleResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user/roles/name/{name}")
    RoleResponse getRoleByName(@PathVariable("name") String name);
}
