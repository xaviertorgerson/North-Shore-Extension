/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trackcont_subsys;
/**
 *
 * @author jeff
 */
public class SwitchStateSuggestion {
    int blockNum;
    boolean[] state;
    int[] trainNum;
    public SwitchStateSuggestion(int bn, boolean[] s, int[] tn){
        blockNum=bn;
        state=s;
        trainNum=tn;
    }
}
