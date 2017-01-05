package com.toniomaLabs.explore.reactor.basicStrategy;

import java.util.concurrent.CountDownLatch;

import reactor.core.publisher.Flux;

public class App {

	public static void main(String[] args) {
		emitAtFixInterval();
	}
	
	/**
	 * Target: 		
	 * To make a flux to emit its items at a fix interval rate.
	 * 
	 * Strategy:
	 * Zip the emitting items together with an interval flux
	 */
	public static void emitAtFixInterval(){
		try {
			CountDownLatch latch = new CountDownLatch(1);
	
			Flux<String> fastPublisher =  Flux.fromArray(new String[] { "John Ford", "Stanley Kubrick", "Orson Welles", "Pedro Almodovar",
					"Akira Kurosawa", "Federico Fellini", "Jean Luc Godard" });
			
			Flux.zip(fastPublisher,Flux.intervalMillis(500),(a, b) -> a)
					.doAfterTerminate(latch::countDown).subscribe(System.out::println);
		
			latch.await();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
