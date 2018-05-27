package ppp.chapter8;

import javax.xml.bind.Element;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 将串行执行转换为并行执行；将串行递归转换为并行递归
 */
public abstract class TransformingSequential {

    //串行执行
    void processSequentially(List<Element> elements) {
        for (Element e: elements) {
            process(e);
        }
    }

    //并行执行
    void processInParalle(Executor exec, List<Element> elements) {
        for (final Element e: elements) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    process(e);
                }
            });
        }
    }

    interface Element{

    }
    public abstract void process(Element e);

    //串行递归
    public <T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results) {
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    //并行递归
    public <T> void parallelRecursive(final Executor exec, List<Node<T>> nodes, final Collection<T> results) {
        for (final Node<T> n : nodes) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    results.add(n.compute());
                }
            });
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    //获取并行递归所有的结果
    public <T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    interface Node <T> {
        T compute();

        List<Node<T>> getChildren();
    }
}
