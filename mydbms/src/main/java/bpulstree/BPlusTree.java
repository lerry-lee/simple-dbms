package bpulstree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map.Entry;

/*
BPlusTree类假设：
1.没有重复的键值被插入
2.阶数D<=一个节点中key的数量<=2D
3.所有key均为非负数
*/

public class BPlusTree {

	public Node root;
	public static final int D = 2;

	/**
	 * TODO 查询指定key的value
	 * 
	 * @param key
	 * @return value
	 */
	public String search(int key) {
		// 查找包含key的叶子节点
		LeafNode leaf = findLeafNodeWithKey(root, key);

		// 查找key对应的value并返回
		for (int i = 0; i < leaf.keys.size(); i++){
			if (leaf.keys.get(i) == key){
				return leaf.values.get(i); 
			}
		}
		return null; 
	}

	/**
	 * TODO 将一组key-value键值对插入到BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(int key, String value) {
		// 初始化插入树
			if (root == null){
			root = new LeafNode(key, value);
		}
		
		Entry<Integer, Node> overflowed = insertHelper(root, key, value);
		if (overflowed != null){
			// root层发生溢出
			root = new IndexNode(overflowed.getKey(), root, overflowed.getValue());
		}

	}
	private Entry<Integer, Node> insertHelper(Node node, int key, String value){
		Entry<Integer,Node> overflow = null; 
		if (node.isLeafNode){
			LeafNode leaf = (LeafNode) node; 
			leaf.insertSorted(key, value);
			if (leaf.isOverflowed()){
				Entry<Integer, Node> rightSplit = splitLeafNode(leaf);
				return rightSplit;
			}
			return null; 
		}
		else {
			IndexNode idxNode = (IndexNode) node; 
			if (key < node.keys.get(0)) 
				overflow = insertHelper(idxNode.children.get(0), key, value);
			else if (key >= node.keys.get(idxNode.keys.size() - 1))
				overflow = insertHelper(idxNode.children.get(idxNode.children.size() - 1), key, value); 
			else {
				// insert at one of the middle child
				for (int i = 0; i < idxNode.children.size(); i++){
					if (idxNode.keys.get(i) > key){
						overflow = insertHelper(idxNode.children.get(i), key, value);
						break;
					}
				}
			}
		}
		if (overflow != null){
			IndexNode idxNode = (IndexNode)node;
			
			// 指出插入溢出索引的位置
			int splittingKey = overflow.getKey();
			int indexAtParent = idxNode.keys.size();
			if (splittingKey < idxNode.keys.get(0)){
				indexAtParent = 0; 
			} else if (splittingKey > idxNode.keys.get(idxNode.keys.size() -1)){
				indexAtParent = idxNode.children.size(); 
			} else {
				for (int i = 0; i < idxNode.keys.size(); i++){
					if (i < idxNode.keys.get(i)){
						indexAtParent = i;
					}
				}
			}
			
			idxNode.insertSorted(overflow, indexAtParent);
			if (idxNode.isOverflowed()){
				Entry<Integer, Node> rightSplit = splitIndexNode(idxNode);
				return rightSplit;
			}
			return null;
		}
		return overflow;
		
	}


	/**
	 *重新排列leftLeaf的nextLeaf指针的指针
	 * @param leftLeaf 
	 * @param rightLeaf
	 */
	private void manageSiblingPtrs(LeafNode leftLeaf, LeafNode rightLeaf) {
		if (leftLeaf.nextLeaf != null){
			rightLeaf.nextLeaf = leftLeaf.nextLeaf;
		}
		leftLeaf.nextLeaf = rightLeaf; 	
	}

