package com.toniomaLabs.explore.reactor.basicStrategy;

import java.util.concurrent.CountDownLatch;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class App {

	public static void main(String[] args) {
		conditionalProcessing();
		emitAtFixInterval();
		
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
	
}
