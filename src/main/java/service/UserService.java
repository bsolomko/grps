package service;



import io.grpc.stub.StreamObserver;
import learn.entity.User;
import learn.repository.UserRepository;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.grpc.UserOuterClass;
import org.example.grpc.userGrpc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class UserService extends userGrpc.userImplBase {

    @Autowired
    UserRepository repository;

    @Override
    public void findUserById(UserOuterClass.UserIdRequest request, StreamObserver<UserOuterClass.UserResponse> responseObserver) {
        User user = repository.findById(request.getId());
        if (user != null) {
            UserOuterClass.UserResponse.Builder response = UserOuterClass.UserResponse.newBuilder();
            UserOuterClass.User.Builder userBuild = UserOuterClass.User.newBuilder();

            userBuild.setEmail(user.getEmail());
            userBuild.setId(user.getId());
            response.setUser(userBuild);
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        }
        UserOuterClass.UserResponse.Builder response = UserOuterClass.UserResponse.newBuilder();
        UserOuterClass.User.Builder userBuild = UserOuterClass.User.newBuilder();

        userBuild.setEmail(null);
        userBuild.setId(null);
        response.setUser(userBuild);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void create(UserOuterClass.UserRequest request, StreamObserver<UserOuterClass.UserResponse> responseObserver) {

        User user = new User(request.getUser().getId(), request.getUser().getUsername(), request.getUser().getEmail());
        System.out.println(user);
        repository.save(user);


        UserOuterClass.UserResponse.Builder response = UserOuterClass.UserResponse.newBuilder();
        UserOuterClass.User.Builder userSuccess = UserOuterClass.User.newBuilder();

        userSuccess.setEmail(user.getEmail());
        userSuccess.setId(user.getId());


        response.setUser(userSuccess);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(UserOuterClass.Empty request, StreamObserver<UserOuterClass.AllUsersResponse> responseObserver) {
        UserOuterClass.AllUsersResponse.Builder response = UserOuterClass.AllUsersResponse.newBuilder();

        List<User> users = repository.findAll();
        System.out.println(users.size());
        for (int i = 0; i < users.size(); i++) {
            UserOuterClass.User.Builder userBuild = UserOuterClass.User.newBuilder();

            userBuild.setId(users.get(i).getId());
            userBuild.setEmail(users.get(i).getEmail());
            response.addUser(userBuild);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void update(UserOuterClass.UserRequest request, StreamObserver<UserOuterClass.UserResponse> responseObserver) {
        User user = repository.findById(request.getUser().getId());
        user.setEmail(request.getUser().getEmail());
        repository.save(user);
        UserOuterClass.UserResponse.Builder response = UserOuterClass.UserResponse.newBuilder();
        UserOuterClass.User.Builder userBuild = UserOuterClass.User.newBuilder();
        userBuild.setId(user.getId());
        userBuild.setEmail(user.getEmail());
        response.setUser(userBuild);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void delete(UserOuterClass.UserIdRequest request, StreamObserver<UserOuterClass.UserResponse> responseObserver) {
        User user = repository.findById(request.getId());
        repository.delete(user);
        UserOuterClass.UserResponse.Builder response = UserOuterClass.UserResponse.newBuilder();
        UserOuterClass.User.Builder userBuild = UserOuterClass.User.newBuilder();
        userBuild.setId(user.getId());
        userBuild.setEmail(user.getEmail());
        response.setUser(userBuild);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
