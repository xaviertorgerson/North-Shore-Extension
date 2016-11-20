/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;

/**
 *
 * @author jeff
 */
public class BetterList {
    private class LList{
        public PLCLogic logic;
        public int blockNum;
        public LList next;
        public LList(LList ll){
            blockNum=ll.blockNum;
            next=ll.next;
            logic=ll.logic;
        }
        public LList(int bn, PLCLogic l){
            blockNum=bn;
            logic=l;
        }
    }
    private LList last;
    private LList first;
    public BetterList(int bn,PLCLogic l){
        first=new LList(bn, l);
        last=first;
    }
    public void add(int bn, PLCLogic l){
        last.next=new LList(bn, l);
        last=last.next;
    }
    public PLCLogic find(int bn){
        LList temp=new LList(first);
        while(temp!=null){
            if(temp.blockNum==bn){
                return temp.logic;
            }
            temp=temp.next;
        }
        return null;
    }
}
