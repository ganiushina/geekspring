package com.geekbrains.geekspring.services;

import com.baeldung.grpc.*;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserRoleServiceImpl extends UserRoleServiceGrpc.UserRoleServiceImplBase {

    @Override
    public void userRole(
        UserRoleRequest request, StreamObserver<UserRoleResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);

        Collection<Role> roles = request.getRolesList();
        StringBuilder greeting = new StringBuilder();

        for (Role role : roles) {
            greeting.append(role.getName())
                    .append(" ");
        }


        UserRoleResponse response = UserRoleResponse.newBuilder()
            .setGreeting(greeting)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
