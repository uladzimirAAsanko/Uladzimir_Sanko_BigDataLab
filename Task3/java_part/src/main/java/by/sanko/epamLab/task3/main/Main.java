package by.sanko.epamLab.task3.main;

import by.sanko.epamLab.task3.exception.ServiceException;
import by.sanko.epamLab.task3.service.executor.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor();
        try {
            executor.parseAndExecuteCommands(args);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
        }
    }
}
