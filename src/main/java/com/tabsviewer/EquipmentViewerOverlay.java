package com.tabsviewer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;

public class EquipmentViewerOverlay extends OverlayPanel
{
	private static final int PLACEHOLDER_WIDTH = 36;
	private static final int PLACEHOLDER_WIDTH_150_PERCENT = 57;
	private static final int PLACEHOLDER_HEIGHT = 32;
	private static final ImageComponent PLACEHOLDER_IMAGE = new ImageComponent(new BufferedImage(PLACEHOLDER_WIDTH, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR));
	private final ItemManager itemManager;
	private final Client client;

	@Inject
	private EquipmentViewerOverlay(Client client, ItemManager itemManager)
	{
		setPosition(OverlayPosition.BOTTOM_RIGHT);
		panelComponent.setWrap(true);
		panelComponent.setGap(new Point(6, 4));
		panelComponent.setPreferredSize(new Dimension(3 * (Constants.ITEM_SPRITE_WIDTH + 20), 0));
		panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);

		this.itemManager = itemManager;
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final ItemContainer itemContainer = client.getItemContainer(InventoryID.EQUIPMENT);

		if (itemContainer == null)
		{
			return null;
		}
		final Item[] items = itemContainer.getItems();

		ArrayList<ArrayList<Item>> loop = getEquipment(items);
		panelComponent.getChildren().clear();

		buildFirstRow(loop.get(0));
		buildSecondRow(loop.get(1));
		buildThirdRow(loop.get(2));
		buildForthRow(loop.get(3));
		buildFifthRow(loop.get(4));

		return panelComponent.render(graphics);
	}

	private void buildFirstRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(PLACEHOLDER_WIDTH_150_PERCENT, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else if (j == 2)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(PLACEHOLDER_WIDTH_150_PERCENT, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(image));
					}
				}
				else
				{
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
		}
	}

	private void buildSecondRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(15, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
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
				else
				{
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
			else if (j == 1)
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
				else
				{
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
			else if (j == 2)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(image));
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(15, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
					}
				}
				else
				{
					panelComponent.getChildren().add(new ImageComponent(new BufferedImage(15, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
		}
	}

	private void buildThirdRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(3, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
						panelComponent.getChildren().add(new ImageComponent(image));
						continue;
					}
				}
				else
				{
					panelComponent.getChildren().add(new ImageComponent(new BufferedImage(3, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);

				}
			}
			else if (j == 1)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(4, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
						panelComponent.getChildren().add(new ImageComponent(image));
						continue;
					}
				}
				else
				{
					panelComponent.getChildren().add(new ImageComponent(new BufferedImage(4, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);

				}
			}
			else if (j == 2)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(2, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
						panelComponent.getChildren().add(new ImageComponent(image));

						continue;
					}
				}
				else
				{
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
		}
	}

	private void buildForthRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(9, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(PLACEHOLDER_WIDTH_150_PERCENT, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else if (j == 2)
			{
				panelComponent.getChildren().add(new ImageComponent(new BufferedImage(PLACEHOLDER_WIDTH_150_PERCENT, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
			}
			else
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
				else
				{
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
		}
	}

	private void buildFifthRow(ArrayList<Item> row)
	{
		for (int j = 0; j < row.size(); j++)
		{
			if (j == 0)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(3, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
						panelComponent.getChildren().add(new ImageComponent(image));
					}
				}
				else
				{
					panelComponent.getChildren().add(new ImageComponent(new BufferedImage(3, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);

				}
			}
			else if (j == 1)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(2, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
						panelComponent.getChildren().add(new ImageComponent(image));
					}
				}
				else
				{
					panelComponent.getChildren().add(new ImageComponent(new BufferedImage(2, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);

				}
			}
			else if (j == 2)
			{
				final Item item = row.get(j);
				if (item != null && item.getQuantity() > 0)
				{
					final BufferedImage image = getImage(item);
					if (image != null)
					{
						panelComponent.getChildren().add(new ImageComponent(new BufferedImage(2, PLACEHOLDER_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR)));
						panelComponent.getChildren().add(new ImageComponent(image));
						continue;
					}
				}
				else
				{
					panelComponent.getChildren().add(PLACEHOLDER_IMAGE);
				}
			}
		}
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
		}
		else if (index >= items.length)
		{
			row.add(null);
		}
		else
		{
			row.add(items[index]);
		}
	}

	private BufferedImage getImage(Item item)
	{
		return itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
	}
}
