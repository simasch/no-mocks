package ch.martinelli.demo.nomocks;

import org.springframework.boot.SpringApplication;

public class TestNoMocksApplication {

    public static void main(String[] args) {
        SpringApplication.from(NoMocksApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
