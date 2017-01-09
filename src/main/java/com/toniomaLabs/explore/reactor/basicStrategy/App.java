package com.toniomaLabs.explore.reactor.basicStrategy;

import java.util.concurrent.CountDownLatch;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class App {

	public static void main(String[] args) {
		//emitAtFixInterval();
		conditionalProcessing();
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
	
	
	/**
	 * Target:
	 * To execute different process pipelines depending on a property of the emitted values
	 * 
	 * Strategy:
	 * Apply the condition in a Flatmap so that you can process in different flux 
	 */
	public static void conditionalProcessing(){
		Flux<String> flux = Flux.fromArray(new String[]{
				"INFO:Server started",
				"INFO:Request for login",
				"ALERT:Incorrect pwd. Attempt no.1",
				"ALERT:Incorrect pwd. Attempt no.2",
				"INFO:Successful Login"});
		
		flux.map(msg -> handlePreprocess(msg))
		.flatMap(msg -> {
			if(msg.startsWith("INFO"))
				return handleInfoNotification(msg);
			else 
				return handleAlertNotification(msg);})
		.map(notif -> handleNotification(notif))
		.subscribe(System.out::println);				
		
	}
	
	private static String handlePreprocess(String msg){
		return msg + " - pre-processed ";
	}
	
	private static Flux<? extends String> handleInfoNotification(String msg){
		return Flux.just(msg)
				.publishOn(Schedulers.single())
				.map(val -> val.toLowerCase());
	}
	
	private static Flux<String> handleAlertNotification(String msg){
		return Flux.just(msg)
				.publishOn(Schedulers.single())
				.map(val -> val.toUpperCase());
	}
	
	private static String handleNotification(String notif){
		return notif + " - processed";
	}
}
