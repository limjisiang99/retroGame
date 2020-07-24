package Dungeon;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The Enemy type representing a "Smart Enemy"
 * @author 985803	
 * @version 1.0
 * @since 04-12-2019
 */
class SmartEnemy extends Enemy {

/**
 * constructor for SmartEnemy object
 * @param x location x for SmartEnemy
 * @param y location y for SmartEnemy
 */
    public SmartEnemy(int x, int y) {
        super(x, y);
    }

    /**
     * Plans and directs where the enemy to move
     * @param walkable The walkable area of the enemy.
     * @param playerPos The player current position
     */    
    protected void planMove(Point playerPos, boolean[][] walkable) {
        ArrayList<Point> path = new ArrayList<Point>(findPath(x, y, playerPos.x, playerPos.y, walkable));
        if (!path.isEmpty()) {
            move(findPath(x, y, playerPos.x, playerPos.y, walkable).get(0));
        }
    }

    /**
     * Estimates distance between 2 nodes.
     *
     * @param nodeA The first node.
     * @param nodeB The second node.
     * @return Integer value representing the estimated cost to travel between
     * these nodes.
     */
    int heuristic(Node nodeA, Node nodeB) {
        int distX = Math.abs(nodeA.x - nodeB.x);
        int distY = Math.abs(nodeA.y - nodeB.y);
        if (distX > distY) {
            return 14 * distY + 10 * (distX - distY);
        } else {
            return 14 * distX + 10 * (distY - distX);
        }
    }

    /**
     * Generates the shortest path to a given position given a starting position
     * and all walkable positions
     *
     * @param startX
     * @param startY
     * @param targetX
     * @param targetY
     * @param walkable
     * @return array of points representing 2d space that lead from starting
     * position to end position
     */
    ArrayList<Point> findPath(int startX, int startY, int targetX, int targetY, boolean[][] walkable) {
        Node startNode = new Node(true, startX, startY); //starting point
        Node targetNode = new Node(true, targetX, targetY);  //end point
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        openList.add(startNode);// put starting point on to open list
        while (openList.size() > 0) { // while there are nodes left on list
            Node currentNode = openList.get(0);

            for (int i = 1; i < openList.size(); i++) {//sorts open list by f cost
                if (openList.get(i).fCost() < currentNode.fCost() || openList.get(i).fCost() == currentNode.fCost() && openList.get(i).hCost < currentNode.hCost) {
                    currentNode = openList.get(i);
                }
            }

            openList.remove(currentNode);
            //takes lowest f cost node off the open list
            closedList.add(currentNode);
            // and places it on closed list
            if (currentNode.nodeCompare() == targetNode.nodeCompare()) {
                // if at target
                ArrayList<Point> path = new ArrayList<>();
                // retrace our steps
                while (currentNode.nodeCompare() != startNode.nodeCompare()) {
                    path.add(new Point(currentNode.x, currentNode.y));
                    currentNode = currentNode.parent;

                }
                Collections.reverse(path);
                //and flip the path back the right way around, done.
                return path;

            }

            //add adjacent nodes to the neighbour list
            ArrayList<Node> neighbours = new ArrayList<>();
            if (walkable[currentNode.x + 1][currentNode.y] == true) {
                neighbours.add(new Node(true, currentNode.x + 1, currentNode.y));
            } else {
                neighbours.add(new Node(false, currentNode.x + 1, currentNode.y));
            }
            if (walkable[currentNode.x - 1][currentNode.y] == true) {
                neighbours.add(new Node(true, currentNode.x - 1, currentNode.y));
            } else {
                neighbours.add(new Node(false, currentNode.x - 1, currentNode.y));
            }
            if (walkable[currentNode.x][currentNode.y + 1] == true) {
                neighbours.add(new Node(true, currentNode.x, currentNode.y + 1));
            } else {
                neighbours.add(new Node(false, currentNode.x, currentNode.y + 1));
            }
            if (walkable[currentNode.x][currentNode.y - 1] == true) {
                neighbours.add(new Node(true, currentNode.x, currentNode.y - 1));
            } else {
                neighbours.add(new Node(false, currentNode.x, currentNode.y - 1));
            }

            boolean neighbourOnClosed;
            boolean neighbourOnOpen;
            for (int i = 0; i < neighbours.size(); i++) {
                neighbourOnOpen = false;
                neighbourOnClosed = false;
                Node neighbour = neighbours.get(i);
                for (int z = 0; z < closedList.size(); z++) {
                    Node tempNode = closedList.get(z);
                    if (tempNode.nodeCompare() == neighbour.nodeCompare()) {
                        neighbourOnClosed = true;
                    }
                }
                for (int z = 0; z < openList.size(); z++) {
                    Node tempNode = openList.get(z);
                    if (tempNode.nodeCompare() == neighbour.nodeCompare()) {
                        neighbourOnOpen = true;
                    }
                }
                if (!neighbour.walkable || neighbourOnClosed) {
                } else {
                    int gCostNew = currentNode.gCost + heuristic(currentNode, neighbour);
                    if (gCostNew < neighbour.gCost || !neighbourOnOpen) {

                        openList.remove(neighbour);
                        neighbour.gCost = gCostNew;
                        neighbour.hCost = heuristic(neighbour, targetNode);
                        neighbour.parent = currentNode;

                        openList.add(neighbour);
                        // if this node is more optimal than the node aleardy on the list, replace it
                    }
                }
            }
        }
        //returns empty list if no valid path e.g. player out of bounds or player is on the same cell
        return new ArrayList<>();

    }
	@Override
	protected void draw(int cellWidth, GraphicsContext gc, Image[][] images) {
		gc.drawImage(images[3][5], x*cellWidth, y*cellWidth);
		
	}

    @Override
    protected String toFile() {
        return "ESM" + "," + x + "," + y;
    }

}

/**
 * A class represents a Node
 * @author 985803	
 * @version 1.0
 * @since 04-12-2019
 */

class Node {

    protected boolean walkable; //if the node is walkable
    protected int gCost = 0; // movement cost
    protected int hCost = 0; // estimated distance cost
    protected int x; //x this node is located at
    protected int y;//y this node is located at
    protected Node parent = null; //the node that was previously travelled on to reach this node

    protected Node(boolean walkable, int x, int y) {
        this.walkable = walkable;
        this.x = x;
        this.y = y;
    }

    /**
     * getter for fcost
     *
     * @return fcost
     */
    protected int fCost() {
        return gCost + hCost;
    }

    /**
     * Checks to see if a node occupying x and y already exists
     *
     * @return true if there is a node already in that position
     */
    protected int nodeCompare() {
        return Objects.hash(x, y);
    }

}
