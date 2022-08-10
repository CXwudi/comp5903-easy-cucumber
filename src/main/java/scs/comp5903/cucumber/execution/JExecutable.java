package scs.comp5903.cucumber.execution;

import java.lang.reflect.InvocationTargetException;

/**
 * @author CX无敌
 * @date 2022-08-09
 */
public interface JExecutable {

  void execute() throws InvocationTargetException, IllegalAccessException;
}
