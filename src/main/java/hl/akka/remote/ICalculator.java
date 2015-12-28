package hl.akka.remote;

import scala.concurrent.Future;

public interface ICalculator {
	public Future<Integer> sum(int value, int value2);
}
