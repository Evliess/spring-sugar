package evliess.io.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("paymentAsyncExecutor")
    public ThreadPoolTaskExecutor paymentAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数（平时常驻的线程数）
        executor.setCorePoolSize(5);

        // 最大线程数（高峰期能扩容到的上限）
        executor.setMaxPoolSize(10);

        // 队列容量（核心线程满了后，新任务先排队）
        executor.setQueueCapacity(200);

        // 线程名前缀（方便排查问题）
        executor.setThreadNamePrefix("payment-async-");

        // 线程空闲时间（超过核心线程数的线程，空闲多久后销毁）
        executor.setKeepAliveSeconds(60);

        // 拒绝策略：由调用线程执行（保证任务不丢失）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务完成后再关闭线程池（优雅停机）
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        return executor;
    }
}
