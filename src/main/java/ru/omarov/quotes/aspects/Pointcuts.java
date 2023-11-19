package ru.omarov.quotes.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(public * ru.omarov.quotes.*.*get*(..))")
    public void allGetMethods() {
    }

    @Pointcut("execution(* ru.omarov.quotes.*.*(..))")
    public void allMethods() {
    }
}
