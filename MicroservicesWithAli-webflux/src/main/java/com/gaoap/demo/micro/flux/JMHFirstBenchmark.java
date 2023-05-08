package com.gaoap.demo.micro.flux;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 使用JMH做Benchmark基准测试
 * 注解介绍
 *
 * @author gaoyd
 * @version 1.0.0
 * @BenchmarkMode Mode 表示 JMH 进行 Benchmark 时所使用的模式。通常是测量的维度不同，或是测量的方式不同。目前 JMH 共有四种模式：
 * <p>
 * Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”，单位是操作数/时间。
 * AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”，单位是时间/操作数。
 * SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
 * SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。
 * 往往同时把 warmup 次数设为0，用于测试冷启动时的性能。
 * @OutputTimeUnit 输出的时间单位。
 * @Iteration Iteration 是 JMH 进行测试的最小单位。在大部分模式下，一次 iteration 代表的是一秒，
 * JMH 会在这一秒内不断调用需要 Benchmark 的方法，
 * 然后根据模式对其采样，计算吞吐量，计算平均执行时间等。
 * @WarmUp Warmup 是指在实际进行 Benchmark 前先进行预热的行为。
 * <p>
 * 为什么需要预热？因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，
 * JVM 会尝试将其编译成为机器码从而提高执行速度。
 * 为了让 Benchmark 的结果更加接近真实情况就需要进行预热。
 * @State 类注解，JMH测试类必须使用 @State 注解，它定义了一个类实例的生命周期，可以类比 Spring Bean 的 Scope。
 * 由于 JMH 允许多线程同时执行测试，不同的选项含义如下：
 * <p>
 * Scope.Thread：默认的 State，每个测试线程分配一个实例；
 * Scope.Benchmark：所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；
 * Scope.Group：每个线程组共享一个实例；
 * @Fork 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。
 * @Meansurement 提供真正的测试阶段参数。指定迭代的次数，每次迭代的运行时间和每次迭代测试调用的数量(
 * 通常使用 @BenchmarkMode(Mode.SingleShotTime) 测试一组操作的开销——而不使用循环)
 * @Setup 方法注解，会在执行 benchmark 之前被执行，正如其名，主要用于初始化。
 * @TearDown 方法注解，与@Setup 相对的，会在所有 benchmark 执行结束以后执行，主要用于资源的回收等。
 * @Setup/@TearDown注解使用Level参数来指定何时调用fixture： 名称    描述
 * Level.Trial	默认level。全部benchmark运行(一组迭代)之前/之后
 * Level.Iteration	一次迭代之前/之后(一组调用)
 * Level.Invocation	每个方法调用之前/之后(不推荐使用，除非你清楚这样做的目的)
 * @Benchmark 方法注解，表示该方法是需要进行 benchmark 的对象。
 * @Param 成员注解，可以用来指定某项参数的多种情况。
 * 特别适合用来测试一个函数在不同的参数输入的情况下的性能。
 * @Param 注解接收一个String数组，在 @Setup 方法执行前转化为为对应的数据类型。
 * 多个 @Param 注解的成员之间是乘积关系，譬如有两个用 @Param 注解的字段，第一个有5个值，第二个字段有2个值，
 * 那么每个测试方法会跑5*2=10次。
 * <p>
 * 查看报告结果：
 * 大多数情况只需要关注最下面的结果。
 * 可以结合 Score 和 Unit 这两列，看到方法的效率。
 * <p>
 * @ClassName JMHFirstBenchmark.java
 * @Description TODO
 * @createTime 2021年10月12日 10:43:00
 */
@BenchmarkMode(Mode.AverageTime) // 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程分配一个实例
@Fork(2) // Fork进行的数目
@Warmup(iterations = 2) // 先预热4轮
@Measurement(iterations = 2) // 进行10轮测试
public class JMHFirstBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                //benchmark 所在的类的名字，注意这里是使用正则表达式对所有类进行匹配的。
                .include(".*JMHFirstBenchmark.*")
                //进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。
                // .forks(2)
                //预热的迭代次数。
                // .warmupIterations(2)
                //实际测量的迭代次数。
                // .measurementIterations(2)
                .build();

        new Runner(opt).run();
    }

    @Param({"11", "22"}) // 定义四个参数，之后会分别对这四个参数进行测试
    private int n;

    private List<Integer> arrayListOfNumbers;
    private List<Integer> linkedListOfNumbers;

    @Setup(Level.Trial) // 初始化方法，在全部Benchmark运行之前进行
    public void init() {
        arrayListOfNumbers = new ArrayList<>();
        addNumbers(arrayListOfNumbers);

        linkedListOfNumbers = new LinkedList<>();
        addNumbers(linkedListOfNumbers);
    }

    private void addNumbers(List<Integer> container) {
        IntStream.range(0, 1_000_000)
                .forEach(container::add);
    }

    @Benchmark
    public int slowSumOfSquares() {
        return linkedListOfNumbers.parallelStream()
                .map(x -> x * x)
                .reduce(0, (acc, x) -> acc + x);
    }

    @Benchmark
    public int serialSlowSumOfSquares() {
        return linkedListOfNumbers.stream()
                .map(x -> x * x)
                .reduce(0, (acc, x) -> acc + x);
    }

    @Benchmark
    public int intermediateSumOfSquares() {
        return arrayListOfNumbers.parallelStream()
                .map(x -> x * x)
                .reduce(0, (acc, x) -> acc + x);
    }

    @Benchmark
    public int serialIntermediateSumOfSquares() {
        return arrayListOfNumbers.stream()
                .map(x -> x * x)
                .reduce(0, (acc, x) -> acc + x);
    }

    @Benchmark
    public int fastSumOfSquares() {
        return arrayListOfNumbers.parallelStream()
                .mapToInt(x -> x * x)
                .sum();
    }

    @Benchmark
    public int serialFastSumOfSquares() {
        return arrayListOfNumbers.stream()
                .mapToInt(x -> x * x)
                .sum();
    }

    @TearDown(Level.Trial) // 结束方法，在全部Benchmark运行之后进行
    public void arrayRemove() {
        arrayListOfNumbers.clear();
        linkedListOfNumbers.clear();
    }
}
