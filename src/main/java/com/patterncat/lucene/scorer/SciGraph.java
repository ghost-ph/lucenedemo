package com.patterncat.lucene.scorer;

/*
 * SciGraph.java, Copyright Richard J. Davies, 1998
 * from `Introductory Java for Scientists and Engineers'
 * chapter: `JSGL, a Scientific Graphics Library for Java'
 *
 * This is part of the implementation of the library.
 *
 * The JSGL is Copyright (C) 1997-1999 Richard Davies.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of either:
 *
 *    i) the GNU Library General Public License; either version 2, or (at your
 *       option) any later version, or
 *
 *   ii) the "Artistic License".
 *
 * Copies of both licenses are enclosed with the library.
 */

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;


/**
 * <CODE>TextFrame</CODE> implements the Text window
 *
 * @author Richard J. Davies
 * @version 1.0
 */
class TextFrame extends Frame
{
    // The main area of text
    private TextArea textArea;

    // The field into which input is typed
    private TextField textField;

    // The button pressed to finish inputting text
    private Button input;

    // A button to quit the program
    private Button quit;


    // Constructor to set up the window. Doesn't show it.
    TextFrame(int width, int height)
    {
        // Create a panel that will contain the output and text entry parts.
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout(0,0));

        // Create and add a text area for output to the panel
        textArea = new TextArea("", height, width);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        textArea.setEditable(false);
        panel.add("Center", textArea);

        // Create a subpanel to go at the bottom and contain the input controls
        Panel subpanel = new Panel();
        subpanel.setLayout(new BorderLayout(0,0));

        // Create and add a text field for input into the input subpanel
        textField = new TextField();
        textField.setFont(new Font("Courier", Font.PLAIN, 12));
        textField.setEditable(false);
        subpanel.add("Center", textField);

        // Create and add a button to end input into the input subpanel
        input = new Button("Input");
        input.enable(false);
        subpanel.add("East", input);

        // Add the subpanel to the panel and the panel to the window
        panel.add("South", subpanel);
        add("Center", panel);

        // Create and add a cancel button for the window.
        quit = new Button("Quit");
        add("South", quit);

        // Set the title of the window
        setTitle("Text");

        // Set out a menu bar for the window
        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        file.add(new MenuItem("About"));
        if (!SciGraph.isApplet)
            file.add(new MenuItem("Save as..."));
        file.add(new MenuItem("Switch to Graph"));
        file.add(new MenuItem("Quit"));
        menubar.add(file);
        setMenuBar(menubar);

        // Arrange all of these components.
        pack();
    }


    // Add text to the main area
    void print(String s)
    {
        textArea.appendText(s);
    }


    // Enable the input area and button
    void startRead()
    {
        textField.setText("");
        textField.setEditable(true);
        textField.requestFocus();
        input.enable(true);
    }


    // Disable input area and button
    void endRead()
    {
        textField.setEditable(false);
        input.enable(false);
    }


    // Put up a save dialog and save the current text
    void saveAs()
    {
        // Put up the save dialog
        FileDialog fileDialog = new FileDialog(this, "Save Text",
                FileDialog.SAVE);
        fileDialog.setFile("output.txt");
        fileDialog.pack();
        fileDialog.show();

        if (fileDialog.getFile() != null)
        {
            // If they wanted to save, then try to do so
            try
            {
                PrintStream p = new PrintStream(new FileOutputStream(
                        fileDialog.getDirectory() + fileDialog.getFile()));
                p.print(textArea.getText());
                p.close();
            }
            catch (IOException e)
            {
                SciGraph.println();
                SciGraph.println("***** File could not be saved *****");
            }
        }
    }


    // Handle clicks on the buttons or menu selections
    public boolean action(Event e, Object what)
    {
        // If it's the quit button then quit
        if (e.target == quit)
        {
            SciGraph.quit();
            return true;
        }

        // If it's the input button, then finish the input session,
        // read the input and store it in SciGraph.input. Finally,
        // wake up the thread that's blocked in SciGraph.readln.
        if (e.target == input)
        {
            endRead();
            SciGraph.input = textField.getText();
            SciGraph.lock = new Object();
            return true;
        }

        // If it's a menu selection then perform appropriately
        if (e.target instanceof MenuItem)
        {
            if (((String)e.arg).equals("About"))
                SciGraph.about(this);

            else if (((String)e.arg).equals("Save as..."))
                saveAs();

            else if (((String)e.arg).equals("Switch to Graph"))
            {
                if (SciGraph.graphFrame != null)
                    SciGraph.graphFrame.toFront();
            }

            else if (((String)e.arg).equals("Quit"))
                SciGraph.quit();

            return true;
        }

        // Otherwise, we haven't handled it
        return false;
    }


    // Handle clicks in the box that closes the window
    public boolean handleEvent(Event e)
    {
        // Handle destroy events as quitting the application
        if (e.id == Event.WINDOW_DESTROY)
        {
            SciGraph.quit();
            return true;
        }

        // Allow our parent to handle all other events.
        return super.handleEvent(e);
    }
}


/**
 * <CODE>Line</CODE> is used to store a single line that has been drawn in the
 * graph area.
 *
 * @author Richard J. Davies
 * @version 1.0
 * @see GraphCanvas
 */
class Line
{
    // Storage for the data
    double xstart, ystart, xend, yend;
    Color c;

    // Constructors
    Line(double xstart, double ystart, double xend, double yend, Color c)
    {
        this.xstart = xstart;
        this.ystart = ystart;
        this.xend = xend;
        this.yend = yend;
        this.c = c;
    }

    // Overloaded versions
    Line()
    { this(0, 0, 0, 0, Color.black); }
}


/**
 * <CODE>Message</CODE> is used to store a textual message that has been put
 * in the graph area.
 *
 * @author Richard J. Davies
 * @version 1.0
 * @see GraphCanvas
 */
class Message
{
    // Storage for the data
    double x, y;
    String message;
    Color c;

    // Constructors
    Message(double x, double y, String message, Color c)
    {
        this.x = x;
        this.y = y;
        this.message = message;
        this.c = c;
    }

