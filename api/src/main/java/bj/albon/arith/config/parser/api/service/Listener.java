package bj.albon.arith.config.parser.api.service;

/**
 * Author: pengyu.song
 * Date: 2018/7/8
 */
public interface Listener <T> {
    public void process(T config);
}
