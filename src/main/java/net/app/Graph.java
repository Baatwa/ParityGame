package net.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
	private List<Node> nodes;
	private Set<Link> links;
	private List<Region> regions;
	
	public Graph() {
		nodes = new ArrayList<Node>();
		links = new HashSet<Link>();
		regions = new ArrayList<Region>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	public void addLink(Node start, Node end) {
		Link result = new Link(start, end);
		Link twin = new Link(end, start);
		
		if(links.contains(result)) {
			return;
		}
		
		if(links.contains(twin)) {
			result.setHasTwin(true);
			for(Link l:start.getInputs()) {
				if(l.equals(twin)) {
					l.setHasTwin(true);
				}
			}
		}
		
		links.add(result);
		start.getOutputs().add(result);
		end.getInputs().add(result);
	}
	
	public void addRegion(Region region) {
		regions.add(region);
	}
	
	public void render(Renderer renderer, Node selected) {
		for(Region region:regions) {
			region.render(renderer);
		}
		for(Link link:links) {
			link.render(renderer);
		}
		for(Node node:nodes) {
			node.render(renderer, selected);
		}
	}
	
}
