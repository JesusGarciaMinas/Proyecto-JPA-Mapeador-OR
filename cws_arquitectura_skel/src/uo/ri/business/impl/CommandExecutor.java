package uo.ri.business.impl;

import uo.ri.util.exception.BusinessException;

public interface CommandExecutor {

	<T> T execute(Command<T> cmd) throws BusinessException;

}