package com.api.jesus_king_tech.log;

import java.time.LocalDateTime;

public class ExceptionLog {
    private String mensagemErro;
    private String classeOrigem;
    private String metodoOrigem;
    private LocalDateTime timestamp;

    public ExceptionLog(String mensagemErro, String classeOrigem, String metodoOrigem) {
        this.mensagemErro = mensagemErro;
        this.classeOrigem = classeOrigem;
        this.metodoOrigem = metodoOrigem;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] Exception em " + classeOrigem + "." + metodoOrigem +
                ": " + mensagemErro;
    }
}
