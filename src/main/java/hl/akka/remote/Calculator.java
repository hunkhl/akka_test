package hl.akka.remote;

import akka.dispatch.Futures;
import scala.concurrent.Future;

public class Calculator implements ICalculator{

	@Override
	public Future<Integer> sum(int value, int value2) {
//		System.out.println("call sum..");
		return Futures.successful(value + value2);
	}

}
