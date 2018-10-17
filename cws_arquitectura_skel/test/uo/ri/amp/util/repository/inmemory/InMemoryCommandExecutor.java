package uo.ri.amp.util.repository.inmemory;

import uo.ri.business.impl.Command;
import uo.ri.business.impl.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class InMemoryCommandExecutor implements CommandExecutor {

	@Override
	public <T> T execute(Command<T> cmd) throws BusinessException {
		return cmd.execute();
	}

}
