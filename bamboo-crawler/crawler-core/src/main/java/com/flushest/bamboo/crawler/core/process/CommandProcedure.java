package com.flushest.bamboo.crawler.core.process;

/**
 * Created by Administrator on 2018/2/21 0021.
 */
public class CommandProcedure implements Procedure {
    private Command command;

    public CommandProcedure(Command command) {
        this.command = command;
    }


    @Override
    public void afterProperties() {

    }

    @Override
    public boolean process(Object item) {
        command.execute();
        return true;
    }
}
