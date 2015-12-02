package client.views.mainWindow;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import shared.communication.ImgParams;
import client.facade.ClientFacade;

@SuppressWarnings("serial")
public class SampleImageViewer extends JOptionPane
{
	private URL 	imageUrl;
	private JPanel	rootPanel  	= new JPanel();
//	private ImageIO	imageViewer	= new ImageIO();
	private JButton closeButton = new JButton("Close");
	private DownloadBatchGUI downloadDialog;
	private ImageIcon myImage;
	
	public SampleImageViewer(String url, DownloadBatchGUI _downloadDialog)
	{
		downloadDialog = _downloadDialog;
		
		try 
		{
			imageUrl = new URL(url);
		} catch (MalformedURLException e) 
		{
			System.out.println("Failed to convert String to URL");
		}
		createComponents();
	}
	
	public void createComponents()
	{
		this.setVisible(true);
		this.setPreferredSize(new Dimension(500,500));
		
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
//		rootPanel.add(imageViewingPanel);
		rootPanel.add(closeButton);
		
		this.add(rootPanel);
		
		
		// Listeners
		closeButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
			}
		});
		
	}
	
	
}