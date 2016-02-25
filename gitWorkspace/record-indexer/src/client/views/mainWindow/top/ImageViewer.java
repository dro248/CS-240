package client.views.mainWindow.top;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.*;

import java.util.*;
import java.io.*;
import java.net.URL;

import shared.model.Field;
import client.facade.*;
import client.views.mainWindow.Indexer;
import client.views.mainWindow.bottom.left.form.DataForm;
import client.views.mainWindow.bottom.left.table.DataTable;
import client.views.mainWindow.bottom.right.FieldHelpPanel;
import client.views.mainWindow.bottom.right.ImageNavigationPanel;


@SuppressWarnings("serial")
public class ImageViewer extends JComponent implements BatchStateListener
{
	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	private int w_translateX;
	private int w_translateY;
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;
	private ArrayList<DrawingShape> shapes;
	private Image batchImage;
	private Image invertedImage;
	private DrawingImage regularDrawingImage;
	private DrawingImage invertedDrawingImage;
	private DrawingImage currentDrawingImage;	// we'll use this as a placeholder
	
	private Rectangle2D myRect;
	private double rect_Width;
	private double rect_Height;
	private double rect_X;
	private double rect_Y;
	
	private BatchState 	batchState;
	public ImageViewer(URL imageUrl)
	{		
		// Get batchState reference from ClientFacade
		batchState = ClientFacade.get().getBatchState();
		batchState.addListener(this);
		if(imageUrl == null && batchState.getURL() != null)
		{
			imageUrl = batchState.getURL();
		}
		
		w_translateX = 0;
		w_translateY = 0;
		scale = .50;
		
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		this.setPreferredSize(new Dimension(700, 700));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		
		
		if(imageUrl != null)
		{
			batchImage = loadImage(imageUrl.toString());
			regularDrawingImage = new DrawingImage(batchImage, new Rectangle2D.Double(0, 0, batchImage.getWidth(null), batchImage.getHeight(null)));
			currentDrawingImage = regularDrawingImage;
			shapes.add(currentDrawingImage);
			
			// Initialize rectangle w/ values
			rect_X 		= 0;
			rect_Y 		= 0;
			rect_Width 	= 0;
			rect_Height = 0;
			myRect = new Rectangle2D.Double(rect_X, rect_Y, rect_Width, rect_Height);
			
			RescaleOp op = new RescaleOp(-1.0f, 255f, null);
			invertedImage = op.filter((BufferedImage) batchImage, null);
			invertedDrawingImage = new DrawingImage(invertedImage, new Rectangle2D.Double(0, 0, invertedImage.getWidth(null), invertedImage.getHeight(null)));
		}
	}
	
	
	
	
	public void setRectParams(double width, double height, double x_pos, double y_pos)
	{
		rect_Width = width;
		rect_Height = height;
		rect_X = x_pos;
		rect_Y = y_pos;
		
		myRect = new Rectangle2D.Double(rect_X, rect_Y, rect_Width, rect_Height);
		
		repaint();
		revalidate();
	}
	
	public void invertImage()
	{

		if(currentDrawingImage == regularDrawingImage)
		{
			currentDrawingImage = invertedDrawingImage;
			shapes.clear();
			shapes.add(currentDrawingImage);
			repaint();
			
			batchState.setImageInverted(true);
		}
		else if(currentDrawingImage == invertedDrawingImage)
		{
			currentDrawingImage = regularDrawingImage;
			shapes.clear();
			shapes.add(currentDrawingImage);
			repaint();
			
			batchState.setImageInverted(false);
		}
	}
	
	private void initDrag() 
	{
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}
	
	private Image loadImage(String imageFile) 
	{
		try 					{ return ImageIO.read(new URL(imageFile)); }
		catch (IOException e) 	{ return NULL_IMAGE; }
	}
	
	public void setScale(double newScale) 
	{
		scale = newScale;
		this.repaint();
	}
	
	@Override
	public void translate(int w_newTranslateX, int w_newTranslateY) 
	{
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
	
		// Draw only background if batchimage is null
		if(batchImage == null)
		{
			return;
		}
		
		g2.translate(getWidth()/2, getHeight()/2);
		g2.scale(scale, scale);
		g2.translate(-batchImage.getWidth(null)/2.0, -batchImage.getHeight(null)/2.0);
		g2.translate(w_translateX, w_translateY);
		
		drawShapes(g2);
		if(myRect != null)
		{
			g2.setColor(new Color(210, 191, 255, 192));
//			g2.fill(myRect);
			g2.fillRect((int)rect_X, (int)rect_Y, (int)rect_Width, (int)rect_Height);
		}
	}
	
	public void toggleRectColor()
	{
		if(myRect.getWidth() > 0)
		{
			myRect = new Rectangle2D.Double(0, 0, 0, 0);
		}
		else
		{
			myRect.setRect(rect_X, rect_Y, rect_Width, rect_Height);
		}
		
		repaint();
		revalidate();
	}
	
