package ai;

import java.util.Vector;

import ai.Node.PathDirection;
import data.Map;
import data.Move;
import data.Position;

public abstract class PathFinder {
	public static final Vector<Move> Calculate(Map aMap, Position aStartPos, Position aGoalPos) {
		Vector<Node> nodes = new Vector<Node>();
		nodes.add(new Node(aMap, aStartPos, aGoalPos));
		Node finalNode = ProcessNodes(nodes, 100000);
		return finalNode != null ? finalNode.GetMoveList() : null;
	}

	public static final Node ProcessNodes(Vector<Node> aNodes, int aTickLimit) {
		int ticks = 0;
		while(!aNodes.isEmpty() && !aNodes.firstElement().IsAtGoal() && ticks++ < aTickLimit) {
			Node baseNode = aNodes.firstElement();
			aNodes.remove(aNodes.firstElement());
			for (PathDirection direction : PathDirection.values()) {
				Node newNode = new Node(baseNode, direction);
				if(!newNode.GetPosition().Compare(baseNode.GetPosition())) {
					if(aNodes.isEmpty()) {
						aNodes.add(newNode);
					} else {
						for(int i=0; i<aNodes.size(); i++) {
							if(newNode.GetTotalCost() < aNodes.elementAt(i).GetTotalCost()) {
								aNodes.add(i, newNode);
								break;
							} else if(i == aNodes.size()-1) {
								aNodes.add(newNode);
								break;
							}
						}
					}
				}
			}
		}
		return !aNodes.isEmpty() ? aNodes.firstElement() : null;
	}
}
