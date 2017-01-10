package com.toniomaLabs.explore.reactor.basicStrategy;

import reactor.core.publisher.Flux;

/**
 * Target: 		
 * To make a flux to emit its items at a fix interval rate.
 * 
 * Strategy:
 * Zip the emitting items together with an interval flux
 */
public class EmitAtFixRate {
	
	public Flux<String> getFlux(){
		return Flux.fromArray(new String[] { "John Ford", "Stanley Kubrick", 
				"Orson Welles", "Pedro Almodovar",
				"Akira Kurosawa", "Federico Fellini", "Jean Luc Godard" })
			.zipWith(Flux.intervalMillis(500),(a, b) -> a);
	}
}
