package com.cognitree.bootique.discovery.agent;

import io.bootique.Bootique;

public class Application {

    public static void main(String[] args) {
        Bootique.app(args).module(new ConsulAgentModuleProvider()).autoLoadModules().exec();
    }
}
