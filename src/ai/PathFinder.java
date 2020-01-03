package ai;

import java.util.Vector;

import ai.Node.PathDirection;
import data.Map;
import data.Move;
import data.Position;

public abstract class PathFinder {
	public static final Vector<Move> Calculate(Map aMap, Position aStartPos, Position aGoalPos) {
		Node baseNode = new Node(aMap, aStartPos, aGoalPos);
		Node finalNode = ProcessNodes(baseNode);

		return finalNode != null ? finalNode.GetMoveList() : null;
	}

	private static final Node ProcessNodes(Node aNode) {
		int ticks = 0;
		Vector<Node> nodes = new Vector<Node>();
		nodes.add(aNode);

		while(!nodes.isEmpty() && !nodes.firstElement().IsAtGoal() && ticks++ < 100000) {
			Node baseNode = nodes.firstElement();
			nodes.remove(nodes.firstElement());
			for (PathDirection direction : PathDirection.values()) {
				Node newNode = new Node(baseNode, direction);
				if(!newNode.GetPosition().Compare(baseNode.GetPosition())) {
					if(nodes.isEmpty()) {
						nodes.add(newNode);
					}
					for(int i=0; i<nodes.size(); i++) {
						if(newNode.GetTotalCost() <= nodes.elementAt(i).GetTotalCost() || i == nodes.size()-1) {
							nodes.add(newNode);
							break;
						}
					}
				}
			}
		}

		return !nodes.isEmpty() ? nodes.firstElement() : null;
	}
}
