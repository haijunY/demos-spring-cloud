package com.yhj.study.designpattern.strategy.stimulateduck.quackbehaviour;

public class GaGaQuackBehaviour implements QuackBehaviour {

    @Override
    public void quack() {
        System.out.println("---GaGaQuack---");
    }

}
