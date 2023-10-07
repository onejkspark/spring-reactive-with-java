package com.example.ch1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class StepVerifierEx1Test {

    private final StepVerifierEx1 sut = new StepVerifierEx1();

    @Test
    @DisplayName("StepVerifier을 사용한 간단한 검증 로직")
    void expectNext_ok() {

        StepVerifier.create(sut.getStrings())
                .expectNext("foo")
                .expectNext("bar")
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("StepVerifier을 사용한 예외 처리 검증 로직 - 1,2 다음 에러가 발생")
    void test1() {

        var mock = sut.getError();

        StepVerifier.create(mock)
                .expectNext(1)
                .expectNext(2)
                .expectErrorMatches(t -> t instanceof IllegalArgumentException && t.getMessage().equals(sut.getMsg()))
                .verify();
    }

    @Test
    @DisplayName("StepVerifier을 사용한 숫자 검증 로직")
    public void test2() throws Exception {
        StepVerifier.create(sut.getInts())
                .expectNextCount(100)
                .verifyComplete();
    }
}

