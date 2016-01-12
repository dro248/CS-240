
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class FavDialog extends JDialog {

	private JTextField titleTextField;
	private JButton okButton;
	private JButton cancelButton;
	private boolean status;
	
	public FavDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.setTitle("Add Favorite");
		
		// Title panel
		
		JLabel titleLabel = new JLabel("Title for this favorite");
		Font defaultFont = titleLabel.getFont();
		Font font = new Font(defaultFont.getName(), defaultFont.getStyle(), 14);
		titleLabel.setFont(font);
		
		titleTextField = new JTextField();
		titleTextField.setPreferredSize(new Dimension(400, 30));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		
		titlePanel.add(titleLabel);
		titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
		titlePanel.add(titleTextField);
		
		// Button panel
		
		okButton = new JButton("OK");
		okButton.addActionListener(actionListener);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(actionListener);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPanel.add(cancelButton);
		
		// Root panel
		
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		
		rootPanel.add(titlePanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		rootPanel.add(buttonPanel);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(rootPanel);
		
		this.pack();
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public String getTitle() {
		return titleTextField.getText();
	}
	
	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				status = true;
			}
			FavDialog.this.setVisible(false);
		}
	};

}
