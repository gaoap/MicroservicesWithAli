package com.gaoap.demo.micro.flux;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join框架测试:
 * <p>
 * Fork/Join框架是Java 7提供的一个用于并行执行任务的框架，是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。
 * Fork/Join框架要完成两件事情：
 * <p>
 * 1.任务分割：首先Fork/Join框架需要把大的任务分割成足够小的子任务，如果子任务比较大的话还要对子任务进行继续分割
 * 2.执行任务并合并结果：分割的子任务分别放到双端队列里，然后几个启动线程分别从双端队列里获取任务执行。
 * 子任务执行完的结果都放在另外一个队列里，启动一个线程从队列里取数据，然后合并这些数据。
 * <p>
 * 在Java的Fork/Join框架中，使用两个类完成上述操作
 * <p>
 * 1.ForkJoinTask:我们要使用Fork/Join框架，首先需要创建一个ForkJoin任务。
 * 该类提供了在任务中执行fork和join的机制。通常情况下我们不需要直接集成ForkJoinTask类，
 * 只需要继承它的子类，Fork/Join框架提供了两个子类：
 * a.RecursiveAction：用于没有返回结果的任务
 * b.RecursiveTask:用于有返回结果的任务
 * ForkJoinTask与一般任务的主要区别在于它需要实现compute方法，在这个方法里，首先需要判断任务是否足够小，
 * 如果足够小就直接执行任务。如果不足够小，就必须分割成两个子任务，每个子任务在调用fork方法时，
 * 又会进入compute方法，看看当前子任务是否需要继续分割成子任务，如果不需要继续分割，
 * 则执行当前子任务并返回结果。使用join方法会等待子任务执行完并得到其结果。
 * <p>
 * 2.ForkJoinPool:ForkJoinTask需要通过ForkJoinPool来执行
 * 任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。当一个工作线程的队列里暂时没有任务时，
 * 它会随机从其他工作线程的队列的尾部获取一个任务(工作窃取算法)。
 *
 * @author gaoyd
 * @version 1.0.0
 * @ClassName ForkJoinTest.java
 * @Description Fork/Join框架测试
 * @createTime 2021年10月12日 11:54:00
 */
public class ForkJoinTest {
    private static final Integer num = 10;

    static class MyRecursiveTask extends RecursiveTask<Integer> {

        private Integer startValue;
        private Integer endValue;

        @Override
        protected Integer compute() {

            if (endValue - startValue <= num) {//满足条件，开始执行
                System.out.println("start compute: startValue:" + startValue + ";highValue:" + endValue);
                Integer sum = 0;
                for (int i = startValue; i <= endValue; i++) {
                    sum += i;
                }
                return sum;

            } else {//不满足执行子任务的条件，继续拆分成两个子任务
                //拆分的第一个子任务，继续递归判断
                MyRecursiveTask left = new MyRecursiveTask(startValue, (startValue + endValue) / 2);
                left.fork();
                //拆分的第二个子任务，继续递归判断
                MyRecursiveTask right = new MyRecursiveTask((startValue + endValue) / 2 + 1, endValue);
                right.fork();
                return left.join() + right.join();
            }
        }

        public MyRecursiveTask(Integer startValue, Integer endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }
    }

    @Test
    public  void testForkJoin() {
        ForkJoinPool pool = new ForkJoinPool();
        //线程池提交任务
        ForkJoinTask task = pool.submit(new MyRecursiveTask(1, 25));
        try {
            //输出各个子任务求和的总值
            System.out.println("totalSum:" + task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
