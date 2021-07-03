/*
 * Complete this class.
 * Student Name: Mohammad Chama
 * Student ID No.: 10138553
 */
import javax.swing.*;


import java.util.*;
import java.util.Map.Entry;

public class RouterNode {
  private int myID;
  private GuiTextArea myGUI;
  private RouterSimulator sim;
  private int nodeCount = RouterSimulator.NUM_NODES;
  private HashMap<Integer, int[]> nhbrs;
  private int[] costs = new int[RouterSimulator.NUM_NODES];
  private int[] dVector = new int[RouterSimulator.NUM_NODES];
  private int[] path = new int[nodeCount];
  private boolean PoisonReverse = false;
  
  //--------------------------------------------------
  public RouterNode(int ID, RouterSimulator sim, int[] costs) {
    myID = ID;
    this.sim = sim;
    myGUI =new GuiTextArea("  Output window for Router #"+ ID + "  ");
    nhbrs = new HashMap<Integer, int[]>();

    //Copies done on cost and distance vector
    System.arraycopy(costs, 0, this.costs, 0, nodeCount);
    System.arraycopy(costs, 0, this.dVector, 0, nodeCount);
    //loop to fill the paths
    for(int node = 0; node < nodeCount; node++){
      if(costs[node] != RouterSimulator.INFINITY){
        int[] nd = new int[nodeCount];
        Arrays.fill(nd, RouterSimulator.INFINITY);
        nhbrs.put(node, nd);
        path[node] = node;
      }
      else{
        path[node] = RouterSimulator.INFINITY;
      }
    }
    //call to check PoisonReverse
    newUpdate();
  }

  //--------------------------------------------------
  public void recvUpdate(RouterPacket pkt) {
    //insert source and mincost
    nhbrs.put(pkt.sourceid, pkt.mincost);
    //call to see if there is any change for the vectors
    boolean change = updateVector();
    if(change){
      newUpdate();
    }
  }

  //--------------------------------------------------
  private boolean updateVector(){
    //initalize to false
    boolean change = false;
    //Bellman-ford Algorithm
    //loop through the amount of nodes and initiate cost and path
    for(int i = 0; i < nodeCount; ++i){
      int newCost = costs[i];
      int newPath = i;
      //loop through the hash map of neighbouring nodes and update
      //based on if there is change between costs
      for(Integer k : nhbrs.keySet()) {
        if(k == i){
          continue;
        }
        if(newCost > (nhbrs.get(k)[i] + costs[k])){
          newCost = nhbrs.get(k)[i] + costs[k];
          newPath = path[k];
        }
      }
      //set change to true if the cost changes
      if(dVector[i] != newCost){
        change = true;
      }
      //regardless of outcome, set the distance vector and path
      dVector[i] = newCost;
      path[i] = newPath;
    }
    return change;
    
       
  }

  //--------------------------------------------------
  private void newUpdate(){
    //loop to account for PoisonedReverse
    for(int i: nhbrs.keySet()){
      int[] pVector = Arrays.copyOf(dVector, nodeCount);
      if(PoisonReverse){
      for(int j = 0; j < nodeCount; j++){
        if(i == path[j]){
          pVector[j] = RouterSimulator.INFINITY;
        }
      
      }
    }
    
      RouterPacket pack = new RouterPacket(myID, i, pVector);
      sendUpdate(pack);
    }
  }
  

  //--------------------------------------------------
  private void sendUpdate(RouterPacket pkt) {
    sim.toLayer2(pkt);
  }
  

  //--------------------------------------------------
  public void printDistanceTable() {
    //print off the dist, cost and route
    myGUI.println("Current table for " + myID + "  at time " + sim.getClocktime());
    myGUI.println("minCostTable");
    myGUI.print("dist |    ");
    for (int i = 0; i < RouterSimulator.NUM_NODES; i++)
      myGUI.print(Integer.toString(i) + "  ");  
    myGUI.println();
    
    for (int i = 0; i < nodeCount; i++)
      myGUI.print("-------");
    myGUI.println();
    
    myGUI.print("cost |   ");
    for (int i = 0; i < nodeCount; i++)
            myGUI.print(Integer.toString(dVector[i]) + "  ");
    myGUI.println();
    
    myGUI.print("route |  ");
    for (int i = 0; i < nodeCount; i++)
    {
            myGUI.print(Integer.toString(path[i])+ "  ");
    }
    myGUI.println();
    myGUI.println();

  }

  //--------------------------------------------------
 

  public void updateLinkCost(int dest, int newcost) {
    costs[dest] = newcost;
    updateVector();
    newUpdate();
    

  }
  

}