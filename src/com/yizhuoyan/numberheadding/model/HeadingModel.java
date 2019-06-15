package com.yizhuoyan.numberheadding.model;

import java.util.ArrayList;
import java.util.List;

public class HeadingModel {
	private static final char SEQ_MARK_CHAR='、';
	public static HeadingModel root() {
		 HeadingModel root=new HeadingModel();
		 root.childOrder=0;
		 root.level=0;
		 root.name=null;
		 root.parent=null;
		 return root;
	}
	private HeadingModel parent;
	private int level;
	private String name;
	private int childOrder;
	private List<HeadingModel> children;
	
	public List<HeadingModel> getChildren() {
		return children;
	}

	
	public HeadingModel getParent() {
		return parent;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public int getChildOrder() {
		return childOrder;
	}

	

	public static HeadingModel parseHeading(String heading,HeadingModel root) {
		int level=parseLevel(heading);
		String name=heading.substring(level).trim();
		HeadingModel hm=new HeadingModel();
		hm.level=level;
		hm.name=name;
		HeadingModel parent=findCurrentParent(level, root);
		parent.addChild(hm);
		return hm;
	}
	
	private static HeadingModel findCurrentParent(int level,HeadingModel root) {
		HeadingModel parent=root;
		while(--level>0&&parent!=null) {
			parent=parent.getLastChild();
		}
		if(parent==null) {
			return findCurrentParent(level-1, root);
		}
		return parent;
	}
	private HeadingModel() {
		
	}

	
	private void addChild(HeadingModel child) {
		if(this.children==null) {
			this.children=new ArrayList<HeadingModel>();
		}
		if(child.parent!=this) {
			child.parent=this;
			child.childOrder=this.children.size()+1;
		}
		
		this.children.add(child);
	}
	
	public HeadingModel getLastChild() {
		if(this.children==null) {
			return null;
		}
		return this.children.get(this.children.size()-1);
	}
	

	private static int parseLevel(String heading) {
		int level=0;
		while(level<heading.length()&&heading.charAt(level)=='#') {
			level++;
		}
		return level;
	}
	
	public String process() {
		StringBuilder result=new StringBuilder();
	
		result.append("#".repeat(this.level));
		result.append(" ");
		String name=this.name;
		int markEndIndex=name.indexOf(SEQ_MARK_CHAR);
		if(markEndIndex!=-1) {
			name=name.substring(markEndIndex+1);
		}
		result.append(this.calcNumberMark());
		
		result.append(name);
		return result.toString();
	}
	private String calcNumberMark() {
		
		String levelChar=SequenceMarkManager.getLevelMark(this.level,this.childOrder);
		if(levelChar==null) {
			return "";
		}
		StringBuilder result=new StringBuilder();
		result.append(levelChar);
		result.append(SEQ_MARK_CHAR);
		return result.toString();
	}
	
}
