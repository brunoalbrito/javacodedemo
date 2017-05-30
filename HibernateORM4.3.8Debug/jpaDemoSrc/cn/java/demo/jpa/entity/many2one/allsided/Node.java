package cn.java.demo.jpa.entity.many2one.allsided;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity(name="cn.java.demo.jpa.entity.many2one.allsided.Node")
@Table(name="jpa_many2one_allsided_node")
public class Node {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="node_id",nullable=false,unique=true)
	private Integer nodeId;
	
	@Column(name="node_name",length=30,nullable=false,unique=true)
	private String nodeName;
	
	// 删除Node不一定要删除Cluster，所以不要配置成cascade={CascadeType.ALL}
	@ManyToOne(targetEntity=Cluster.class,cascade={})
	@JoinColumn(name="cluster_id")
	private Cluster cluster;

	
	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	@Override
	public String toString() {
		return "Node [nodeId=" + nodeId + ", nodeName=" + nodeName + "]";
	}
	
	
	
}
