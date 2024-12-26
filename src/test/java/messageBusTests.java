import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.services.CameraService;
class messageBusTests {
    MessageBus bus = MessageBusImpl.getInstance();
    Camera camera = new Camera();
    @Test
    void SubscribeEvent() {
    }
    @Test
    void test2() {
        // Test 2
    }
    @Test
    void test3() {
        // Test 3
    }
}