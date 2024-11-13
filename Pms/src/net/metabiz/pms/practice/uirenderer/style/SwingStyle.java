package net.metabiz.pms.practice.uirenderer.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;

public class SwingStyle {
    
    
    public static void textFieldStyleChanger(JTextField textField) {
        textField.setBackground(new Color(240, 240, 240));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }
    
    public static void btnStyleChanger(JButton btn) {
        btn.setBackground(new Color(92, 179, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(72, 159, 235));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(92, 179, 255));
            }
        });
        
    }
    
    
}
