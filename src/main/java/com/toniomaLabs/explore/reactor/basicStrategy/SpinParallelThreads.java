package com.toniomaLabs.explore.reactor.basicStrategy;

import java.util.Random;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

/**
 * 
 * Target:
 * Execute several processes in parallel
 * 
 * Strategy:
 * 		- strategy 1: Use flatMap to create a Mono for each item 
 * 					and subscribe these new Mono's to a pool of threads.
 * 		- strategy 2: Use parallel and run each emitted item with a pool of threads  
 *
 */
public class SpinParallelThreads {

	public Flux<Integer> getFlatMapThreads(){
		return Flux.range(0, 20)
				.flatMap(count -> Mono.just(count)
				.subscribeOn(Schedulers.parallel())
				.map(i -> slowIOprocess(i)));
	}
	
	
	public ParallelFlux<Integer> getParallelThreads(){
		return Flux.range(0, 20)
				.parallel()
				.runOn(Schedulers.parallel())
				.map(i -> slowIOprocess(i));
	}
	
	
	public int slowIOprocess(int i) {
		try {
			System.out.println("sleeping thread for value "+ i +"-" + Thread.currentThread().getName());
			Thread.sleep(new Random().nextInt(3000) + 500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return i;
	}
}
