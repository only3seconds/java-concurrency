package ppp.chapter8;

import java.util.Set;

/**
 * 表示"搬箱子"之类谜题的接口
 * @param <P>
 * @param <M>
 */
public interface Puzzle<P, M> {
    P initialPosition();
    boolean isGoal(P position);
    Set<M> legalMoves(P position);
    P move(P position, M move);
}