	/**
	 * TODO 拆分叶节点并返回新的右节点和拆分
	 * key作为条目<slitingKey, RightNode>
	 * 
	 * @param leaf
	 * @return key-node作为条目
	 */
	public Entry<Integer, Node> splitLeafNode(LeafNode leaf) {
		int RIGHT_BUCKET_SIZE = D+1;
		 
		ArrayList<Integer> rightKeys = new ArrayList<Integer>(RIGHT_BUCKET_SIZE); 
		ArrayList<String> rightValues = new ArrayList<String>(RIGHT_BUCKET_SIZE);
		
		rightKeys.addAll(leaf.keys.subList(D, leaf.keys.size()));
		rightValues.addAll(leaf.values.subList(D, leaf.values.size())); 
	
		// 从左删除右侧
		leaf.keys.subList(D, leaf.keys.size()).clear();
		leaf.values.subList(D, leaf.values.size()).clear();
		
		LeafNode rightLeaf = new LeafNode(rightKeys, rightValues);
		
		// 管理新的兄弟节点
		manageSiblingPtrs(leaf, rightLeaf);

		return new AbstractMap.SimpleEntry<Integer, Node>(rightLeaf.keys.get(0), rightLeaf);

	}

	/**
	 * TODO 拆分indexNode并返回新的右节点和拆分
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index
	 * @return new key/node pair as an Entry
	 */
	public Entry<Integer, Node> splitIndexNode(IndexNode index) {
		int BUCKET_SIZE = D;  
		ArrayList<Integer> rightKeys = new ArrayList<Integer>(BUCKET_SIZE); 
		ArrayList<Node> rightChildren = new ArrayList<Node>(BUCKET_SIZE + 1);
		
		rightKeys.addAll(index.keys.subList(D+1, index.keys.size()));
		rightChildren.addAll(index.children.subList(D+1, index.children.size())); 
		
		// push up the new index
		IndexNode rightNode = new IndexNode(rightKeys, rightChildren);
		AbstractMap.SimpleEntry<Integer, Node> splitted = new AbstractMap.SimpleEntry<Integer, Node>(index.keys.get(D), rightNode);

		// delete the right side from the left
		index.keys.subList(D, index.keys.size()).clear();
		index.children.subList(D+1, index.children.size()).clear();
		
		return splitted;
	}

	/**
	 * TODO 从B+tree删除某个键值对
	 * 
	 * @param key
	 */
	public void delete(int key) {
		int index = deleteHelper(null, root, key);
		if (index != -1){
			root.keys.remove(index);
			if (root.keys.size() == 0){
				root = ((IndexNode) root).children.get(0);
			}
		}
		
		if (root.keys.size() == 0){
			root = null;
		}
	}

