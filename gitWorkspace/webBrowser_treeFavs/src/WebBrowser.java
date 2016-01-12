
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class WebBrowser extends JFrame {

    private JMenuItem backMenuItem;
    private JMenuItem forwardMenuItem;
    private JMenuItem addMenuItem;
    private JMenuItem removeMenuItem;
    private JMenuItem exitMenuItem;
    private UrlPanel urlPanel;
    private FavPanel favPanel;
    private HtmlPanel htmlPanel;
    
    
    public WebBrowser(String title) {
        super(title);
       
        createComponents();
    }

    private void createComponents() {
    	
        addWindowListener(windowAdapter);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Commands");
        menu.setMnemonic('c');
        menuBar.add(menu);

        backMenuItem = new JMenuItem("Back", KeyEvent.VK_B);
        backMenuItem.addActionListener(actionListener);
        menu.add(backMenuItem);

        forwardMenuItem = new JMenuItem("Forward", KeyEvent.VK_F);
        forwardMenuItem.addActionListener(actionListener);
        menu.add(forwardMenuItem);

        addMenuItem = new JMenuItem("Add Favorite", KeyEvent.VK_A);
        addMenuItem.addActionListener(actionListener);
        menu.add(addMenuItem);

        removeMenuItem = new JMenuItem("Remove Favorite", KeyEvent.VK_R);
        removeMenuItem.addActionListener(actionListener);
        menu.add(removeMenuItem);

        exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(actionListener);
        menu.add(exitMenuItem);
        
        urlPanel = new UrlPanel(urlContext);
        
        favPanel = new FavPanel(favContext);
        
        htmlPanel = new HtmlPanel(htmlContext);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, favPanel, htmlPanel);
        
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(urlPanel, BorderLayout.NORTH);
        rootPanel.add(splitPane, BorderLayout.CENTER);

        this.add(rootPanel);
    }
    
    private WindowAdapter windowAdapter = new WindowAdapter() {
    	
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    };
    
    private UrlPanel.Context urlContext = new UrlPanel.Context() {
		
		@Override
		public void onUrlChanged(String url) {
			htmlPanel.setUrl(url);
		}
	};
	
	private FavPanel.Context favContext = new FavPanel.Context() {

		@Override
		public String getCurrentUrl() {
			return urlPanel.getUrl();
		}

		@Override
		public void onFavSelected(String url) {
			urlPanel.setUrl(url);
			htmlPanel.setUrl(url);		
		}
	};
	
	private HtmlPanel.Context htmlContext = new HtmlPanel.Context() {
		
		@Override
		public void onUrlSelected(String url) {
			urlPanel.setUrl(url);
		}
	};

    private ActionListener actionListener = new ActionListener() {
    	
	    public void actionPerformed(ActionEvent e) {
	    	
	        if (e.getSource() == backMenuItem) {
	        	htmlPanel.back();
	        }
	        else if (e.getSource() == forwardMenuItem) {
	        	htmlPanel.forward();
	        }
	        else if (e.getSource() == addMenuItem) {
	        	favPanel.addFav();
	        }
	        else if (e.getSource() == removeMenuItem) {
	        	favPanel.removeFav();
	        }
	        else if (e.getSource() == exitMenuItem) {
	            System.exit(0);
	        }
	    }
    };

    public static void main(String[] args) {
    	
        EventQueue.invokeLater(new Runnable()
        {
           public void run()
           {
               WebBrowser frame = new WebBrowser("Web Browser");
               frame.pack();
               frame.setVisible(true);
           }
        });
    }

}
