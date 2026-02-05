package com.auth.client;

import com.auth.dto.RoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/roles/name/{name}")
    RoleResponse getRoleByName(@PathVariable("name") String name);
}
