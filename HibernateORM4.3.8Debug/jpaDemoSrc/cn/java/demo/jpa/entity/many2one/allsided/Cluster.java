package cn.java.demo.jpa.entity.many2one.allsided;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.many2one.allsided.Cluster")
@Table(name="jpa_many2one_allsided_cluster")
public class Cluster {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cluster_id")
	private Integer clusterId;
	
	@Column(name = "cluster_name")
	private String clusterName;

	// 删除Cluster一定要删除Node，所以要配置成cascade={CascadeType.ALL}
	@OneToMany(targetEntity=Node.class,mappedBy="cluster",cascade={CascadeType.ALL})
	private Set<Node> nodes;
	
	public Integer getClusterId() {
		return clusterId;
	}

	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public String toString() {
		return "Cluster [clusterId=" + clusterId + ", clusterName=" + clusterName + "]";
	}
	
}
