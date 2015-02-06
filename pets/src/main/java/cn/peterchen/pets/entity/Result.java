package cn.peterchen.pets.entity;

/**
 * the general HttpResponse
 * Created by peter on 15-2-6.
 */
public class Result<T> {

    private int code;

    private String msg;

    private T result;


    public boolean isSuccess() {
        if (code == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
