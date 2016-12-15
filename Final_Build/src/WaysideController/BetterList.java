/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeff
 */
//A simple linked list class used to make explicit block plc logic instructions easy to locate
//essentially ties an instruction to a block and allows the user to quickly find the instruction using the block number
//Also allows the user to enter an unspecified number of block tied PLC instructions
public class BetterList {
    //Simple linked list class, links together nodes that contain a block number and PLC logic for that block number
    private class LList{
        public PLCLogic logic;
        public int blockNum;
        public LList next;
        public boolean found;
        public LList(LList ll){
            blockNum=ll.blockNum;
            next=ll.next;
            logic=ll.logic;
            found=false;
        }
        public LList(int bn, PLCLogic l){
            blockNum=bn;
            logic=l;
        }
        public void setFound(boolean f){
            found=f;
        }
    }
    private LList last;
    private LList first;
    public BetterList(int bn,PLCLogic l){
        first=new LList(bn, l);
        last=first;
    }
    //add a new PLC logic that is tied to the given block number
    public void add(int bn, PLCLogic l){
        last.next=new LList(bn, l);
        last=last.next;
    }
    //find the PLC logic for a given block number
    public PLCLogic find(int bn){
        LList temp=new LList(first);
        while(temp!=null){
            if(temp.blockNum==bn && !temp.found){
                temp.setFound(true);
                return temp.logic;
            }
            temp=temp.next;
        }
        return null;
    }
    public void resetFind(){
        LList temp=new LList(first);
        while(temp!=null){
            temp.setFound(false);
            temp=temp.next;
        }
    }
}