    // Overloaded versions
    Message()
    { this(0, 0, "", Color.black); }
}


/**
 * This class implements the <CODE>Canvas</CODE> that is scrolled around inside
 * the scrollable graph panel. It keeps a list of lines and messages drawn so
 * that it can redraw itself appropriately. It also converts between a virtual
 * coordinate system and pixels.
 *
 * @author Richard J. Davies
 * @version 1.0
 * @see GraphPanel
 */
class GraphCanvas extends Canvas
{
    // Storage for our list of lines and messages that have been drawn
    private Vector objects = new Vector(100, 100);

    // The virtual coordinate system's details
    double left, right, bottom, top;

    // The size of the real window
    int width, height;

    // The offset of the top-left if we're scrolling
    int xoffset, yoffset;

    // The window that we're embedded in
    private GraphFrame parent;


    // Constructor
    GraphCanvas(double left, double right, double bottom, double top,
                int width, int height, GraphFrame parent)
    {
        // Store away all of the information about ourself that we have been told.
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.width = width;
        this.height = height;
        this.parent = parent;

        // We start off without any scrolling
        xoffset = 0;
        yoffset = 0;
    }


    // Clear the current drawing
    void clear()
    {
        // Remove all objects from existing drawing and tell it to repaint itself
        objects.removeAllElements();
        repaint();

        // Draw the axes
        Line l;
        Message m;

        if (top * bottom < 0)
        {
            l = new Line(left, 0, right, 0, Color.black);
            addObject(l);
        }

        if (left * right < 0)
        {
            l = new Line(0, bottom, 0, top, Color.black);
            addObject(l);
        }

        // Work size of x-graduations. Aim for 2-20 graduations visible
        double xstep = 1;

        while ((right-left)/xstep > 20)
            xstep *= 10;

        while ((right-left)/xstep < 2)
            xstep /= 10;

        // Work out where to place these in y coord.
        // yg is y-coord of graduations.
        // ym is y-coord of scale text. (N.B. text drawn above and to right of pt)
        double xpixel = (right - left) / width;
        double ypixel = (top - bottom) / height;

        double yg, ym;

        if (top < 0)
        {
            yg = top;
            ym = top - 10*ypixel;
        }
        else if (bottom > 0)
        {
            yg = bottom;
            ym = bottom;
        }
        else
        {
            yg = 0;
            ym = Math.max(-10*ypixel, bottom);
        }

        // Draw the x-graduations with a scale each end.
        double x = xstep * Math.round(left/xstep + 0.5);

        m = new Message(x, ym, SciGraph.stringOf(x, 12), Color.black);
        addObject(m);

        while (x <= right)
        {
            l = new Line(x, yg - 2*ypixel, x, yg, Color.black);
            addObject(l);
            l = new Line(x, yg, x, yg + 2*ypixel, Color.black);
            addObject(l);
            x += xstep;
        }

        x -= xstep;
        m = new Message(x - 80*xpixel, ym, SciGraph.stringOf(x, 12), Color.black);
        addObject(m);

        // Work size of y-graduations. Aim for 2-20 graduations visible
        double ystep = 1;

        while ((top-bottom)/ystep > 20)
            ystep *= 10;

        while ((top-bottom)/ystep < 2)
            ystep /= 10;

        // Work out where to place these in x coord.
        // xg is x-coord of graduations.
        // xm is x-coord of scale text. (N.B. text drawn above and to right of pt)
        double xg, xm;

        if (right < 0)
        {
            xg = right;
            xm = right - 80*xpixel;
        }
        else if (left > 0)
        {
            xg = left;
            xm = left;
        }
        else
        {
            xg = 0;
            xm = Math.max(-80*xpixel, left);
        }

        // Draw the y-graduations with a message each end.
        double y = ystep * Math.round(bottom/ystep + 0.5);

        m = new Message(xm, y, SciGraph.stringOf(y, 12), Color.black);
        addObject(m);

        while (y <= top)
        {
            l = new Line(xg - 2*xpixel, y, xg, y, Color.black);
            addObject(l);
            l = new Line(xg, y, xg + 2*xpixel, y, Color.black);
            addObject(l);
            y += ystep;
        }

        y -= ystep;
        m = new Message(xm, y - 10*ypixel, SciGraph.stringOf(y, 12), Color.black);
        addObject(m);
    }


    // Actually draw an object, converting to screen coords
    void drawObject(Object o, Graphics g)
    {
        // Test if it's a line and draw it if so.
        if (o instanceof Line)
        {
            Line l = (Line)o;
            g.setColor(l.c);

            // Calculate the pixel position in doubles (N.B. 0 0 is top left).
            double dxs, dys, dxe, dye;

            dxs = width * (l.xstart - left) / (right - left);
            dys = height - height * (l.ystart - bottom) / (top - bottom);
            dxe = width * (l.xend - left) / (right - left);
            dye = height - height * (l.yend - bottom) / (top - bottom);

            // Draw the line if both ends are within bounds
            if ((dxs >= 0) && (dys >= 0) && (dxs <= width) && (dys <= height))
                if ((dxe >= 0) && (dye >= 0) && (dxe <= width) && (dye <= height))
                {
                    g.drawLine(((int)Math.round(dxs)) - xoffset,
                            ((int)Math.round(dys)) - yoffset,
                            ((int)Math.round(dxe)) - xoffset,
                            ((int)Math.round(dye)) - yoffset);
                }

            g.setColor(Color.black);
        }

        // Test if it's a message and draw it if so
        else if (o instanceof Message)
        {
            Message m = (Message)o;
            g.setColor(m.c);

            // Calculate the pixel position in doubles (N.B. 0 0 is top left).
            double dx = width * (m.x - left) / (right - left);
            double dy = height - height * (m.y - bottom) / (top - bottom);

            // Draw the message if point is within bounds
            if ((dx >= 0) && (dy >= 0) && (dx <= width) && (dy <= height))
            {
                g.drawString(m.message, ((int)Math.round(dx)) - xoffset,
                        ((int)Math.round(dy)) - yoffset);
            }

            g.setColor(Color.black);
        }
    }


