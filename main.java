import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

public class main extends JPanel {
    public static int XCoordinate = -1;
    public static int YCoordinate = -1;

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        // frame.addMouseListener(new MouseAdapter() {
        //     public void mouseClicked(MouseEvent e) {
        //         referencePoints(frame);
        //         double clickedCol = -1;
        //         double clickedRow = -1;

        //         if (e.getY() > YCoordinate && (e.getY() - YCoordinate) / 90 < 1) {
        //             clickedRow = 5;
        //         } else if (e.getY() > YCoordinate && (e.getY() - YCoordinate) / 90 >= 1) {
        //             clickedRow = 5 + (e.getY() - YCoordinate) / Board.getTilesize();
        //         } else {

        //             clickedRow = 4 + (e.getY() - YCoordinate) / Board.getTilesize();
        //         }
        //         if (e.getX() > XCoordinate && (e.getX() - XCoordinate) / 90 < 1) {
        //             clickedCol = 5;
        //         } else if (e.getX() > XCoordinate && (e.getX() - XCoordinate) / 90 >= 1) {
        //             clickedCol = 5 + (e.getX() - XCoordinate) / Board.getTilesize();
        //         } else {
        //             clickedCol = 4 + (e.getX() - XCoordinate) / Board.getTilesize();

        //         }

        //         System.out.println("Clicked Row: " + clickedRow);
        //         System.out.println("Clicked Column: " + clickedCol);

        //     }
        // });

        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(760, 760));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Board board = new Board();
        Game game = new Game();
        frame.add(game);
        frame.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {
        double clickedRow = 4 + (e.getY() - YCoordinate) / Board.getTilesize();
        double clickedCol = 4 + (e.getX() - XCoordinate) / Board.getTilesize();

        System.out.println("Clicked Row: " + clickedRow);
        System.out.println("Clicked Column: " + clickedCol);
    }

    public static void referencePoints(Frame frame) {
        XCoordinate = frame.getWidth() / 2;
        YCoordinate = frame.getHeight() / 2;

    }

}