	private void drawBackground(Graphics2D g2) 
	{
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) 
	{
		for (DrawingShape shape : shapes) 
		{
			shape.draw(g2);
		}
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() 
	{
		@Override
		public void mousePressed(MouseEvent e) 
		{
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(-batchImage.getWidth(null)/2.0, -batchImage.getHeight(null)/2.0);
			transform.translate(w_translateX, w_translateY);
			
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			
			try 										{ transform.inverseTransform(d_Pt, w_Pt); }
			catch (NoninvertibleTransformException ex) 	{ return; }
			
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) 
			{
				if (shape.contains(g2, w_X, w_Y)) 
				{
					break;
				}
			}
			
//			if (hitShape) 
			{
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(-batchImage.getWidth(null)/2.0, -batchImage.getHeight(null)/2.0);
			transform.translate(w_translateX, w_translateY);
			
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			
			try 										{ transform.inverseTransform(d_Pt, w_Pt); }
			catch (NoninvertibleTransformException ex) 	{ return; }
			
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			hitShape(w_X, w_Y);
			
		}
		
		private void hitShape(int x, int y)
		{			
			int top = batchState.getProject().getFirstYCoordinate();
			int bottom = top + (batchState.getProject().getRecordHeight() * batchState.getProject().getRecordsPerImage());
			int left = batchState.getFields().get(0).getXCoordinate();
			int right = left;
			
			// get 'right'
			for(Field f : batchState.getFields())
			{
				right += f.getWidth();
			}
				
			// Go through all fields.. test if x & y are within cells section
			if(x > left && x < right && y > top	&& y < bottom)
			{				
				// update batchState selectedCellChanged
				batchState.selectedCellChanged(calcRow(y), calcCol(x));
			}
			
		}
		
		private int calcRow(int mouse_y)
		{
			int top = batchState.getProject().getFirstYCoordinate();
			int height = batchState.getProject().getRecordHeight();
			
			for(int r = 0; r < batchState.getProject().getRecordsPerImage(); r++)
			{
				if(mouse_y < (top+(r+1)*height))
				{
					return r;
				}
			}
			// This should never happen
			return 0;
		}
		
		private int calcCol(int mouse_x)
		{
			for(int c = 0; c < batchState.getFields().size(); c++)
			{
				int width = batchState.getFields().get(c).getWidth();
				int column_left = batchState.getFields().get(c).getXCoordinate();
				
				if(mouse_x < column_left + width)
				{
					return c;
				}
			}
			// This should never happen
			return 0;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) 
		{		
			if (dragging) 
			{
				int d_X = e.getX();
				int d_Y = e.getY();
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				
				try 										{ dragTransform.inverseTransform(d_Pt, w_Pt); }
				catch (NoninvertibleTransformException ex) 	{ return; }
				
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
//				w_translateX = w_dragStartTranslateX + w_deltaX;
//				w_translateY = w_dragStartTranslateY + w_deltaY;
				
				int w_translateX = w_dragStartTranslateX + w_deltaX;
				int w_translateY = w_dragStartTranslateY + w_deltaY;
				batchState.translate(w_translateX, w_translateY);
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) { initDrag(); }

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) 
		{
			if(e.getWheelRotation() < 0)
			{
				// Zoom-In
				if(scale > 5)
				{
					return;
				}
				scale *= 1.25;
				repaint();
			}
			else if(e.getWheelRotation() > 0)
			{
				if(scale < .2)
				{
					return;
				}
				// Zoom-Out
				scale/=1.25;
				repaint();
			}
		}
	};
	
	public void scaleImage(double multiplier)
	{
		if(scale < .2 && multiplier < 1)
		{
			return;
		}
		else if(scale > 5 && multiplier > 1)
		{
			return;
		}
		
		scale *= multiplier;
		repaint();
		revalidate();
	}
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() 
	{
		@Override
		public void componentHidden(ComponentEvent e) { return; }

		@Override
		public void componentMoved(ComponentEvent e) { return; }

		/*@Override
		public void componentResized(ComponentEvent e) { 
			updateTextShapes();
		}*/

		@Override
		public void componentShown(ComponentEvent e) { return; }	
	};

	
	/////////////////
	// Drawing Shape
	/////////////////
	
	
	interface DrawingShape 
	{
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
	}


	class DrawingRect implements DrawingShape 
	{
		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) 
		{
			this.rect = rect;
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) 
		{
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) 
		{
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) 
		{
			return rect.getBounds2D();
		}
	}

	class DrawingImage implements DrawingShape 
	{
		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) 
		{
			this.image = image;
			this.rect = rect;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) { return rect.contains(x, y); }

		@Override
		public void draw(Graphics2D g2) 
		{
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) { return rect.getBounds2D(); }
	}

	
	
	
	//////////////////////////////////////
	//							 		//
	//		BatchStateListener code		//
	//									//
	//////////////////////////////////////
	
	@Override
	public void valueChanged(int row, int column, String newValue) {}
	
	@Override
	public void selectedCellChanged(int row, int column)
	{
		if(column < 0)
		{
			return;
		}
				
		// get Rectangle coordinates
		int top  = batchState.getProject().getFirstYCoordinate();
		int left = batchState.getFields().get(column).getXCoordinate();
		
		// get Rectangle width
		int width = batchState.getFields().get(column).getWidth();
		int height = batchState.getProject().getRecordHeight();
		
		// adjust top
		top = top + height*row;
		
		// set globals
		rect_Width = width;
		rect_Height = height;
		rect_X = left;
		rect_Y = top;
		
		setRectParams(width, height, left, top);	
	}
	
	@Override
	public Coordinate getWindowPosition()			{ return null; 	}
	@Override
	public Dimension getWindowSize()				{ return null; 	}
	@Override
	public int getVPaneDivPosition()				{ return 0; 	}
	@Override
	public int getHPaneDivPosition()				{ return 0;		}
	@Override
	public void setButtonAvailability() 			{}
	@Override
	public DataTable getDataTable()					{ return null; 	}
	@Override
	public DataForm getDataForm()					{ return null; 	}
	@Override
	public FieldHelpPanel getFieldHelp()			{ return null; 	}
	@Override
	public ImageNavigationPanel getImageNavigation(){ return null; 	}
	@Override
	public double getZoom()
	{
		return scale;
	}





	@Override
	public void setScale(Double scale) {	
	}
}
