
class Logger {
    constructor(level) {
        this.level = level;
        this.nextLogger = null;
    }

    setNextLogger(nextLogger) {
        this.nextLogger = nextLogger;
    }

    logMessage(level, message) {
        if (level >= this.level) {
            this.write(message);
        }
        if (this.nextLogger) {
            this.nextLogger.logMessage(level, message);
        }
    }

    write(message) {

    }
}

const INFO = 1;
const DEBUG = 2;
const ERROR = 3;

class InfoLogger extends Logger {
    write(message) {
        console.log(`INFO: ${message}`);
    }
}

class DebugLogger extends Logger {
    write(message) {
        console.log(`DEBUG: ${message}`);
    }
}

class ErrorLogger extends Logger {
    write(message) {
        console.log(`ERROR: ${message}`);
    }
}

function getChainOfLoggers() {
    const errorLogger = new ErrorLogger(ERROR);
    const debugLogger = new DebugLogger(DEBUG);
    const infoLogger = new InfoLogger(INFO);

    errorLogger.setNextLogger(debugLogger);
    debugLogger.setNextLogger(infoLogger);

    return errorLogger;
}

const loggerChain = getChainOfLoggers();

loggerChain.logMessage(INFO, "This is an info.");
loggerChain.logMessage(DEBUG, "This is a debug.");
loggerChain.logMessage(ERROR, "This is an error.");
