package uo.ri.amp.util.repository.inmemory;

import uo.ri.business.impl.ComandExecutorFactory;
import uo.ri.business.impl.CommandExecutor;

public class InMemoryCommandExecutorFactory implements ComandExecutorFactory {

	@Override
	public CommandExecutor forExecutor() {
		return new InMemoryCommandExecutor();
	}

}