    // Add an object to the current drawing
    void addObject(Object o)
    {
        // Firstly draw it
        drawObject(o, getGraphics());

        // Secondly add it to the list
        objects.addElement(o);
    }


    // Returns a line continuing from the last position in the same color.
    Line continuingLine()
    {
        Line ans = new Line();

        // Start where we last left off, or at 0,0 if there was no previous
        // line.
        try
        {
            if (objects.lastElement() instanceof Line)
            {
                Line last = (Line) objects.lastElement();

                ans.xstart = last.xend;
                ans.ystart = last.yend;
                ans.c = last.c;
            }
            else
            {
                ans.xstart = 0;
                ans.ystart = 0;
                ans.c = Color.black;
            }
        }
        catch(NoSuchElementException e)
        {
            ans.xstart = 0;
            ans.ystart = 0;
            ans.c = Color.black;
        }

        ans.xend = ans.xstart;
        ans.yend = ans.ystart;

        return ans;
    }


    // Draw the entire picture
    public void paint(Graphics g)
    {
        Object o;

        // Obtain a lock on the Vector so `clear' won't cause
        // redraw to crash part way through.
        synchronized (objects)
        {
            for (int i=0; i<objects.size(); i++)
            {
                o = objects.elementAt(i);
                drawObject(o, g);
            }
        }
    }


    // Return the entire picture as EPS.
    String getEPS()
    {
        String res = "";
        Object o;;

        // Output the EPS file headers.
        res = res.concat("%!PS-Adobe-3.0 EPSF-3.0\n");
        res = res.concat("%%BoundingBox: 0 0 " + width + " " + height + "\n");
        res = res.concat("%%EndComments\n");
        res = res.concat("%%BeginProlog\n");
        res = res.concat("%%EndProlog\n");
        res = res.concat("%%Page: 1 1\n");
        res = res.concat("\ngsave\n");
        res = res.concat("/Times-Roman findfont 12 scalefont setfont\n");

        // Obtain a lock on the Vector so `clear' won't cause
        // redraw to crash part way through.
        synchronized (objects)
        {
            for (int i=0; i<objects.size(); i++)
            {
                o = objects.elementAt(i);

                // Draw the object if it's a line.
                if (o instanceof Line)
                {
                    Line l = (Line)o;

                    // Calculate the pixel position in doubles
                    // (N.B. 0 0 is bottom left)
                    double dxs, dys, dxe, dye;

                    dxs = width * (l.xstart - left) / (right - left);
                    dys = height * (l.ystart - bottom) / (top - bottom);
                    dxe = width * (l.xend - left) / (right - left);
                    dye = height * (l.yend - bottom) / (top - bottom);

                    // Output commands to draw the line if within bounds
                    if ((dxs >= 0) && (dys >= 0)
                            && (dxs <= width) && (dys <= height))
                        if ((dxe >= 0) && (dye >= 0)
                                && (dxe <= width) && (dye <= height))
                        {
                            res = res.concat(l.c.getRed()/255.0 + " "
                                    + l.c.getGreen()/255.0 + " "
                                    + l.c.getBlue()/255.0 + " setrgbcolor ");

                            res = res.concat(dxs + " " + dys + " moveto ");
                            res = res.concat(dxe + " " + dye + " lineto stroke\n");
                        }
                }

                // Draw the object if it's a message
                else if (o instanceof Message)
                {
                    Message m = (Message)o;

                    // Calculate the pixel position in doubles
                    // (N.B. 0 0 is bottom left)
                    double dx, dy;

                    dx = width * (m.x - left) / (right - left);
                    dy = height * (m.y - bottom) / (top - bottom);

                    // Output commands to draw the text if within bounds
                    if ((dx >= 0) && (dy >= 0)
                            && (dx <= width) && (dy <= height))
                    {
                        res = res.concat(m.c.getRed()/255.0 + " "
                                + m.c.getGreen() / 255.0 + " "
                                + m.c.getBlue() / 255.0 + " setrgbcolor ");

                        res = res.concat(dx + " " + dy + " moveto ");
                        res = res.concat("(" + m.message + ") show\n");
                    }
                }
            }
        }

        // Output the EPS file footers.
        res = res.concat("grestore\n\n");
        res = res.concat("showpage\n");
        res = res.concat("%%EOF\n");

        return res;
    }


    // Update the coordinates box in the window when the mouse moves
    public boolean mouseMove(Event e, int x, int y)
    {
        // Convert into virtual coordinates
        double dx, dy;
        dx = left + (right - left) * (x + xoffset) / width;
        dy = top - (top - bottom) * (y + yoffset) / height;

        // Set the coordinates
        parent.coords.setText("x = " + SciGraph.stringOf(dx, 12)
                + ", y = " + SciGraph.stringOf(dy, 12));

        return true;
    }
}


/**
 * <CODE>GraphPanel</CODE> implements the panel containing scrollbars that lets
 * you move around the canvas on which the graph is drawn.
 *
 * @author Richard J. Davies
 * @version 1.0
 */
class GraphPanel extends Panel
{
    // The canvas embedded in us
    GraphCanvas graphCanvas;

    // Our scrollbars
    private Scrollbar hbar, vbar;


    // Constructor creates and embeds components
    GraphPanel(double left, double right, double bottom, double top,
               int width, int height, GraphFrame parent)
    {
        // Change the layout manager for this panel
        setLayout(new BorderLayout(0,0));

        // Create scrollable canvas and add it
        graphCanvas = new GraphCanvas(left, right, bottom, top,
                width, height, parent);
        add("Center", graphCanvas);

        // Create and add horizontal scrollbar
        hbar = new Scrollbar(Scrollbar.HORIZONTAL);
        add("South", hbar);

        // Create and add horizontal scrollbar
        vbar = new Scrollbar(Scrollbar.VERTICAL);
        this.add("East", vbar);
    }


