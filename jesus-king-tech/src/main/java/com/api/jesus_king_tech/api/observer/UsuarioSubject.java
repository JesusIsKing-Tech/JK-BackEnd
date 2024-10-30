package com.api.jesus_king_tech.api.observer;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioSubject {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String eventType, Usuario usuario) {

        System.out.println(observers);

        for (Observer observer : observers) {
            observer.update(eventType, usuario);
        }
    }
}
