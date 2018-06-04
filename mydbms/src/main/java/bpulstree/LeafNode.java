package bpulstree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class LeafNode extends Node {
    //存储data的链表
    protected ArrayList<String> values;
    //指向下一个叶子节点的指针
    protected LeafNode nextLeaf;

    public LeafNode(int firstKey, String firstValue) {
        isLeafNode = true;
        keys = new ArrayList<Integer>();
        values = new ArrayList<String>();
        keys.add(firstKey);
        values.add(firstValue);
    }

    public LeafNode(List<Integer> newKeys, List<String> newValues) {
        isLeafNode = true;
        keys = new ArrayList<Integer>(newKeys);
        values = new ArrayList<String>(newValues);
    }


    //	插入key-value到叶节点，使节点仍然保持有序
    public void insertSorted(int key, String value) {
        //key小于第一个key，插入到最前面
        if (key < keys.get(0)) {
            keys.add(0, key);
            values.add(0, value);
        }
        //key大于最后一个key，插入到最后面
        else if (key > keys.get(keys.size() - 1)) {
            keys.add(key);
            values.add(value);
        }
        //找到key刚好小于的位置
        else {
            ListIterator<Integer> iterator = keys.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next() > key) {
                    int position = iterator.previousIndex();
                    keys.add(position, key);
                    values.add(position, value);
                    break;
                }
            }
        }
    }

}
