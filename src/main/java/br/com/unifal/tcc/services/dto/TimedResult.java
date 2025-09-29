package br.com.unifal.tcc.services.dto;

import java.time.Duration;

public record TimedResult<T>(T result, Duration duration) {}
