package ppp.chapter6;

import java.util.List;
import java.util.concurrent.*;

/**
 * 如果向Executor提交了一组计算任务
 * 使用 CompletionService实现页面渲染器，使页面元素在下载完成后立即显示出来
 * CompletionService将Executor和BlockingQueue的功能融合在一起,ExecutorCompletionService实现了CompletionService接口
 */
public abstract class Renderer {
    private ExecutorService executor;

    public Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source ) {
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor); //将计算部分委托给executor

        for (final ImageInfo imageInfo : info) {
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() throws Exception {
                    return imageInfo.downloadImage();
                }
            });
        }

        renderText(source); //渲染文本

        try {
            for (int t = 0; t < info.size(); t ++) {
                Future<ImageData> f = completionService.take(); //类似于队列的操作take获得已完成的结果，这些结果会在完成时被封装成Future
                ImageData imageData = f.get(); //得到已经加载好的图片内容
                renderImage(imageData); //显示图片
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            //throw launderThrowable(e.getCause());
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }
    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);

}