    // Handle clicks on the scrollbars
    public boolean handleEvent(Event e)
    {
        int value;

        // Horizontal scrolling
        if (e.target == hbar)
        {
            switch(e.id)
            {
                case Event.SCROLL_LINE_UP:
                case Event.SCROLL_LINE_DOWN:
                case Event.SCROLL_PAGE_UP:
                case Event.SCROLL_PAGE_DOWN:
                case Event.SCROLL_ABSOLUTE:
                    // Find out where they've moved and update the offsets
                    value = ((Integer)e.arg).intValue();
                    graphCanvas.xoffset = value;

                    // Set the new scroll position
                    // this only seems necessary under JDK 1.1 for Windows.
                    hbar.setValue(value);
                    break;
            }

            // Update the canvas
            graphCanvas.update(graphCanvas.getGraphics());
            return true;
        }

        // Vertical scrolling
        if (e.target == vbar)
        {
            switch(e.id)
            {
                case Event.SCROLL_LINE_UP:
                case Event.SCROLL_LINE_DOWN:
                case Event.SCROLL_PAGE_UP:
                case Event.SCROLL_PAGE_DOWN:
                case Event.SCROLL_ABSOLUTE:
                    // Find out where they've moved and update the offsets
                    value = ((Integer)e.arg).intValue();
                    graphCanvas.yoffset = value;

                    // Set the new scroll position
                    // this only seems necessary under JDK 1.1 for Windows.
                    vbar.setValue(value);
                    break;
            }

            // Update the canvas
            graphCanvas.update(graphCanvas.getGraphics());
            return true;
        }

        return super.handleEvent(e);
    }


    // Move our scrollbars and reset their ranges when the window is resized
    public synchronized void reshape(int x, int y, int width, int height)
    {
        // Allow any superclass actions to occur
        super.reshape(x, y, width, height);

        // Find out the sizes of the scrollbars
        Dimension hbar_size = hbar.size();
        Dimension vbar_size = vbar.size();

        // Calculate how much room that leaves for the canvas
        int canvas_width = width - vbar_size.width;
        int canvas_height = height - hbar_size.height;

        // Fiddle the offset so that the bottom right of the canvas is still
        // in range
        graphCanvas.xoffset = Math.max(0, Math.min(graphCanvas.xoffset,
                graphCanvas.width - canvas_width));
        graphCanvas.yoffset = Math.max(0, Math.min(graphCanvas.yoffset,
                graphCanvas.height - canvas_height));

        // And now set the scrollbars to their new ranges
        hbar.setValues(graphCanvas.xoffset, canvas_width, 0, graphCanvas.width);
        vbar.setValues(graphCanvas.yoffset, canvas_height, 0, graphCanvas.height);
        hbar.setPageIncrement(canvas_width / 2);
        vbar.setPageIncrement(canvas_height / 2);

        // Update the canvas
        graphCanvas.repaint();
    }
}


/**
 * <CODE>GraphFrame</CODE> implements the window into which the scrollable
 * panel is placed.
 *
 * @author Richard J. Davies
 * @version 1.0
 * @see GraphPanel
 */
class GraphFrame extends Frame
{
    // The scrollable panel
    GraphPanel graphPanel;

    // A box showing the mouse position
    Label coords;

    // The quit button
    private Button quit;


    // Create the components and embed them into the frame. Don't show it.
    GraphFrame(double left, double right, double bottom, double top,
               int width, int height)
    {
        // Create the text label showing the mouse pointer's position
        coords = new Label("", Label.CENTER);
        coords.setFont(new Font("Courier", Font.PLAIN, 12));
        add("North", coords);

        // Create the scrollable canvas and accompanying scrollbars
        graphPanel = new GraphPanel(left, right, bottom, top, width, height, this);
        add("Center", graphPanel);

        // Create a quit button
        quit = new Button("Quit");
        add("South", quit);

        // Set the title for this window
        this.setTitle("Graph");

        // Create a menu bar for this window
        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        file.add(new MenuItem("About"));

        if (!SciGraph.isApplet)
            file.add(new MenuItem("Save as EPS..."));


        // Printing is only available under JDK 1.1 or later
        if (!SciGraph.isApplet)
            file.add(new MenuItem("Print..."));


        file.add(new MenuItem("Switch to Text"));
        file.add(new MenuItem("Quit"));
        menubar.add(file);
        setMenuBar(menubar);

        // Move focus off the coordinates box, set the window to a reasonable
        // initial size and and resize the window to its controls
        quit.requestFocus();
        resize(400, 400);
        this.pack();
    }


    // Put up a save dialog and save the graph as an EPS file
    void saveEPS()
    {
        // Put up the save dialog
        FileDialog fileDialog = new FileDialog(this, "Save Graph as EPS",
                FileDialog.SAVE);
        fileDialog.setFile("output.eps");
        fileDialog.pack();
        fileDialog.show();

        if (fileDialog.getFile() != null)
        {
            // If they wanted to save, then try to do so
            try
            {
                PrintStream p = new PrintStream(new FileOutputStream(
                        fileDialog.getDirectory() + fileDialog.getFile()));
                p.print(graphPanel.graphCanvas.getEPS());
                p.close();
            }
            catch (IOException e)
            {
                SciGraph.println();
                SciGraph.println("***** File could not be saved *****");
            }
        }
    }



    // Printing is only available under JDK 1.1 or later

    // Print the graph, handling all necessary dialogs
    void print()
    {
        // Put up and handle the print dialog
        PrintJob pjob = getToolkit().getPrintJob(this, "Graph", null);

        if (pjob != null)
        {
            // If told to print, then do so.
            Graphics pg = pjob.getGraphics();

            if (pg != null)
            {
                graphPanel.graphCanvas.printAll(pg);
                pg.dispose(); // flush page
            }
            pjob.end();

        }
    }



    // Handle a click on the quit button or menu selections
    public boolean action(Event e, Object what)
    {
        // Click on the quit button
        if (e.target == quit)
        {
            SciGraph.quit();
            return true;
        }

        // Menu selections
        if (e.target instanceof MenuItem)
        {
            if (((String)e.arg).equals("About"))
                SciGraph.about(this);

            else if (((String)e.arg).equals("Save as EPS..."))
                saveEPS();



            else if (((String)e.arg).equals("Print..."))
                print();


            else if (((String)e.arg).equals("Switch to Text"))
            {
                if (SciGraph.textFrame != null)
                    SciGraph.textFrame.toFront();
            }

            else if (((String)e.arg).equals("Quit"))
                SciGraph.quit();

            return true;
        }

        // Otherwise, we haven't handled it.
        return false;
    }