	/**
	 * delete方法帮助.
	 * @param parent :
	 * 			包含key的节点的父节点
	 * @param node : 
	 * 			包含key的节点
	 * @param key :
	 * 			要查询的key
	 * @return
	 */
	private int deleteHelper(IndexNode parent, Node node, int key) {
		int indexToDelete = -1; 
		
		// 在父节点中找node的索引
		int indexInParent = -1; 
		if (parent != null){
			for (indexInParent = 0; indexInParent < parent.children.size(); indexInParent++){
				if (parent.children.get(indexInParent) == node){
					break; 
				}
			}
		}
		
		if (node.isLeafNode){
			LeafNode leafNode = (LeafNode) node; 
			for (int i = 0; i < leafNode.keys.size(); i++){
				if (leafNode.keys.get(i) == key){
					// 从叶节点的keys中删除
					leafNode.keys.remove(i); 
					// 删除对应的value
					leafNode.values.remove(i); 
					break;
				}
			}

			
			// 检查下溢
			if (leafNode.isUnderflowed() && leafNode != root){
				// 找到驻留在父节点中的索引叶节点
				if (indexInParent - 1 >= 0){
					// 节点有左孩子
					LeafNode left = (LeafNode) parent.children.get(indexInParent -1);
					return handleLeafNodeUnderflow(left, leafNode, parent);
				} else {
					// 节点没有左孩子
					LeafNode right = (LeafNode) parent.children.get(indexInParent + 1); 
					return handleLeafNodeUnderflow(leafNode, right, parent);
				}
			} else {
				// 如果删除拆分key，可能需要更新父节点/祖先节点
				if (leafNode.keys.size() > 0)
					updateIndexNodeKeyWithKey(root, key, leafNode.keys.get(0));
				return -1; // 删除不会造成下溢
			}
			
		} 
		
		else {
			// 节点是索引节点
			IndexNode idxNode = (IndexNode) node; 
			if (key < idxNode.keys.get(0)){
				// go down first child
				indexToDelete = deleteHelper(idxNode, idxNode.children.get(0), key);
			}
			else if (key >= idxNode.keys.get(idxNode.keys.size() - 1)){
				// go down last child
				indexToDelete = deleteHelper(idxNode, idxNode.children.get(idxNode.children.size() - 1), key);
			}
			else {
				// go down the middle children
				for (int i = 0; i < idxNode.keys.size(); i++){
					if (idxNode.keys.get(i) > key){
						indexToDelete = deleteHelper(idxNode, idxNode.children.get(i), key);
					}
				}
			}
		}
		
		// 是否有要删除的索引还存在
		if (indexToDelete != -1){
			if (node == root ){
				return indexToDelete; 
			}
			node.keys.remove(indexToDelete);
			
			// 删除造成下溢
			if (node.isUnderflowed()){
				// determine if node has left sibling
				IndexNode left = (IndexNode)node; 
				IndexNode right = (IndexNode)node; 
				
				// check to see if indexNode has sibling
				if (indexInParent - 1 >= 0){
					left = (IndexNode) parent.children.get(indexInParent - 1);  
				} else {
					right = (IndexNode) parent.children.get(indexInParent + 1);  
				}
				return handleIndexNodeUnderflow(left, right, parent);  
			}
		}
		
		return -1; 
	}

