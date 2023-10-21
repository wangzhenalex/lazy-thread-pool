package com.example.lazythreadpool;

/**
 * @projectName: lazy-thread-pool
 * @package: com.example.lazythreadpool
 * @className: TestTask
 * @author: zhen.wang
 * @description:
 * @date: 2023/10/20 22:22
 * @version: 1.0
 */
public class TestTask extends AbstractTask{
    public TestTask() {
        this.taskName = "测试线程池框架";
    }
    @Override
    public void run() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("执行方法主体");
        System.out.println(2/0);
    }
}