    // Handle clicks in the window's close box.
    public boolean handleEvent(Event e)
    {
        // We treat closing the window as quitting the program.
        if (e.id == Event.WINDOW_DESTROY)
        {
            SciGraph.quit();
            return true;
        }

        // Otherwise, we pass the event to our parent for handling.
        return super.handleEvent(e);
    }
}


/**
 * The <CODE>AboutDialog</CODE> class displays the about dialog
 *
 * @author Richard J. Davies
 * @version 1.0
 */
class AboutDialog extends Dialog
{
    // The components of the dialog
    private TextArea textArea;
    private Button ok;


    // Constructor
    AboutDialog(Frame parent)
    {
        // Call our superclass' constructor
        super(parent, "About this program", true);

        // Create an OK button in a panel of it's own, so it doesn't get really big
        ok = new Button("OK");
        Panel p = new Panel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.add(ok);

        // Add that panel to the dialog
        add("South", p);

        // Add descriptive text to the dialog.
        textArea = new TextArea("This program was written using the scientific\ngraphics library from the book\n'Introductory Java for Scientists and Engineers'\n by Richard J. Davies.\n\nThis library is copyright Richard J. Davies.", 10, 60);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        textArea.setEditable(false);
        add("Center", textArea);

        // Let the dialog set its size to that of its components
        pack();
    }


    // Catch clicks on the button to close the dialog.
    public boolean action(Event e, Object arg)
    {
        if (e.target == ok)
        {
            hide();
            dispose();
            return true;
        }
        else
            return false;
    }
}


/**
 * The <CODE>MainThread</CODE> class is used to run the main method in an
 * applet.
 *
 * @author Richard J. Davies
 * @version 1.0
 */
class MainThread extends Thread
{
    // Storage for the object owning the main method
    SciGraphBase s;


    // Constructor specifying the main method
    MainThread(SciGraphBase s)
    {
        this.s = s;
    }


    // run method to call main.
    public void run()
    {
        s.main();
    }
}


/**
 * The class <CODE>SciGraphBase</CODE> is the parent of <CODE>SciGraph</CODE>.
 * It defines those methods that are internal and not for use by the users of
 * the library. This stops them from appearing in the Javadoc documentation or
 * class definition.
 *
 * @author Richard J. Davies
 * @version 1.0
 */
abstract class SciGraphBase extends Applet
{
    // Variable stating if we are running as an applet or application
    static boolean isApplet = false;

    // Thread used to run the main method in applet mode.
    static MainThread thread;

    // References to the two windows which we create
    static TextFrame textFrame;
    static GraphFrame graphFrame;

    // A lock and variable from readln
    static Object lock = null;
    static String input = "";


    // Applet Methods

    // An abstract main method for your applet to write.
    public abstract void main();


    // Init starts a thread running to execute the main methods
    public void init()
    {
        isApplet = true;
        thread = new MainThread(this);
        thread.start();
    }


    // The web browser has finished with this page
    public void destroy()
    {
        quit();
    }


    // Draw something in the space on the browser
    public void paint(Graphics g)
    {
        g.drawString("Running...", 10, 10);
    }


    // Utility methods.

    // About displays an about dialog
    static void about(Frame parent)
    {
        AboutDialog d = new AboutDialog(parent);
        d.show();
    }

    // Quit stops the program in its tracks.
    static void quit()
    {
        // If we're an applet then kill the main thread if it's still going
        if (isApplet && thread.isAlive())
            thread.stop();

        // Shut down our windows
        if (textFrame != null)
        {
            textFrame.hide();
            textFrame.dispose();
            textFrame = null;
        }

        if (graphFrame != null)
        {
            graphFrame.hide();
            graphFrame.dispose();
            graphFrame = null;
        }

        // If we're an application then shut down
        if (!isApplet)
            System.exit(0);
    }
}


/**
 * The <CODE>SciGraph</CODE> class is the interface into the JSGL, a Scientific
 * Graphics Library for Java. This library was written for the book
 * `Introductory Java for Scientists and Engineers' by Richard J. Davies
 * and is described at much greater length in that book. The methods that you
 * should use are those declared as <CODE>public static</CODE>.
 *
 * <P>Note that this library can be used to write either applications or
 * applets. In the first case, write your application as normal using a <CODE>
 * main</CODE> method:
 *
 * <PRE>
 * public static void main(String[] argv)
 * </PRE>
 *
 * And simply call the methods of <CODE>SciGraph</CODE> as desired. In the
 * second case, you should define a class that extends <CODE>SciGraph</CODE>,
 * and write a <CODE>main</CODE> method with the prototype:
 *
 * <PRE>
 * public void main()
 * </PRE>
 *
 * Your class is then a subclass of <CODE>Applet</CODE> and should be used the
 * class that you name in the <CODE>code</CODE> part of the <CODE>Applet</CODE>
 * tag in your HTML.
 *
 * <P>You can write a program which can be run in both ways by writing:
 *
 * <PRE>
 * public class MyClass extends SciGraph
 * {
 *   public static void main(String[] argv)
 *   {
 *     ...
 *   }
 *
 *   public void main()
 *   {
 *     main(new String[1])
 *   }
 * }
 * </PRE>
 *
 * @author Richard J. Davies
 * @version 1.0
 */
public abstract class SciGraph extends SciGraphBase
{
    /**
     * <CODE>showText</CODE> creates and shows the text window in which
     * <CODE>print</CODE>, <CODE>println</CODE> and <CODE>readln</CODE> occur.
     *
     * @param width The width of the window in characters
     * @param height The height of the window in characters
     * @see SciGraph#print
     * @see SciGraph#println
     * @see SciGraph#readln
     */
    public static void showText(int width, int height)
    {
        if (textFrame == null)
            textFrame = new TextFrame(width, height);
        textFrame.show();
    }


