package com.temhorario.api.config.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Trata exceções de regra de negócio (ex: "Este horário já está reservado.")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(ex.getMessage()));
    }

    // 2. Trata erros de validação do @Valid nos DTOs (retorna campo + mensagem)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDataDTO>> handleValidationError(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ValidationErrorDataDTO::new).toList());
    }

    // 3. Trata a trava de segurança do MySQL (Unique Constraints, FKs, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponseDTO("Conflito de dados: o registro ou horário selecionado já existe.")
        );
    }
}