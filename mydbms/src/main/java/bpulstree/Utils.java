package bpulstree;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Utils类包含了一些辅助编码和测试的方法
 * 
 */
public class Utils {

	/**
	 * 批量插入测试数据
	 * 
	 * @param b
	 * @param tests
	 */
	public static void bulkInsert(BPlusTree b, int[] tests) {
		for (int i = 0; i < tests.length; i++) {
			b.insert(tests[i], String.valueOf(tests[i]));
		}

	}

	/**
	 * 把当前tree输出到控制台
	 * 
	 * @param tree
	 */
	public static void printTree(BPlusTree tree) {
		/* 临时队列. */
		LinkedBlockingQueue<Node> queue;

		/* 保存node指针. */
		queue = new LinkedBlockingQueue<Node>();
		String result = "";

		int nodesInCurrentLevel = 1;
		int nodesInNextLevel = 0;
		ArrayList<Integer> childrenPerIndex = new ArrayList<Integer>();
		queue.add(tree.root);
		while (!queue.isEmpty()) {
			Node target = queue.poll();
			nodesInCurrentLevel--;
			if (target.isLeafNode) {
				LeafNode leaf = (LeafNode) target;
				result += "[";
				for (int i = 0; i < leaf.keys.size(); i++) {
					result += "(" + leaf.keys.get(i) + " , "
							+ leaf.values.get(i) + ");";
				}
				childrenPerIndex.set(0, childrenPerIndex.get(0) - 1);
				if (childrenPerIndex.get(0) == 0) {
					result += "] $ ";
					childrenPerIndex.remove(0);
				} else {
					result += "] # ";
				}
			} else {
				IndexNode index = ((IndexNode) target);
				result += "@ ";
				for (int i = 0; i < index.keys.size(); i++) {
					result += "" + index.keys.get(i) + "/";
				}
				result += "@   ";
				queue.addAll(index.children);
				if (index.children.get(0).isLeafNode) {
					childrenPerIndex.add(index.children.size());
				}
				nodesInNextLevel += index.children.size();
			}

			if (nodesInCurrentLevel == 0) {
				result += "\n";
				nodesInCurrentLevel = nodesInNextLevel;
				nodesInNextLevel = 0;
			}

		}
		System.out.println(result);

	}

}
