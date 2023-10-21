package com.example.lazythreadpool;
 

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
 

@Slf4j
public class LazyThreadPool extends ThreadPoolExecutor{
	/**
	 * 仿Executors.newFixedThreadPool(corePoolSize, threadFactory);
	 * return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
	 * 创建指定大小线程池
	 * @param corePoolSize
	 */
	private LazyThreadPool(int corePoolSize) {
		super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				//t.setDaemon(true);//此线程创建后，设置为后台线程
				log.info("创建线程："+t);
				return t;
			}
		});
		log.info("懒实例执行器服务被创建：LazyThreadPool");
	}

	private static class ExecutorServiceHolder {
		private static ExecutorService executorService = new LazyThreadPool(1);//作为单线程池处理任务
	}

	public static ExecutorService getExecutorService() {
		return ExecutorServiceHolder.executorService;
	}
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		System.out.println(t);
		if(r instanceof AbstractTask){
			log.info("任务准备执行："+((AbstractTask)r).taskName);
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(r instanceof AbstractTask){
			log.info("任务执行完成："+((AbstractTask)r).taskName);
		}
	}

	@Override
	protected void terminated() {
		log.info("线程池退出");
	}
	@Override
	public void execute(Runnable command) {
		// TODO Auto-generated method stub
		super.execute(this.wrapRunnable(command,clientTrace()));
	}
	@Override
	public Future<?> submit(Runnable task) {
		// TODO Auto-generated method stub
		return super.submit(this.wrapRunnable(task,clientTrace()));
	}
	private Exception clientTrace(){
		return new Exception("任务提交所在处。");
	}
	/**
	 * 1、知道异常在哪里抛出的
	 * 2、这个任务到底是在哪里提交的
	 * @param command
	 * @return
	 */
	private Runnable wrapRunnable(final Runnable command,final Exception clientTrace){
		return new Runnable() {
			@Override
			public void run() {
				try{
					command.run();
				}catch(Exception e){
					clientTrace.printStackTrace();//追踪调用所在地
					e.printStackTrace();//防止部分任务运行时异常丢失
					throw e;
				}
			}
		};
	}
}