    /**
     * An overloaded version of <CODE>showText</CODE> which defaults to a window
     * 80 characters wide and 24 characters tall.
     */
    public static void showText()
    { showText(80, 24); }


    /**
     * <CODE>print</CODE> outputs a line of text into the text window. The output
     * does not automatically move to a new line after this. This command
     * corresponds to <CODE>java.lang.System.out.print</CODE>.
     *
     * @param s The string to be written.
     * @see SciGraph#println
     */
    public static void print(String s)
    {
        textFrame.print(s);
    }

    /** A version of <CODE>print</CODE> that takes an object. */
    public static void print(Object obj) { print(String.valueOf(obj)); }

    /** A version of <CODE>print</CODE> that takes an array of characters. */
    public static void print(char[] s) { print(String.valueOf(s)); }

    /** A version of <CODE>print</CODE> that takes a character. */
    public static void print(char c) { print(String.valueOf(c)); }

    /** A version of <CODE>print</CODE> that takes an integer. */
    public static void print(int i) { print(String.valueOf(i)); }

    /** A version of <CODE>print</CODE> that takes a long integer. */
    public static void print(long l) { print(String.valueOf(l)); }

    /** A version of <CODE>print</CODE> that takes a floating point number. */
    public static void print(float f) { print(String.valueOf(f)); }

    /** A version of <CODE>print</CODE> that takes a <CODE>double</CODE>. */
    public static void print(double d) { print(String.valueOf(d)); }

    /** A version of <CODE>print</CODE> that takes a <CODE>boolean</CODE>. */
    public static void print(boolean b) { print(String.valueOf(b)); }


    /**
     * <CODE>println</CODE> outputs a line of text into the text window and
     * then moves to the start of a new line of output.
     *
     * @param s The string to be written.
     * @see SciGraph#print
     */
    public static void println(String s)
    {
        textFrame.print(s + "\n");
    }

    /** A version of <CODE>println</CODE> that just moves to a new line. */
    public static void println() { println(""); }

    /** A version of <CODE>println</CODE> that takes an object. */
    public static void println(Object obj) { println(String.valueOf(obj)); }

    /** A version of <CODE>println</CODE> that takes an array of characters. */
    public static void println(char[] s) { println(String.valueOf(s)); }

    /** A version of <CODE>println</CODE> that takes a character. */
    public static void println(char c) { println(String.valueOf(c)); }

    /** A version of <CODE>println</CODE> that takes an integer. */
    public static void println(int i) { println(String.valueOf(i)); }

    /** A version of <CODE>println</CODE> that takes a long integer. */
    public static void println(long l) { println(String.valueOf(l)); }

    /** A version of <CODE>println</CODE> that takes a floating point number. */
    public static void println(float f) { println(String.valueOf(f)); }

    /** A version of <CODE>println</CODE> that takes a <CODE>double</CODE>. */
    public static void println(double d) { println(String.valueOf(d)); }

    /** A version of <CODE>println</CODE> that takes a <CODE>boolean</CODE>. */
    public static void println(boolean b) { println(String.valueOf(b)); }


    /**
     * <CODE>stringOf</CODE> converts a floating point number into a
     * string containing a fixed number of digits.
     *
     * @param x The floating point number.
     * @param n The number of digits to return.
     * @return A string of fixed length representing the number.
     */
    public static String stringOf(double x, int n)
    {
        // Use the Standard Library routine to get a string.
        // The resulting string may be of two forms:
        // [-]ddd.dddd or [-]m.ddddE+-xxxx
        String raw = String.valueOf(x);

        // Detach everything before any E
        int epos = raw.indexOf('E');

        String start;

        if (epos == -1)
            start = raw;
        else
            start = raw.substring(0, epos);

        // Make this bit 12 characters long.
        while (start.length() < n)
            start = start + "0";

        if (start.length() > n)
            start = start.substring(0, n);

        // Add the E and anything after it.
        String ans;

        if (epos != -1)
            ans = start.concat(raw.substring(epos));
        else
            ans = start;

        return ans;
    }


    /**
     * <CODE>readln</CODE> stops execution of your program, allows the user to
     * enter some text and then returns that text as a string when they click on
     * the <CODE>Input</CODE> button in the window. Execution of your program
     * then continues.
     *
     * @return The string that the user typed.
     */
    public static String readln()
    {
        // This is complicated, since we want the user to have full access to
        // the GUI while they wonder what number to type. As a result, we block
        // the calling thread, and let the GUI threads keep running. They
        // will unblock the calling thread after storing the result in
        // SciGraph.input

        // Clear SciGraph.input
        input = "";
        lock = null;

        // Enable the editable area and its button
        textFrame.startRead();

        // Wait until the lock is altered. This is very unsatisfactory, but
        // MSIE 3.02 seems to have a bug on either wait or notify.
        while (lock == null)
        {
            try
            {
                Thread.currentThread().sleep(200);
            }
            catch(InterruptedException e)
            {
            }
        }

        // By now the result has been stored.
        // Copy it, print it into the window
        String s = new String(input);
        println(s);

        // And return it to the calling method
        return s;
    }


    /**
     * <CODE>showGraph</CODE> creates and shows a graph window. The window is
     * <CODE>width</CODE> pixels wide and <CODE>height</CODE> pixels tall on
     * screen. However, internally it uses a coordinate system that is defined
     * by <CODE>left</CODE>, <CODE>right</CODE>, <CODE>bottom</CODE> and
     * <CODE>top</CODE>.
     *
     * @param left The left hand edge of the window in internal coordinates.
     * @param right The right hand edge of the window in internal coordinates.
     * @param bottom The bottom edge of the window in internal coordinates.
     * @param top The top edge of the window in internal coordinates.
     * @param width The width of the window in pixels.
     * @param height The height of the window in pixels.
     */
    public static void showGraph(double left, double right,
                                 double bottom, double top,
                                 int width, int height)
    {
        // Kill any old window already around
        if (graphFrame != null)
        {
            graphFrame.hide();
            graphFrame.dispose();
        }

        // And create a new one.
        graphFrame = new GraphFrame(left, right, bottom, top, width, height);
        graphFrame.show();
        graphFrame.graphPanel.graphCanvas.clear();
    }

