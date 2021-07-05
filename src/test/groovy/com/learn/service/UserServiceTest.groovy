package com.learn.service

import com.learn.GRPCServer
import com.learn.entity.User
import com.learn.repository.UserRepository
import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.internal.testing.StreamRecorder
import io.grpc.stub.StreamObserver
import org.example.grpc.UserOuterClass
import org.grpcmock.springboot.AutoConfigureGrpcMock
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.junit4.SpringRunner
import spock.lang.Specification


@SpringBootTest()
class UserServiceTest extends Specification {

    @Autowired
    UserRepository repository;
    @Autowired
    UserService userService;


    def "FindUserById"() {
        setup:
        UserOuterClass.UserIdRequest request = UserOuterClass.UserIdRequest.newBuilder().setId(1111).build();
        StreamRecorder<UserOuterClass.UserResponse> responseObserver = StreamRecorder.create();
        userService.findUserById(request,responseObserver);
        List<UserOuterClass.UserResponse> results = responseObserver.getValues();

        expect:
          1111 == results.get(0).getUser().getId();
    }

    def "Create"() {
        setup:

        UserOuterClass.User user = UserOuterClass.User.newBuilder()
                .setId(1111)
                .setEmail("tets@gmaail.com")
                .build();
        UserOuterClass.UserRequest request = UserOuterClass.UserRequest.newBuilder()
                .setUser(user)
                .build();

        StreamRecorder<UserOuterClass.UserResponse> responseObserver = StreamRecorder.create();
        userService.create(request,responseObserver);
        List<UserOuterClass.UserResponse> results = responseObserver.getValues();

        expect:
        "tets@gmaail.com".equals(results.get(0).getUser().getEmail());

    }

    def "GetAllUsers"() {
        setup:
        UserOuterClass.Empty request =  UserOuterClass.Empty.newBuilder().build();
        StreamRecorder<UserOuterClass.AllUsersResponse> responseObserver = StreamRecorder.create();
        userService.getAllUsers(request,responseObserver);
        expect:
        repository.findAll().size() == responseObserver.getValues().get(0).getUserCount();
    }

    def "Update"() {
        setup:
        UserOuterClass.User user = UserOuterClass.User.newBuilder()
                .setId(144)
                .setEmail("bob@gmail.com")
                .build();
        UserOuterClass.UserRequest request = UserOuterClass.UserRequest.newBuilder()
                .setUser(user)
                .build();
        StreamRecorder<UserOuterClass.UserResponse> responseObserver = StreamRecorder.create();
        userService.update(request,responseObserver);
        User res =  repository.findAll().stream().filter(u-> u.getId()  == 144).findAny().get();
        expect:
            res.getEmail().equals("bob@gmail.com");



    }

    def "Delete"() {

        setup:
        UserOuterClass.User user = UserOuterClass.User.newBuilder()
                .setId(404)
                .setEmail("404@gmail.com")
                .build();
        UserOuterClass.UserRequest request1 = UserOuterClass.UserRequest.newBuilder()
                .setUser(user)
                .build();

        StreamRecorder<UserOuterClass.UserResponse> responseObserver1 = StreamRecorder.create();
        userService.create(request1,responseObserver1);

        UserOuterClass.UserIdRequest request = UserOuterClass.UserIdRequest.newBuilder().setId(404).build();
        StreamRecorder<UserOuterClass.UserResponse> responseObserver = StreamRecorder.create();

        userService.delete(request,responseObserver);
        expect:
        request.getId() == responseObserver.getValues().get(0).getUser().getId();


    }
}
