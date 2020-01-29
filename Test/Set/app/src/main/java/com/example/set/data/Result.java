package com.example.set.data;


public class Result<T> {

    private Result() { }

    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return success.getData().toString();
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return error.getError().getLocalizedMessage();
        }
        return "";
    }

    public final static class Success<T> extends Result {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    public final static class Error extends Result {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