    /** An version of <CODE>showGraph</CODE> defaulting to 300 pixels each way.*/
    public static void showGraph(double left, double right,
                                 double bottom, double top)
    { showGraph(left, right, bottom, top, 300, 300); }


    /**
     * <CODE>clear</CODE> removes all previous drawing from the graph window.
     */
    public static void clear()
    {
        graphFrame.graphPanel.graphCanvas.clear();
    }


    /**
     * <CODE>point</CODE> plots a single point in the graph window.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param c The color of the point.
     */
    public static void point(double x, double y, Color c)
    {
        Line l = new Line(x, y, x, y, c);
        graphFrame.graphPanel.graphCanvas.addObject(l);
    }

    /** A version of <CODE>point</CODE> that defaults to black points. */
    public static void point(double x, double y)
    { point(x, y, Color.black); }


    /**
     * <CODE>line</CODE> draws a line in the graph window between two points.
     *
     * @param xstart The x-coordinate of the start of the line.
     * @param ystart The y-coordinate of the start of the line.
     * @param xend The x-coordinate of the end of the line.
     * @param yend The y-coordinate of the end of the line.
     * @param c The color of the line.
     */
    public static void line(double xstart, double ystart,
                            double xend, double yend, Color c)
    {
        Line l = new Line(xstart, ystart, xend, yend, c);
        graphFrame.graphPanel.graphCanvas.addObject(l);
    }


    /** A version of <CODE>line</CODE> that defaults to a black line. */
    public static void line(double xstart, double ystart,
                            double xend, double yend)
    { line(xstart, ystart, xend, yend, Color.black); }


    /**
     * A version of <CODE>line</CODE> that continues drawing from the last point
     * to be drawn in the graph.
     */
    public static void line(double xend, double yend, Color c)
    {
        Line l = graphFrame.graphPanel.graphCanvas.continuingLine();
        l.xend = xend;
        l.yend = yend;
        l.c = c;
        graphFrame.graphPanel.graphCanvas.addObject(l);
    }

    /**
     * A version of <CODE>line</CODE> that continues drawing from the last point
     * to be drawn in the graph, continuing using that same color that that point
     * was drawn in.
     */
    public static void line(double xend, double yend)
    {
        Line l = graphFrame.graphPanel.graphCanvas.continuingLine();
        l.xend = xend;
        l.yend = yend;
        graphFrame.graphPanel.graphCanvas.addObject(l);
    }


    /**
     * <CODE>functionPlot</CODE> draws a graph of a function. The function is
     * passed using a <CODE>Plottable</CODE> object, and the graph is drawn for
     * x values between <CODE>left</CODE> and <CODE>right</CODE>. The graph is
     * drawn in the color <CODE>c</CODE> with one point every <CODE>pixelgap
     * </CODE> pixels horizontally across the screen.
     *
     * @param p A <CODE>Plottable</CODE> object containing the function to draw.
     * @param left The left hand end of the range in which to draw it.
     * @param right The right hand end of the range in which to draw it.
     * @param c The color in which to draw it.
     * @param pixelgap The number of pixels horizontally across the screen
     *                  between points.
     * @see Plottable
     * @see SciGraph#parametricPlot
     * @see SciGraph#stepPlot
     * @see SciGraph#linePlot
     */
    public static void functionPlot(Plottable p, double left,
                                    double right, Color c, double pixelgap)
    {
        int steps = (int) (graphFrame.graphPanel.graphCanvas.width / pixelgap);
        double x, y;

        // Put down the left-most point
        point(left, p.f(left), c);

        for (int i=1; i <= steps; i++)
        {
            x = left + i * (right - left) / steps;
            y = p.f(x);
            line(x, y);
        }

        // Put down the right-most point
        line(right, p.f(right));
    }

    /** A version of <CODE>functionPlot</CODE> that defaults to black. */
    public static void functionPlot(Plottable p, double left, double right,
                                    double pixelgap)
    { functionPlot(p, left, right, Color.black, pixelgap); }

    /** A version of <CODE>functionPlot</CODE> that defaults to drawing the
     * graph using the whole width of the graph window. */
    public static void functionPlot(Plottable p, Color c, double pixelgap)
    { functionPlot(p, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, c, pixelgap); }

    /** A version of <CODE>functionPlot</CODE> that defaults to drawing the
     * graph in black using the whole width of the graph window. */
    public static void functionPlot(Plottable p, double pixelgap)
    { functionPlot(p, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, Color.black, pixelgap); }

    /** A version of <CODE>functionPlot</CODE> that defaults to drawing the
     * graph with one point every 10 pixels horizontally. */
    public static void functionPlot(Plottable p, double left,
                                    double right, Color c)
    { functionPlot(p, left, right, c, 10); }

    /** A version of <CODE>functionPlot</CODE> that defaults to drawing the
     * graph in black with one point every 10 pixels horizontally. */
    public static void functionPlot(Plottable p, double left, double right)
    { functionPlot(p, left, right, Color.black, 10);  }

    /** A version of <CODE>functionPlot</CODE> that defaults to drawing the
     * graph using the whole width of the graph window with one point every
     * 10 pixels horizontally. */
    public static void functionPlot(Plottable p, Color c)
    { functionPlot(p, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, c, 10); }

    /** A version of <CODE>functionPlot</CODE> that defaults to drawing the
     * graph in black using the whole width of the graph window with one point
     * every 10 pixels horizontally. */
    public static void functionPlot(Plottable p)
    { functionPlot(p, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, Color.black, 10); }


