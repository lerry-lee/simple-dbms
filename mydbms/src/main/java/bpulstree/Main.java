package bpulstree;


import java.util.List;

public class Main {

	/**
	 * @param list
	 */

	public static BPlusTree createBPlusTree(List<List<String>> list) {
		BPlusTree myTree = new BPlusTree();

		//将主键值和文件名插入到B+树的节点
		for(int i=0;i<list.size();i++){
			int key= Integer.parseInt(list.get(i).get(0));
			String value=list.get(i).get(1);
			myTree.insert(key,value);
		}
		//Utils.printTree(myTree);
		return myTree;
	}

	public static BPlusTree loadIndex(List<List<String>> list){
		BPlusTree tree=new BPlusTree();

		for(int i=0;i<list.size();i++){
			int key=Integer.parseInt(list.get(i).get(0).substring(1,list.get(i).get(0).length()));
			String value=list.get(i).get(1);
			tree.insert(key,value);
		}
		return tree;
	}

}