	/**
	 * TODO 处理LeafNode下溢（合并或重新分配）
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode left, LeafNode right,
			IndexNode parent) { 
		
		// merge
		if (left.keys.size() + right.keys.size() < 2*D){
			left.keys.addAll(right.keys); 
			left.values.addAll(right.values);
			left.nextLeaf = right.nextLeaf;
		
			// delete the other node
			int indexInParent = parent.children.indexOf(right);
			parent.children.remove(indexInParent);
			return indexInParent -1; 
		}
		
		// re-distribute
		int childsIndexInParent;
		if (left.isUnderflowed()){
			childsIndexInParent = parent.children.indexOf(right);
			// get the minimum key value of right
			left.insertSorted(right.keys.remove(0), right.values.remove(0));
		} else {
			childsIndexInParent = parent.children.indexOf(right);
			// get maximum key value of left
			right.insertSorted(left.keys.remove(left.keys.size()-1), left.values.remove(left.values.size()-1));
			parent.keys.set(childsIndexInParent - 1, parent.children.get(childsIndexInParent).keys.get(0));
		}
		parent.keys.set(childsIndexInParent - 1, parent.children.get(childsIndexInParent).keys.get(0));
		
		// update the parent's index key
		
		
		return -1;

	}

	/**
	 * TODO 处理IndexNode下溢（合并或重新分配）
	 * 
	 * @param leftIndex
	 *            : the smaller node
	 * @param rightIndex
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode leftIndex,
			IndexNode rightIndex, IndexNode parent) {
		int separatingKey;
		int index; 
		
		// find separating key value from parent  
		for (index = 0; index < parent.keys.size(); index++){
			if (parent.children.get(index) == leftIndex && parent.children.get(index+1) == rightIndex){
				break; 
			}
		}
		
		separatingKey = parent.keys.get(index);
		
		// Action : merge
		if (leftIndex.keys.size() + rightIndex.keys.size() < 2*D){
			// move separating key down 
			leftIndex.keys.add(separatingKey); 
			leftIndex.keys.addAll(rightIndex.keys);
			
			leftIndex.children.addAll(rightIndex.children);
			
			// delete the right side
			parent.children.remove(parent.children.indexOf(rightIndex));
			return index; 
		
		}

		// Action: Distribute
		if (leftIndex.isUnderflowed()){
			// move separating key down to leftIndex
			leftIndex.keys.add(separatingKey);
			// move leftmost key from right up 
			parent.keys.set(index, rightIndex.keys.remove(0)); 
			// leftmost child of right is now left's
			leftIndex.children.add(rightIndex.children.remove(0));
		}
		else if (rightIndex.isUnderflowed()) {
			// move separating key down to rightIndex
			rightIndex.keys.add(0, separatingKey); 
			// the last child of left index sibling is now right index's
			Node lastChild = leftIndex.children.remove(leftIndex.children.size() - 1);
			rightIndex.children.add(0, lastChild); 
			// move rightmost key from leftIndex up
			parent.keys.set(parent.keys.size()-1, leftIndex.keys.remove(leftIndex.keys.size() - 1));
		}
		
		return -1;
	}
	
	/**
	 * 查找要插入的关键点的LeafNode
	 * @param key
	 * 			: the key used to find the LeafNode it is to be inserted
	 * @return the LeafNode the key is to be inserted to
	 */
	private LeafNode findLeafNodeWithKey(Node theNode, int key){
		if (theNode == null)
			return null; 
		
		if (theNode.isLeafNode){
			// Found the LeafNode
			return (LeafNode) theNode;
		}
		else {
			// The node is an index node
			IndexNode indexNode = (IndexNode) theNode;
			
			if (key < theNode.keys.get(0)){
				return findLeafNodeWithKey(indexNode.children.get(0), key);
			}
			else if (key >= theNode.keys.get(theNode.keys.size() - 1)) {
				return findLeafNodeWithKey(indexNode.children.get(indexNode.children.size() - 1), key);
			}
			else {
				ListIterator<Integer> iterator = indexNode.keys.listIterator();
				while (iterator.hasNext()){
					if (iterator.next() > key){
						return findLeafNodeWithKey(indexNode.children.get(iterator.previousIndex()), key); 
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds the indexNode with a specific key. 
	 * Method used to update ancestors that may contain a deleted key
	 * @param theNode
	 * @param searchKey :
	 * 			the identifying key
	 * @param newKey :
	 * 			the newKey to update the old key with
	 * @return
	 */
	private void updateIndexNodeKeyWithKey(Node theNode, int searchKey, int newKey){
		if (theNode == null) 
			return;
		
		if (theNode.isLeafNode) 
			return; 
		
		IndexNode idxNode = (IndexNode) theNode;
		for (int i = 0; i < theNode.keys.size(); i++){
			// not here, don't need to keep going
			
			if (idxNode.keys.get(i) > searchKey){
				break; 
			}
			
			if (idxNode.keys.get(i) == searchKey){
				idxNode.keys.set(i, newKey);
				return;
			}
		}
		
		// not found, perhaps in another child
		if (searchKey < idxNode.keys.get(0)){
			updateIndexNodeKeyWithKey(idxNode.children.get(0), searchKey, newKey); 
		} else if (searchKey > idxNode.keys.get(idxNode.keys.size() - 1)){
			updateIndexNodeKeyWithKey(idxNode.children.get(idxNode.children.size() - 1), searchKey, newKey);
		} else {
			for (int i = 0; i < theNode.keys.size(); i++){
				if (idxNode.keys.get(i) > searchKey){
					updateIndexNodeKeyWithKey(idxNode.children.get(i), searchKey, newKey); 
				}
			}
			
		}
	}
	//TODO 索引的更新：更新叶子节点
	public void update(int key,String value){
			LeafNode leafNode=findLeafNodeWithKey(root,key);
			ArrayList<Integer> keys=leafNode.keys;
			ArrayList<String> values=leafNode.values;
			ListIterator<Integer> iterator = keys.listIterator();
		while (iterator.hasNext()) {
			if (iterator.next() == key) {
				int position = iterator.previousIndex();
				values.set(position, value);
				break;
			}
		}
	}
}
