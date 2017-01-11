package com.toniomaLabs.explore.reactor.basicStrategy;

import java.util.concurrent.CountDownLatch;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class App {

	public static void main(String[] args) throws InterruptedException {
		conditionalProcessing();
		emitAtFixInterval();
		parallelProcessing();
		
	}
	
	
	public static void emitAtFixInterval(){
		try {
			CountDownLatch latch = new CountDownLatch(1);
	
			EmitAtFixRate fixRateFlux = new EmitAtFixRate();
			
			fixRateFlux.getFlux()
			.doAfterTerminate(latch::countDown)
			.subscribe(System.out::println);
		
			latch.await();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	public static void conditionalProcessing(){
		ConditionalProcessing cp = new ConditionalProcessing();
		cp.getFlux().subscribe(System.out::println);
	}
	
	
	public static void parallelProcessing() throws InterruptedException{
		SpinParallelThreads spinParallelThreads = new SpinParallelThreads();
		
		CountDownLatch latchFlatMap = new CountDownLatch(1);
		spinParallelThreads.getFlatMapThreads()
			.doAfterTerminate(latchFlatMap::countDown)
			.subscribe( m -> System.out.println("Subscriber received - " + m + " on thread: " 
					+ Thread.currentThread().getName()));
		
		latchFlatMap.await();
		System.out.println("\n - FlatMap threads processed - \n");
		
		CountDownLatch latchParallel = new CountDownLatch(1);
		spinParallelThreads.getParallelThreads()
			.doAfterTerminate(latchParallel::countDown)
			.subscribe( m -> System.out.println("Subscriber received - " + m + " on thread: " 
					+ Thread.currentThread().getName()));
		
		latchParallel.await();
		System.out.println("\n - Parallel threads processed - \n");
		
	}
}
