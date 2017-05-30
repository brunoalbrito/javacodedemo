package cn.java.demo.jpa.entity.many2one.onesided;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="cn.java.demo.jpa.entity.many2one.onesided.Cluster")
@Table(name="jpa_many2one_onesided_cluster")
public class Cluster {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cluster_id")
	private Integer clusterId;
	
	@Column(name = "cluster_name")
	private String clusterName;

	
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

	@Override
	public String toString() {
		return "Cluster [clusterId=" + clusterId + ", clusterName=" + clusterName + "]";
	}
	
}
