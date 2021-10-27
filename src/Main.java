import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JPanel {

    private int maxLoops = 40;
    private double xMin = -2, xMax = 1.5, yMin = -1.5, yMax = 1.5;

    public Main(int w, int h){
        setSize(w, h);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                //zoom in
                double currentWidth = xMax - xMin;
                double currentHeight = yMax - yMin;

                int x = e.getX();
                double a = x * (currentWidth / getWidth()) + xMin;
                int y = e.getY();
                double b = (-y + getHeight()) * (currentHeight / getHeight()) + yMin;
                xMin = a - currentWidth/4;
                xMax = a + currentWidth/4;
                yMin = b - currentHeight/4;
                yMax = b + currentHeight/4;

                maxLoops *= 1.15;

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        double da = (xMax - xMin)/getWidth();
        double db = (yMax - yMin)/getHeight();
        for (double a = xMin; a < xMax; a += da) {
            for (double b = yMin; b < yMax; b += db) {
                int count = testPoint(a,b);
                if(count == maxLoops){
                    g2.setColor(Color.BLACK);
                    plotPoint(a,b,g2);
                }
                else {
                    g2.setColor(Color.getHSBColor(1f*count/maxLoops, 1-1f*count/maxLoops, 1f));
                    plotPoint(a,b,g2);
                }
            }
        }
    }

    public int testPoint(double a, double b) {
        double an = a;
        double bn = b;
        int counter = 0;
        while (counter < maxLoops && an*an + bn*bn < 4) {
            double nextA = an*an - bn*bn + a;
            double nextB = (2*an*bn) + b;
            an = nextA;
            bn = nextB;
            counter++;
        }
        return counter;
    }

    public void plotPoint(double a, double b, Graphics2D g2) {
        double x = (a - xMin)*(getWidth()/(xMax - xMin));
        double y = getHeight() - (b - yMin)*(getHeight()/(yMax - yMin));
        g2.drawLine((int)x, (int)y, (int)x+1, (int)y+1);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int width = 933;
        int height = 800;
        window.setBounds(0, 0, width, height + 22); //(x, y, w, h) 22 due to title bar.

        Main panel = new Main(width, height);
        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(false);
    }
}