package com.flipmed.system.exception;

public class FlipMedException extends RuntimeException {
    public FlipMedException(String message) {
        super(message);
    }
}

class BookingNotFoundException extends FlipMedException {
    public BookingNotFoundException(String message) { super(message); }
}

class OverlapException extends FlipMedException {
    public OverlapException(String message) { super(message); }
}

class DoctorNotFoundException extends FlipMedException {
    public DoctorNotFoundException(String message) { super(message); }
}
