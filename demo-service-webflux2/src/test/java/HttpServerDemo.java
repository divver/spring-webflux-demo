import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

public class HttpServerDemo {
    public static final String HOST = "localhost";
    public static final int PORT = 8082;

    public static void main(String[] args) throws Exception {
        HttpServerDemo server = new HttpServerDemo();
        server.startReactorServer();

        System.in.read();
    }

    public void startReactorServer() {
        HttpHandler httpHandler = (request, response) -> {
            if(request.getURI().getPath().equals("/orders")){
                ReactiveHttpOutputMessage outputMessage = response;
                DataBuffer dataBuffer = outputMessage.bufferFactory().allocateBuffer();
                dataBuffer.write(new String("test").getBytes());
                return outputMessage.writeWith(Mono.just(dataBuffer));
            }
            return Mono.empty();
        };

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(HOST, PORT);
        server.newHandler(adapter).block();
    }

}
