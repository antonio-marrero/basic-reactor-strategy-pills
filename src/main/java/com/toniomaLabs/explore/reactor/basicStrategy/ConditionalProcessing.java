package com.toniomaLabs.explore.reactor.basicStrategy;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Target:
 * To execute different process pipelines depending on a property of the emitted values
 * 
 * Strategy:
 * Apply the condition in a Flatmap so that you can process in different flux 
 */
public class ConditionalProcessing {
	
	public Flux<String> getFlux(){
		return  Flux.fromArray(new String[]{
				"INFO:Server started",
				"INFO:Request for login",
				"ALERT:Incorrect pwd. Attempt no.1",
				"ALERT:Incorrect pwd. Attempt no.2",
				"INFO:Successful Login"})
		.map(msg -> handlePreprocess(msg))
		.flatMap(prepMsg -> {
			if(prepMsg.startsWith("INFO"))
				return handleInfoNotification(prepMsg);
			else 
				return handleAlertNotification(prepMsg);})
		.map(notif -> handleNotification(notif));
	}

	
	private String handlePreprocess(String msg){
		return msg + " - pre-processed ";
	}
	
	private Flux<? extends String> handleInfoNotification(String msg){
		return Flux.just(msg)
				.publishOn(Schedulers.single())
				.map(val -> val.toLowerCase());
	}
	
	private Flux<String> handleAlertNotification(String msg){
		return Flux.just(msg)
				.publishOn(Schedulers.single())
				.map(val -> val.toUpperCase());
	}
	
	private String handleNotification(String notif){
		return notif + " - processed";
	}
}
