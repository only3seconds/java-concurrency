package ppp.chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 基于线程池的web服务器
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80); //服务端socket
        while (true) {
            final Socket connection = socket.accept(); //客户端一个连接请求
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection); //新建一个任务处理这个请求
                }
            };
            exec.execute(task); //Executor框架将任务提交到线程池中，任务的执行由线程中的线程处理
        }

    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }


}
