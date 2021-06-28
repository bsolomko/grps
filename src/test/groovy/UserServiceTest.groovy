import io.grpc.stub.StreamObserver
import org.example.grpc.UserOuterClass
import org.springframework.beans.factory.annotation.Autowired
import service.UserService
import spock.lang.Specification

class UserServiceTest extends Specification {


    private UserService userService  = new UserService();

    def "FindUserById"() {

        expect:
            2==2
    }

    def "Create"() {
        expect:
        2==2
    }

    def "GetAllUsers"() {
        expect:
        2==2
    }

    def "Update"() {
        expect:
        2==2
    }

    def "Delete"() {
        expect:
        2==2
    }
}
