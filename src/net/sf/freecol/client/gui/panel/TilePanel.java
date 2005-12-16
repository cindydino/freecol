
package net.sf.freecol.client.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.util.ArrayList;

import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.sf.freecol.client.gui.Canvas;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.Goods;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Tile;

/**
 * This panel is used to show information about a tile.
 */
public final class TilePanel extends FreeColDialog implements ActionListener {
    public static final String  COPYRIGHT = "Copyright (C) 2003-2005 The FreeCol Team";
    public static final String  LICENSE = "http://www.gnu.org/licenses/gpl.html";
    public static final String  REVISION = "$Revision$";

    private static final Logger logger = Logger.getLogger(EventPanel.class.getName());

    private static final int OK = 0;
    private final Canvas canvas;

    private final int[] goods;
    private final int number;
    
    private final JPanel goodsPanel;
    private final JLabel tileNameLabel;
    private final JLabel ownerLabel;
    private final JButton okButton;
    private final JLabel[] labels;
    
    /**
    * The constructor that will add the items to this panel.
    */
    public TilePanel(Canvas parent) {
        canvas = parent;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        tileNameLabel = new JLabel("", JLabel.CENTER);
        tileNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(tileNameLabel);

        ownerLabel = new JLabel("", JLabel.CENTER);
        ownerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(ownerLabel);        
        
        ArrayList farmedGoods = new ArrayList();
        for (int i = 0; i < Goods.NUMBER_OF_TYPES; i++) {
            if (Goods.isFarmedGoods(i)) {
                farmedGoods.add(new Integer(i));
            }

        }
        number = farmedGoods.size();

        goodsPanel = new JPanel();
        goodsPanel.setLayout(new FlowLayout());

        goods = new int[number];
        labels = new JLabel[number];
        for (int k = 0; k < number; k++) {
            int index = ((Integer) farmedGoods.get(k)).intValue();
            goods[k] = index;
            labels[k] = new JLabel(canvas.getImageProvider().getGoodsImageIcon(index));
            goodsPanel.add(labels[k]);
        }
        
        goodsPanel.setSize(getPreferredSize());
        add(goodsPanel);

        okButton = new JButton(Messages.message("ok"));
        okButton.setActionCommand(String.valueOf(OK));
        okButton.addActionListener(this);
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Use ESCAPE for closing the panel:
        InputMap inputMap = new ComponentInputMap(okButton);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "pressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "released");
        SwingUtilities.replaceUIInputMap(okButton, JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);        

        add(okButton);

        setSize(450, 120);
    }


    public void requestFocus() {
        okButton.requestFocus();
    }


    /**
    * Initializes the information that is being displayed on this panel.
    * The information displayed will be based on the given tile.
    * @param settlement The Tile whose information should be displayed.
    */
    public void initialize(Tile tile) {
        tileNameLabel.setText(tile.getName());
        String ownerName = Player.getNationAsString(tile.getNationOwner());
        if (ownerName.equals("INVALID")) {
            ownerLabel.setText("");
        } else {
            ownerLabel.setText(ownerName);
        }
        for (int i = 0; i < number; i++) {
            labels[i].setText(String.valueOf(tile.potential(goods[i])));
        }
    }


    /**
    * This function analyses an event and calls the right methods to take
    * care of the user's requests.
    * @param event The incoming ActionEvent.
    */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            switch (Integer.valueOf(command).intValue()) {
                case OK:
                    setResponse(new Boolean(true));
                    break;
                default:
                    logger.warning("Invalid Actioncommand: invalid number.");
            }
        } catch (NumberFormatException e) {
            logger.warning("Invalid Actioncommand: not a number.");
        }
    }
}