    /**
     * <CODE>parametricPlot</CODE> draws a graph in which both x and y
     * coordinates are functions of a third variable. The functions are passed
     * using <CODE>Plottable</CODE> objects, and the graph is drawn for values
     * of the third variable between <CODE>start</CODE> and <CODE>finish</CODE>.
     * The graph is drawn in the color <CODE>c</CODE> with one point every
     * <CODE>step</CODE> across the range of the third variable.
     *
     * @param x A <CODE>Plottable</CODE> object mapping the third variable to x.
     * @param y A <CODE>Plottable</CODE> object mapping the third variable to y.
     * @param start The start of the range of the third variable.
     * @param finish The end of the range of the third variable.
     * @param c The color in which to draw it.
     * @param points The number of points to calculate for the graph.
     * @see Plottable
     * @see SciGraph#functionPlot
     * @see SciGraph#stepPlot
     * @see SciGraph#linePlot
     */
    public static void parametricPlot(Plottable x, Plottable y, double start,
                                      double finish, Color c, int points)
    {
        double px, py;

        // Put down the first point
        point(x.f(start), y.f(start), c);

        for (int i=1; i <= points; i++)
        {
            px = x.f(start + i * (finish - start) / points);
            py = y.f(start + i * (finish - start) / points);
            line(px, py);
        }

        // Put down the last point
        line(x.f(finish), y.f(finish));
    }

    /** A version of <CODE>parametricPlot</CODE> that defaults to black. */
    public static void parametricPlot(Plottable x, Plottable y, double start,
                                      double finish, int points)
    { parametricPlot(x, y, start, finish, Color.black, points); }

    /** A version of <CODE>parametricPlot</CODE> that defaults a range from
     * 0 to 1. */
    public static void parametricPlot(Plottable x, Plottable y,
                                      Color c, int points)
    { parametricPlot(x, y, 0, 1, c, points); }

    /** A version of <CODE>parametricPlot</CODE> that defaults to drawing the
     * graph in black with a range from 0 to 1. */
    public static void parametricPlot(Plottable x, Plottable y, int points)
    { parametricPlot(x, y, 0, 1, Color.black, points); }

    /** A version of <CODE>parametricPlot</CODE> that defaults to drawing the
     * graph with fifty points. */
    public static void parametricPlot(Plottable x, Plottable y, double start,
                                      double finish, Color c)
    { parametricPlot(x, y, start, finish, c, 50); }

    /** A version of <CODE>parametricPlot</CODE> that defaults to drawing the
     * graph in black with fifty points. */
    public static void parametricPlot(Plottable x, Plottable y,
                                      double start, double finish)
    { parametricPlot(x, y, start, finish, Color.black, 50); }

    /** A version of <CODE>parametricPlot</CODE> that defaults to drawing the
     * graph with a range from 0 to 1 and with fifty points. */
    public static void parametricPlot(Plottable x, Plottable y, Color c)
    { parametricPlot(x, y, 0, 1, c, 50); }

    /** A version of <CODE>parametricPlot</CODE> that defaults to drawing the
     * graph in black with a range from 0 to 1 and with fifty points. */
    public static void parametricPlot(Plottable x, Plottable y)
    { parametricPlot(x, y, 0, 1, Color.black, 50); }


    /**
     * <CODE>stepPlot</CODE> draws a bar-chart based on an array of values.
     * The values in <CODE>array</CODE>are drawn with equal width bars between
     * the edge of an interval defined by <CODE>left</CODE> and
     * <CODE>right</CODE>. They are drawn in the color <CODE>c</CODE>.
     *
     * @param array The values to use in the bar chart.
     * @param left The left hand edge of the chart.
     * @param right The right hand edge of the chart.
     * @param c The color of the chart.
     * @see SciGraph#functionPlot
     * @see SciGraph#parametricPlot
     * @see SciGraph#linePlot
     */
    public static void stepPlot(double[] array, double left,
                                double right, Color c)
    {
        double x;

        // Draw the graph with one step per value
        // Plot the left-hand end point
        point(left, array[0], c);

        // Now move along, drawing each step
        for (int i=1; i<array.length; i++)
        {
            x = left + i * (right-left) / array.length;
            line(x, array[i-1]);
            line(x, array[i]);
        }

        // Finally, draw the right-hand step
        line(right, array[array.length-1]);
    }

    /** A version of <CODE>stepPlot</CODE> that defaults to black. */
    public static void stepPlot(double[] array, double left, double right)
    { stepPlot(array, left, right, Color.black); }

    /** A version of <CODE>stepPlot</CODE> that draws the bars across the
     * entire width of the graph window. */
    public static void stepPlot(double[] array, Color c)
    { stepPlot(array, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, c); }

    /** A version of <CODE>stepPlot</CODE> that draws the bars in black across
     * the entire width of the graph window. */
    public static void stepPlot(double[] array)
    { stepPlot(array, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, Color.black); }


    /**
     * <CODE>linePlot</CODE> draws a line graph from a set of values stored in
     * <CODE>array</CODE>. The points of the graph are evenly distributed across
     * the interval between <CODE>left</CODE> and <CODE>right</CODE> and the
     * lines between them are drawn in the color <CODE>c</CODE>.
     *
     * @param array The values for the point on the graph.
     * @param left The left hand edge of the graph.
     * @param right The right hand edge of the graph.
     * @param c The color of the graph.
     * @see SciGraph#functionPlot
     * @see SciGraph#parametricPlot
     * @see SciGraph#stepPlot
     */
    public static void linePlot(double[] array, double left,
                                double right, Color c)
    {
        double x;

        // Plot the left-hand end point
        point(left, array[0], c);

        // Now move along, drawing each step
        for (int i=1; i<array.length; i++)
        {
            x = left + i * (right-left) / (array.length-1);
            line(x, array[i]);
        }
    }

    /** A version of <CODE>linePlot</CODE> that defaults to black. */
    public static void linePlot(double[] array, double left, double right)
    { linePlot(array, left, right, Color.black); }

    /** A version of <CODE>linePlot</CODE> that draws the graph across the entire
     * width of the graph window. */
    public static void linePlot(double[] array, Color c)
    { linePlot(array, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, c); }

    /** A version of <CODE>linePlot</CODE> that draws the graph across the entire
     * width of the graph window, using black. */
    public static void linePlot(double[] array)
    { linePlot(array, graphFrame.graphPanel.graphCanvas.left,
            graphFrame.graphPanel.graphCanvas.right, Color.black); }
}
