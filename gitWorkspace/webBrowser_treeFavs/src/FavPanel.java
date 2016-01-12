
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;


@SuppressWarnings("serial")
public class FavPanel extends JPanel {

	public interface Context {

		public String getCurrentUrl();

		public void onFavSelected(String url);
	}

	private Context context;
	private JButton addButton;
	private JButton removeButton;
	private JTree favTree;
	private DefaultTreeModel favTreeModel;
	private JPopupMenu favPopupMenu;
	private JMenuItem favAddMenuItem;
	private JMenuItem favRemoveMenuItem;

	public FavPanel(Context c) {

		super();

		context = c;

		addButton = new JButton("Add");
		addButton.addActionListener(actionListener);

		removeButton = new JButton("Remove");
		removeButton.addActionListener(actionListener);

		JToolBar favToolBar = new JToolBar();
		favToolBar.add(addButton);
		favToolBar.add(removeButton);

		favTreeModel = new DefaultTreeModel(createDefaultFavs());

		favTree = new JTree(favTreeModel);
		favTree.setOpaque(true);
		favTree.setBackground(Color.white);
		favTree.setPreferredSize(new Dimension(250, 600));
		favTree.setEditable(false);
		favTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		favTree.setShowsRootHandles(false);
		favTree.addTreeSelectionListener(treeSelListener);
		favTree.addMouseListener(mouseAdapter);

		JScrollPane favScrollPane = new JScrollPane(favTree);
		favScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		favScrollPane.setPreferredSize(new Dimension(250, 600));

		favAddMenuItem = new JMenuItem("Add");
		favAddMenuItem.addActionListener(actionListener);

		favRemoveMenuItem = new JMenuItem("Remove");
		favRemoveMenuItem.addActionListener(actionListener);

		favPopupMenu = new JPopupMenu();
		favPopupMenu.add(favAddMenuItem);
		favPopupMenu.add(favRemoveMenuItem);

		this.setLayout(new BorderLayout());
		this.add(favToolBar, BorderLayout.NORTH);
		this.add(favScrollPane, BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode createDefaultFavs() {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Favorites");

		DefaultMutableTreeNode byu = new DefaultMutableTreeNode("BYU");
		byu.add(new DefaultMutableTreeNode(new FavInfo("Computer Science",
				"http://www.cs.byu.edu")));

		DefaultMutableTreeNode news = new DefaultMutableTreeNode("News");
		news.add(new DefaultMutableTreeNode(new FavInfo("CNN",
				"http://www.cnn.com")));

		DefaultMutableTreeNode sports = new DefaultMutableTreeNode("Sports");
		sports.add(new DefaultMutableTreeNode(new FavInfo("ESPN",
				"http://www.espn.com")));

		DefaultMutableTreeNode weather = new DefaultMutableTreeNode("Weather");
		weather.add(new DefaultMutableTreeNode(new FavInfo("Weather Channel",
				"http://www.weather.com")));

		root.add(byu);
		root.add(news);
		root.add(sports);
		root.add(weather);

		return root;
	}

	private ActionListener actionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == addButton || e.getSource() == favAddMenuItem) {
				addFav();
			} else if (e.getSource() == removeButton
					|| e.getSource() == favRemoveMenuItem) {
				removeFav();
			}
		}
	};

	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseReleased(MouseEvent e) {

			if (e.isPopupTrigger()) {
				if (e.getSource() == favTree) {
					favPopupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	};

	private TreeSelectionListener treeSelListener = new TreeSelectionListener() {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree
					.getLastSelectedPathComponent();
			if (node != null) {
				if (node.isLeaf()) {
					FavInfo favInfo = (FavInfo) node.getUserObject();
					if (context != null) {
						context.onFavSelected(favInfo.getUrl());
					}
				}
			}
		}
	};

	public void addFav() {

		System.out.println("add favorite");

		if (context.getCurrentUrl().equals("")) {
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
					"You must first load a web page", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree
					.getLastSelectedPathComponent();
			if (node == null || node.isLeaf()) {
				JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
						"Select a folder before adding a favorite", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				FavDialog favDialog = new FavDialog(
						(JFrame) this.getTopLevelAncestor(), true);
				favDialog.setLocationRelativeTo(this.getTopLevelAncestor());
				favDialog.setVisible(true);

				if (favDialog.getStatus()) {
					String title = favDialog.getTitle();
					String curURL = context.getCurrentUrl();
					favTreeModel.insertNodeInto(new DefaultMutableTreeNode(
							new FavInfo(title, curURL)), node, node
							.getChildCount());
				}
			}
		}
	}

	public void removeFav() {

		System.out.println("remove favorite");

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree
				.getLastSelectedPathComponent();
		if (node == null || !node.isLeaf()) {
			JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
					"Select a favorite to remove", "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			favTreeModel.removeNodeFromParent(node);
		}
	}

}
