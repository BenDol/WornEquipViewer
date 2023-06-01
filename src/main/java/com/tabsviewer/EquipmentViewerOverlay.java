package net.runelite.client.plugins.tabsviewer;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.tabsviewer.config.ViewerMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.Point;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EquipmentViewerOverlay extends Overlay
{
	private static final int PLACEHOLDER_WIDTH = 36;
	private static final int PLACEHOLDER_HEIGHT = 32;
	private static final ImageComponent PLACEHOLDER_IMAGE = new ImageComponent(new BufferedImage(PLACEHOLDER_WIDTH, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR));
	private final ItemManager itemManager;
	private final TabsViewerConfig config;
	private final Client client;

	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	private EquipmentViewerOverlay(Client client, ItemManager itemManager, TabsViewerConfig config)
	{
		this.itemManager = itemManager;
		this.config = config;
		this.client = client;

		setPosition(OverlayPosition.BOTTOM_RIGHT);

	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.setWrap(true);
		panelComponent.setGap(new Point(6, 4));
		panelComponent.setPreferredSize(new Dimension(3 * (Constants.ITEM_SPRITE_WIDTH + 6 + 14), 0));
		panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);

		if (config.tabsViewMode() != ViewerMode.EQP && config.tabsViewMode() != ViewerMode.BOTH)
		{
			return null;
		}

		final ItemContainer itemContainer = client.getItemContainer(InventoryID.EQUIPMENT);

		if (itemContainer == null)
		{
			return null;
		}
		final Item[] items = itemContainer.getItems();

		ArrayList<ArrayList<Item>> loop = getEquipment(items);
		panelComponent.getChildren().clear();

		for (int i = 0; i < loop.size(); i++) {
			ArrayList<Item> row = loop.get(i);

			for (int j = 0; j < row.size(); j++) {

				if (i == 0) {
					if (j == 0) {
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (38 * 1.5 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
					} else if (j == 2) {
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (38 * 1.5), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
					} else {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(image));
									continue;
								}
							}  else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
							}
						}
					}
				}



				if (i == 1) {
					if (j == 0) {
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (10 * 1.5 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(image));

									continue;
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
							}
						}
					} else if (j == 1) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(image));
									continue;
								}
							}
							else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
							}
						}
					} else if (j == 2) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{

									panelComponent.getChildren().add(new ImageComponent(image));
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (10 * 1.5 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

									continue;
								}
							}
							else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (10 * 1.5 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
							}
						}
					}
				}


				if (i == 2) {
					// weapon
					if (j == 0) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (3), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
									panelComponent.getChildren().add(new ImageComponent(image));
									continue;
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (3), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

							}
						}
					} else if (j == 1) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (4 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
									panelComponent.getChildren().add(new ImageComponent(image));
									continue;
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (4), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

							}
						}
					} else if (j == 2) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (2 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
									panelComponent.getChildren().add(new ImageComponent(image));

									continue;
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (26), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (12), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

							}
						}

					}
				}
				//legs
				if (i == 3) {
					if (j == 0) {
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (6 * 1.5 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

						panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36 * 1.5 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
					} else if (j == 2) {
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36 * 1.5), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
					} else {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(image));
									continue;
								}
							}else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
							}
						}
					}
				}
				if (i == 4) {
					// boots
					if (j == 0) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (3), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
									panelComponent.getChildren().add(new ImageComponent(image));
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (3), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

							}
						}
					} else if (j == 1) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (2 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
									panelComponent.getChildren().add(new ImageComponent(image));
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (2), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (36), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));

							}
						}
					} else if (j == 2) {
						if (i < items.length)
						{
							final Item item = row.get(j);
							if (item != null && item.getQuantity() > 0)
							{
								final BufferedImage image = getImage(item);
								if (image != null)
								{
									panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (2 ), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
									panelComponent.getChildren().add(new ImageComponent(image));

									continue;
								}
							} else {
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (26), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
								panelComponent.getChildren().add(new ImageComponent(new BufferedImage((int) (12), (int) (32 ), BufferedImage.TYPE_4BYTE_ABGR)));
							}
						}

					}
				}
			}
		}

		return panelComponent.render(graphics);
	}

	private ArrayList<ArrayList<Item>> getEquipment(Item[] items)
	{
		ArrayList<Item> row1 = new ArrayList<>();
		addItemIfExists(row1, items, -1);
		addItemIfExists(row1, items, 0);
		addItemIfExists(row1, items, -1);
		ArrayList<Item> row2 = new ArrayList<>();
		addItemIfExists(row2, items, 1);
		addItemIfExists(row2, items, 2);
		addItemIfExists(row2, items, 13);
		ArrayList<Item> row3 = new ArrayList<>();
		addItemIfExists(row3, items, 3);
		addItemIfExists(row3, items, 4);
		addItemIfExists(row3, items, 5);
		ArrayList<Item> row4 = new ArrayList<>();
		addItemIfExists(row4, items, 6);
		addItemIfExists(row4, items, 7);
		addItemIfExists(row4, items, 8);
		ArrayList<Item> row5 = new ArrayList<>();
		addItemIfExists(row5, items, 9);
		addItemIfExists(row5, items, 10);
		addItemIfExists(row5, items, 12);

		ArrayList<ArrayList<Item>> returnThis = new ArrayList<>();
		returnThis.add(row1);
		returnThis.add(row2);
		returnThis.add(row3);
		returnThis.add(row4);
		returnThis.add(row5);

		return returnThis;
	}

	private void addItemIfExists(ArrayList<Item> row, Item[] items, int index)
	{
		if (index == -1)
		{
			row.add(null);
		} else if (index >= items.length)
		{
			row.add(null);
		} else {
			row.add(items[index]);
		}
	}

	private BufferedImage getImage(Item item)
	{
		return itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
	}
}