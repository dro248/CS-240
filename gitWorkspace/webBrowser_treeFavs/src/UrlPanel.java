
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class UrlPanel extends JPanel {

	public interface Context {
		
		public void onUrlChanged(String url);
	}

	
	private Context context;
	private JTextField urlField;
	
	public UrlPanel(Context c) {
		
		super();
		
		context = c;
		
        JLabel urlLabel = new JLabel("URL:");
        
        urlField = new JTextField(50);
        urlField.setOpaque(true);
        urlField.setBackground(Color.white);
        urlField.setPreferredSize(new Dimension(750, 30));
        urlField.addActionListener(actionListener);
        
        this.setLayout(new BorderLayout());
        this.add(urlLabel, BorderLayout.WEST);
        this.add(urlField, BorderLayout.CENTER);
	}
	
	public void setUrl(String url) {
		urlField.setText(url);
	}
	
	public String getUrl() {
		return urlField.getText();
	}
	
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (context != null) {
				context.onUrlChanged(getUrl());
			}
		}   	
    };
	
}
