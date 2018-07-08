package bj.albon.arith.config.parser.api.service;

public class InvalidListener implements Listener {
    @Override
    public void process(Object config) {
        throw new UnsupportedOperationException("此类此方法不可调用。");
    }
}
