package cn.java.demo.jpa.entity.test.many2one;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.jpa.entity.many2one.onesided.Cluster;
import cn.java.demo.jpa.entity.many2one.onesided.Node;
import cn.java.demo.jpa.util.SessionFactoryUtil;

public class Many2OneOneSidedTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		List<Node> nodeList = insert0();
		selectOne(nodeList);
		update(nodeList);
		selectOne(nodeList);
		delete(nodeList);
		selectOne(nodeList);
		insert0();
		insert0();
		selectList(nodeList);
		SessionFactoryUtil.closeSessionFactory();
	}

	private static void selectList(List<Node> nodeList) {
		System.out.println("---------selectList---------");
		Session session = null;
		try {
			session = SessionFactoryUtil.getSession();
			Query query = session.createQuery("from " + Node.class.getName() + " where id <> :id ");
			query.setParameter("id", 0);
			List<Node> nodeListTemp = (List<Node>) query.list();
			for (int i = 0; i < nodeListTemp.size(); i++) {
				Node node = nodeListTemp.get(i);
				if (node != null) {
					System.out.println(node);
					System.out.println("\t" + node.getCluster());
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * Cluster关联Node
	 * 
	 * @return
	 */
	private static List<Node> insert0() {
		System.out.println("------insert0--------");
		Session session = null;
		Transaction transaction = null;
		try {
			Cluster cluster = new Cluster();
			cluster.setClusterName("ClusterName_" + System.nanoTime());
			Node node0 = new Node();
			node0.setNodeName("NodeName_" + System.nanoTime());
			node0.setCluster(cluster); // 一个节点属于一个集群
			Node node1 = new Node();
			node1.setNodeName("NodeName_" + System.nanoTime());
			node1.setCluster(cluster);

			// 添加数据
			session = SessionFactoryUtil.getSession();
			transaction = session.beginTransaction();
			session.save(cluster);
			session.save(node0);
			session.save(node1);
			transaction.commit();

			List<Node> nodeList = new ArrayList<Node>();
			nodeList.add(node0);
			nodeList.add(node1);
			return nodeList;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param Person
	 */
	private static void update(List<Node> nodeList) {

		System.out.println("---------update---------");
		Session session = null;
		Transaction transaction = null;

		try {
			Node node = nodeList.get(0);
			if (node != null) {
				Cluster cluster = node.getCluster();
				cluster.setClusterName(cluster.getClusterName() + "_new");
				session = SessionFactoryUtil.getSession();
				transaction = session.beginTransaction();
				session.update(cluster); // 修改
				transaction.commit();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void delete(List<Node> nodeList) {
		System.out.println("---------delete---------");
		Session session = null;
		Transaction transaction = null;

		try {
			Node node = nodeList.get(0);
			if (node != null) {
				session = SessionFactoryUtil.getSession();
				

				// 删除“多”的一端
				{
					for (int i = 0; i < nodeList.size(); i++) {
						transaction = session.beginTransaction();
						Node nodeTemp = nodeList.get(i);
						session.delete(nodeTemp); // 删除
						transaction.commit();
					}
				}

				// 删除“一”的一端
				{
					transaction = session.beginTransaction();
					session.delete(node.getCluster()); // 删除
					transaction.commit();
				}

				
			}

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static void selectOne(List<Node> nodeList) {
		System.out.println("------selectOne(List<Node> nodeList)--------");
		Session session = null;
		Transaction transaction = null;
		try {
			if (nodeList != null) {
				for (Node node : nodeList) {
					session = SessionFactoryUtil.getSession();
					transaction = session.beginTransaction();
					Node nodeEntity = (Node) session.get(Node.class, node.getNodeId());
					System.out.println(nodeEntity);
					if (nodeEntity != null) {
						System.out.println("\t" + nodeEntity.getCluster());
					}
					transaction.commit();
				}
			}

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
