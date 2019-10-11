public class Main {
    public static void main(String[] args) {
        Result.begin(42)            // starts with 42
            .run(elem -> elem / 2)  // divides by 2
            .show()                 // show the result
            .run(elem -> elem / 0)  // tries to divide by 0 and fails
            .run(elem -> elem / 3)  // since error is not captured, does nothing
            .capture(message -> 42) // captures the error and rolls back to 42
            .run(elem -> elem / 3)  // divides by 3
            .show();                // finally show the result
    }
}

interface AtomLambda<A, B> {
    public B call(A value);
}

abstract class Result<V> {
    public static <C> Result<C> begin(C elem) {
        return new Success(elem);
    }

    abstract public <N> Result<N> run(AtomLambda<V, N> lam);
    abstract public <N> Result<N> capture(AtomLambda<String, N> lam);
    abstract public boolean succeded();


    public Result<V> show() {
        System.out.println(this);
        return this;
    }
}

class Success<V> extends Result<V> {
    private V elem;

    public Success(V elem) {
        this.elem = elem;
    }

    public <N> Result<N> run(AtomLambda<V, N> lam) {
        try {
          return new Success(lam.call(elem));
        }
        catch(Exception e) {
          return new Error(e.getMessage());
        }
    }

    public <N> Result<N> capture(AtomLambda<String, N> lam) {
        return new Success(elem);
    }

    public boolean succeded() {
        return true;
    }

    public String toString() {
        return elem.toString();
    }
}

class Error<V> extends Result<V> {
    private String elem;

    public Error(String elem) {
        this.elem = elem;
    }

    public <N> Result<N> run(AtomLambda<V, N> lam) {
        return new Error(elem);
    }

    public <N> Result<N> capture(AtomLambda<String, N> lam) {
        try {
          return new Success(lam.call(elem));
        }
        catch(Exception e) {
          return new Error(e.getMessage());
        }
    }

    public boolean succeded() {
        return true;
    }

    public String toString() {
        return elem.toString();
    }
}
