package ch.martinelli.demo.nomocks.domain;

public record Customer(Long id, String firstName, String lastName, String street, String postalCode, String city) {
}
