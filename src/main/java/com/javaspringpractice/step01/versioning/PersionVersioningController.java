package com.javaspringpractice.step01.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersionVersioningController {

    @GetMapping("v1/person")
    public  PersonV1 persionV1() {
        return new PersonV1("Bob Charlie");
    }

    @GetMapping("v2/person")
    public  PersonV2 persionV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    //PARAMS
    @GetMapping(path="person/param", params="version=1")
    public PersonV1 paramV1() {
        return new PersonV1("Bob Chalie");
    }

    @GetMapping(path="person/param", params="version=2")
    public PersonV2 paramV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    //HEADER
    @GetMapping(path="person/header", headers="X-API-VERSION=1")
    public PersonV1 headerV1() {
        return new PersonV1("Bob Chalie");
    }

    @GetMapping(path="person/header", headers="X-API-VERSION=2")
    public PersonV2 headerV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }

    //PRODUCES
    @GetMapping(path="person/produces", produces="application/vnd.company.app-v1+json")
    public PersonV1 producesV1() {
        return new PersonV1("Bob Chalie");
    }

    @GetMapping(path="person/produces", produces="application/vnd.company.app-v2+json")
    public PersonV2 producesV2() {
        return new PersonV2(new Name("Bob", "Charlie"));
    }
}
