Terms/Concepts:
 *      current block: the block that is being fed into the PLC, the block being worked on
 *      	This block will recieve the new block state if the PLC logic statement is true (the then of the if then)
 *      relative block: a block within the jurisdiction of the controller that will be tested
 *      	This block is tested acording to the if part of the PLC logic statement
 *      relative block #: A veriable that says how many blocks relative to the the current block will be tested
 *      	i.e. if the relative block # is equal to 2 and the current block number is equal to 4 
 *      		the relative block state will be tested block 4, then on the block next to block 4, finally next next
 *      	     Similarly if rb#=-1 and current block Number=23 then the relative block state will be tested on
 *      		block 23 and the block previous to 23
 *      relative block state: the state of the relative block being tested
 *      new block state: if the relative block state is true then the current block will be given this new state
 *      general: applies to every block
 *      explicit: only applies to the block specified
 *      logic type: the logic statement will only apply to blocks that are this type
 *      Priority: priority within a logic type is implicit given the oder of the logic statements (top to bottom)
 *      	the top logic statements will have lower priority compared to the lower statements
 *      	Priority of the logic types are as follows (1)general,(2)crossing,(3)switch,(4)explicit where 4 is highest priority

IMPORTANT: PLC code is case sensative, make sure that the PLC logic you write matches the way it is written in the PLCLogic and TrackCont_PLC java codes

PLC logic statements are written in the following format
 *      (logic type):
 *      if (relative block #) (relative block state) then (new block state) (explicit block # NOTE: optional)
 * 
 *      Logic Types:
 *          crossing: logic for railroad crossing blocks goes to crossingLogic arraylist
 *          switch: logic for switching blocks goes to switchLogic arraylist
 *          failure: logic for failed blocks goes to failureLogic arraylist
 *          occupied: logic for occupied blocks goes to occupiedLogic arraylist
 *          general: logic for general track blocks, track blocks with no special properties goes to generalLogic arraylist, will have lowest priority
 *          explicit: logic for explicit track blocks, track blocks that need extra logic goes to explicitLogic arraylist, will have highest priority
 *      
 *      relative block
 *          The number of blocks that will be tested, the PLC will check for the 'relative block state' on every designated block
 *          -x means that block is x blocks behind the track block currently being tested (iterate using getPreviousBlock)
 *          +x means that the relative block state will be tested on (iterate using getNextBlock)
 *  
 *      relative block state: the 'N' prefix denotes NOT, i.e. Noccupied means not occupied
 *          occupied: relative block is occupied by a train
 *          failure: relative block has failed
 *          switchSet(1 or 0): relative block has switch set to 1 or 0
 *          trainInSugList: relative block is occupied by a train in this switches switch state suggestion list
 *          no(Next or Prev)Connection: relative block returns a null getNextBlock or getPreviousBlock
 *          temp: the train temperture is below a set threshold
 *      
 *      new block state:
 *          stop or start: set the blocks go to 0 or 1
 *          cross(1 or 0): set crossing's state to 0 or 1
 *          switch(1 or 0): set switch's state to 0 or 1
 *          switchSug: set switch's state to whatever is stored in the switch suggestion list
 *          fail(1 or 0): set blocks failure sate to 0 or 1
 *          heat(1 or 0): set blocks heater
 *          addTrain: add train to block, not currently in use
 *      
 *      explicit block #:
 *          the logic will only apply to this specified block 
 *          This should only be used in the explicit logic function