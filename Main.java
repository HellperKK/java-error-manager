public class Main {
    public static void main(String[] args) {
        Result.begin(42)
            .run(elem -> divide(elem, 2))
            .show()
            .run(elem -> divide((Integer)elem, 0))
            .show();
    }

    public static Result<Integer> divide(Integer f, Integer s) {
        if (s != 0) {
            return new Success(f / s);
        }
        else {
            return new Error("Dividing by Zero");
        }
    }

    public static <V> V show(V elem) {
        System.out.println(elem);
        return elem;
    }
}

interface AtomLambda<A, B> {
    public B call(A value);
}

abstract class Result<V> {
    public static <C> Result<C> begin(C elem) {
        return new Success(elem);
    }

    abstract public <N, O> Result<N> run(AtomLambda<V, O> lam);
    abstract public <N, O> Result<N> capture(AtomLambda<String, O> lam);
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

    public <N, O> Result<N> run(AtomLambda<V, O> lam) {
        O result = lam.call(elem);
        if (result instanceof Result) {
            Result<N> conv = (Result<N>) result;
            return conv;
        }
        else {
            return new Success(result);
        }
    }

    public <N, O> Result<N> capture(AtomLambda<String, O> lam) {
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

    public <N, O> Result<N> run(AtomLambda<V, O> lam) {
        return new Error(elem);        
    }

    public <N, O> Result<N> capture(AtomLambda<String, O> lam) {
        O result = lam.call(elem);
        if (result instanceof Result) {
            Result<N> conv = (Result<N>) result;
            return conv;
        }
        else {
            return new Success(result);
        }
    }

    public boolean succeded() {
        return true;
    }

    public String toString() {
        return elem.toString();
    }
}