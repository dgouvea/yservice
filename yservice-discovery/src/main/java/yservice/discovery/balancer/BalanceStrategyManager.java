package yservice.discovery.balancer;

public final class BalanceStrategyManager {

	private static volatile BalanceStrategyManager instance;
	
	private BalancerStrategy balancerStrategy;
	
	private BalanceStrategyManager() {
		this.balancerStrategy = new RoundRobinBalancerStrategy();		
	}
	
	public static BalanceStrategyManager getInstance() {
		if (instance == null) {
			synchronized (BalanceStrategyManager.class) {
				instance = new BalanceStrategyManager();
			}
		}
		return instance;
	}
	
	public BalancerStrategy getBalancerStrategy() {
		return balancerStrategy;
	}

	public synchronized void setBalancerStrategy(BalancerStrategy balancerStrategy) {
		this.balancerStrategy = balancerStrategy;
	}
	
}
