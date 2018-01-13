package ch.sysout.emubro.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import ch.sysout.ui.ImageUtil;
import ch.sysout.util.Icons;
import ch.sysout.util.Messages;
import ch.sysout.util.ScreenSizeUtil;

class ViewSettingsPopupMenu extends JPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JRadioButtonMenuItem itm1 = new JRadioButtonMenuItem(Messages.get("viewCoversBiggest"));
	private JRadioButtonMenuItem itm2 = new JRadioButtonMenuItem(Messages.get("viewCoversBig"));
	private JRadioButtonMenuItem itm3 = new JRadioButtonMenuItem(Messages.get("viewCoversNormal"));
	private JRadioButtonMenuItem itm4 = new JRadioButtonMenuItem(Messages.get("viewCoversSmall"));
	private JRadioButtonMenuItem itm5 = new JRadioButtonMenuItem(Messages.get("viewCoversSmallest"));
	private JRadioButtonMenuItem itmList = new JRadioButtonMenuItem(Messages.get("viewListHorizontalSb"));
	private JRadioButtonMenuItem itmElements = new JRadioButtonMenuItem(Messages.get("viewListVerticalSb"));
	private JRadioButtonMenuItem itmDetails = new JRadioButtonMenuItem(Messages.get("viewDetails"));

	public ViewSettingsPopupMenu() {
		ButtonGroup grp = new ButtonGroup();
		grp.add(itm1);
		grp.add(itm2);
		grp.add(itm3);
		grp.add(itm4);
		grp.add(itm5);
		grp.add(itmList);
		grp.add(itmElements);
		grp.add(itmDetails);

		add(itm1);
		add(itm2);
		add(itm3);
		add(itm4);
		add(itm5);
		add(new JSeparator());
		add(itmList);
		add(itmElements);
		add(itmDetails);

		setAccelerators();
		setIcons();
	}

	private void setAccelerators() {
	}

	private void setIcons() {
		int size = ScreenSizeUtil.is3k() ? 24 : 16;
		itm1.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewCovers", size, size)));
		itm2.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewCovers", size, size)));
		itm3.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewCovers", size, size)));
		itm4.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewCovers", size, size)));
		itm5.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewCovers", size, size)));
		itmList.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewList", size, size)));
		itmElements.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewList", size, size)));
		itmDetails.setIcon(ImageUtil.getImageIconFrom(Icons.get("viewTable", size, size)));
	}

	public void addChangeToListViewListener(ActionListener l) {
		itmList.addActionListener(l);
		itmElements.addActionListener(l);
	}

	public void addChangeToTableViewListener(ActionListener l) {
		itmDetails.addActionListener(l);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		e.getSource();
	}
}