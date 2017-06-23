package cn.java.demo.jpa.entity.test.many2one;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.java.demo.jpa.entity.many2one.allsided.Cluster;
import cn.java.demo.jpa.entity.many2one.allsided.Node;
import cn.java.demo.jpa.util.SessionFactoryUtil;

public class Many2OneAllSidedTest {

	public static void main(String[] args) {
		SessionFactoryUtil.openSessionFactory();
		ClusterCtrlTest.main();
		NodeCtrlTest.main();
		SessionFactoryUtil.closeSessionFactory();
	}

	public static class ClusterCtrlTest {

		public static void main() {
			System.out.println("---------ClusterCtrlTest----------");
			Cluster cluster = insert0();
			selectOne(cluster);
			update(cluster);
			selectOne(cluster);
			delete(cluster);
			selectOne(cluster);

			{
				Cluster cluster0 = insert0();
				Cluster cluster1 = insert0();
				selectList(cluster);
				delete(cluster0);
				delete(cluster1);
			}
		}

		private static void selectList(Cluster cluster) {
			System.out.println("---------selectList---------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
				Query query = session.createQuery("from " + Cluster.class.getName() + " where id <> :id ");
				query.setParameter("id", 0);
				List<Cluster> clusterList = (List<Cluster>) query.list();
				for (int i = 0; i < clusterList.size(); i++) {
					Cluster clusterEntity = clusterList.get(i);
					System.out.println(clusterEntity);
					if (clusterEntity != null) {
						System.out.println("\t" + clusterEntity.getNodes());
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
		private static Cluster insert0() {
			System.out.println("------insert0--------");
			Session session = null;
			Transaction transaction = null;
			try {
				Node node0 = new Node();
				node0.setNodeName("NodeName_" + System.nanoTime());
				Node node1 = new Node();
				node1.setNodeName("NodeName_" + System.nanoTime());
				Set<Node> nodeSet = new HashSet<Node>();
				nodeSet.add(node0);
				nodeSet.add(node1);
				Cluster cluster = new Cluster();
				cluster.setClusterName("ClusterName_" + System.nanoTime());
				cluster.setNodes(nodeSet);
				node0.setCluster(cluster);
				node1.setCluster(cluster);

				// 添加数据
				session = SessionFactoryUtil.openSession();
				transaction = session.beginTransaction();
				session.save(cluster);
				session.save(node0);
				session.save(node1);
				transaction.commit();

				return cluster;
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		/**
		 * @param Person
		 */
		private static void update(Cluster cluster) {
			System.out.println("---------update---------");
			Session session = null;
			Transaction transaction = null;
			try {
				cluster.setClusterName(cluster.getClusterName() + "_new");
				session = SessionFactoryUtil.openSession();
				transaction = session.beginTransaction();
				session.update(cluster); // 修改
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void delete(Cluster cluster) {
			System.out.println("---------delete(Cluster cluster)---------");
			Session session = null;
			Transaction transaction = null;
			try {
				session = SessionFactoryUtil.openSession();
				transaction = session.beginTransaction();
				session.delete(cluster); // 删除 - 会级联删除
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		private static void selectOne(Cluster cluster) {
			System.out.println("------selectOne(Cluster cluster)--------");
			Session session = null;
			Transaction transaction = null;
			try {
				session = SessionFactoryUtil.openSession();
				transaction = session.beginTransaction();
				Cluster clusterEntity = (Cluster) session.get(Cluster.class, cluster.getClusterId());
				System.out.println(clusterEntity);
				if (clusterEntity != null) {
					System.out.println("\t" + clusterEntity.getNodes());
				}
				transaction.commit();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
	}

	public static class NodeCtrlTest {

		public static void main() {
			System.out.println("---------NodeCtrlTest----------");
			List<Node> nodeList = insert0();
			selectOne(nodeList);
			update(nodeList);
			selectOne(nodeList);
			delete(nodeList);
			selectOne(nodeList);

			{
				List<Node> nodeList0 = insert0();
				List<Node> nodeList1 = insert0();
				selectList(nodeList0);
				delete(nodeList0);
				delete(nodeList1);
			}

		}

		private static void selectList(List<Node> nodeList) {
			System.out.println("---------selectList---------");
			Session session = null;
			try {
				session = SessionFactoryUtil.openSession();
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
				session = SessionFactoryUtil.openSession();
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
					session = SessionFactoryUtil.openSession();
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
			System.out.println("---------delete(List<Node> nodeList)---------");
			Session session = null;
			Transaction transaction = null;

			try {
				if (nodeList != null) {
					session = SessionFactoryUtil.openSession();
					

					// 删除“多”的一端
					{
						for (Node node : nodeList) {
							transaction = session.beginTransaction();
							session.delete(node); // 删除
							transaction.commit();
						}
					}

					// 删除“一”的一端
					{
						transaction = session.beginTransaction();
						session.delete(nodeList.get(0).getCluster()); // 删除
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
						session = SessionFactoryUtil.openSession();
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

}
