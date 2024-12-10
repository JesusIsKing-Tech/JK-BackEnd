package com.api.jesus_king_tech.log;

import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final PilhaObj<ExceptionLog> pilhaDeLogs;

    public LogService() {
        this.pilhaDeLogs = new PilhaObj<>(100);
    }

    public void adicionarLog(ExceptionLog log) {
        pilhaDeLogs.push(log);
    }

    public ExceptionLog obterUltimoLog() {
        return pilhaDeLogs.isEmpty() ? null : pilhaDeLogs.peek();
    }

    public void exibirLogs() {
        pilhaDeLogs.exibe();
    }
}
