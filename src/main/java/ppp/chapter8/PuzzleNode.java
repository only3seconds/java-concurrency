package ppp.chapter8;

import java.util.LinkedList;
import java.util.List;

/**
 * 用于谜题解决框架的链表节点.PuzzleNode代表通过一系列的移动到达的一个位置，其中保存了到达该位置的移动以及前一个PuzzleNode
 */
public class PuzzleNode<P, M> {
    final P pos;
    final M move;
    final PuzzleNode<P, M> prev;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    List<M> asMoveList() {
        List<M> solution = new LinkedList<M>();
        for (PuzzleNode<P, M> n = this; n.move != null; n = n.prev) {
            solution.add(0, n.move);
        }

        return solution;
    }


}
